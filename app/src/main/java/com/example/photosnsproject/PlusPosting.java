
package com.example.photosnsproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlusPosting extends AppCompatActivity {
    private ImageView imgPost;
    private static final int REQUEST_CODE = 0;
    private Spinner spinFriend;
    private Spinner spinPublic;
    private TextView tvTag, tvFriend, tvLocation, tvPublic;
    private EditText etTag;
    private Button btnTag;
    private ArrayList<String> arrTag, arrFriend, selected, arrPublic, selected2;
    private String strTag, strFriend, strAddress, strPublic, strContents;
    private Float lati=0.0f, longi=0.0f;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Bitmap bitmap;
    private static final int PERMISSIONS_REQUEST=1;
    private EditText etContents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_posting);
        init();
    }

    private void init() {
        //GET IMAGE
        imgPost = findViewById(R.id.imgPost);
        imgPost.setOnClickListener(v -> {
            onCheckPermission();
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE);
        });

        tvLocation = findViewById(R.id.tvLocation);


        //HASH TAG
        tvTag = findViewById(R.id.tvTag);
        etTag = findViewById(R.id.etTag);
        btnTag = findViewById(R.id.btnTag);
        strTag="";
        arrTag = new ArrayList<>();
        btnTag.setOnClickListener(v -> {
            if(!etTag.equals("")){
                arrTag.add(etTag.getText().toString());
                strTag += " #"+etTag.getText().toString();
                etTag.setText("");
                tvTag.setText(strTag);
            }
        });

        //Friend TAG (친구 추가 기능 완료시 수정해야 함)
        spinFriend = findViewById(R.id.spinFriend);
        tvFriend = findViewById(R.id.tvFriend);
        strFriend="";
        arrFriend = new ArrayList<>();
        selected = new ArrayList<>();

        arrFriend.add("내 친구 리스트");
        arrFriend.add("우승수");
        arrFriend.add("장호성");
        arrFriend.add("이지훈");
        arrFriend.add("유승희");


        //Public TAG
        spinPublic = findViewById(R.id.spinPublic);
        tvPublic = findViewById(R.id.tvPublic);
        strPublic="";
        arrPublic = new ArrayList<>();
        selected2 = new ArrayList<>();

        arrPublic.add("공개 범위");
        arrPublic.add("전체 공개");
        arrPublic.add("친구만 공개");
        arrPublic.add("비공개");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, arrFriend);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinFriend.setAdapter(adapter);
        spinFriend.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    selected.add(arrFriend.get(position));
                    strFriend+=" @"+arrFriend.get(position);
                    tvFriend.setText(strFriend);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, arrPublic);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPublic.setAdapter(adapter2);
        spinPublic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    selected2.add(arrPublic.get(position));
                    strPublic=arrPublic.get(position);
                    tvPublic.setText(strPublic);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    private void onCheckPermission() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                ||ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(this, "사진 업로드를 위해서는 권한 설정이 필요합니다", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_MEDIA_LOCATION}, PERMISSIONS_REQUEST);
            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_MEDIA_LOCATION}, PERMISSIONS_REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSIONS_REQUEST:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "앱 실행을 위한 권한이 설정되었습니다", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, "앱 실행을 위한 권한 설정이 취소되었습니다", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    imgPost.setImageBitmap(bitmap);
                    getGPS(data.getData());
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void getGPS(Uri imgUri) {
        String imagePath = getRealPathFromURI(imgUri);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        String latitude = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        String latitudeRef = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
        String longitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        String longitudeRef = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

        if((latitude!=null) && (latitudeRef!=null)&&(longitude!=null)&&(latitudeRef!=null)){
            if(latitudeRef.equals("N")) lati = convertToDegree(latitude);
            else lati = 0-convertToDegree(latitude);

            if(longitudeRef.equals("E")) longi = convertToDegree(longitude);
            else longi = 0-convertToDegree(longitude);
        }
        strAddress = getCurrentAddress(lati, longi);
        tvLocation.setText(strAddress);

        bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        imgPost.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기
    }

    private String getCurrentAddress(Float lati, Float longi) {
        double latitude = (double)lati;
        double longitude = (double)longi;

        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = null;

        try{
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    8
            );
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
        if(addresses!=null){
            if(addresses.size()==0){
                Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            }
            else{
                Address address = addresses.get(0);
                return address.getAddressLine(0);
            }
        }
        return "";
    }

    private Float convertToDegree(String stringDMS) {
        Float result = null;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double d0 = new Double(stringD[0]);
        Double d1 = new Double(stringD[1]);
        Double floatD = d0/d1;

        String[] stringM = DMS[1].split("/", 2);
        Double m0 = new Double(stringM[0]);
        Double m1 = new Double(stringM[1]);
        Double floatM = m0/m1;

        String[] stringS = DMS[2].split("/", 2);
        Double s0 = new Double(stringS[0]);
        Double s1 = new Double(stringS[1]);
        Double floatS = s0/s1;

        result = new Float(floatD+(floatM/60)+(floatS/3600));

        return result;
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) { return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) { return 270; } return 0;
    }


    private Bitmap rotate(Bitmap src, float degree) {
        Matrix matrix = new Matrix(); // 회전 각도 셋팅
        matrix.postRotate(degree);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String getTime = dateFormat.format(date);

        return getTime;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private String getRealPathFromURI(Uri contentUri) {
        contentUri = MediaStore.setRequireOriginal(contentUri);
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        return cursor.getString(column_index);
    }

    public void onClickCancel(View view) {
        finish();
    }

    public void onClickOkay(View view) {
        String userID = PreferenceManager.getUserId(getApplicationContext());

        // 내용 입력
        etContents = (EditText)findViewById(R.id.etContents);
        strContents = etContents.getText().toString();


        //파이어베이스 데이터베이스 연동
        firebaseDatabase = FirebaseDatabase.getInstance();

        //DatabaseReference는 데이터베이스의 특정 위치로 연결
        databaseReference = firebaseDatabase.getReference("Users").child(userID).child("post").push();
        PostItem postItem = new PostItem(strAddress, lati, longi, arrTag, selected, selected2, strContents, getTime());
        databaseReference.setValue(postItem);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage  .getReference().child(userID).child(databaseReference.getKey());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "게시물 작성 실패", Toast.LENGTH_LONG).show();
        }).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(getApplicationContext(), "게시물 작성 성공", Toast.LENGTH_LONG).show();
        });
    }
}