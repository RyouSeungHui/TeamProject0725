package com.example.photosnsproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment {
    private View view;
    private Context context;

    private GpsTracker gpsTracker;
    private double latitude=0.0, longitude=0.0;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private ArrayList<String> path, postLoc;

    private FirebaseDatabase database;
    private DatabaseReference db;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public static MapFragment newInstance(){
        return new MapFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        context = getActivity();
        view = inflater.inflate(R.layout.activity_map_fragment, container, false);

        checkPermissions();
        init();
        setFloatingButton();

        return view;
    }

    private void init() {
        TextView tvMyGps = view.findViewById(R.id.tvMyGPS);
        ImageView btnRenew = view.findViewById(R.id.btnRenew);
        EditText eddis = view.findViewById(R.id.ed_dis);

        path=new ArrayList<>();
        postLoc = new ArrayList<>();

        recyclerView = (view).findViewById(R.id.map_rcy);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MappathAdapter(path,context);
        recyclerView.setAdapter(adapter);

        database=FirebaseDatabase.getInstance();
        db=database.getReference();

        btnRenew.setOnClickListener(v -> {
            Animation anim = AnimationUtils.loadAnimation(context, R.anim.rotate_anim);
            btnRenew.startAnimation(anim);
            gpsTracker = new GpsTracker(context);

            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();

            eddis.setText("100");
            Findphotobygps(latitude,longitude,100);

            String address = getCurrentAddress(latitude, longitude);
            tvMyGps.setText(address+" 근처의 인기 게시물이에요");
        });

        btnRenew.callOnClick();
        eddis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(eddis.getText().toString().equals(""))
                {
                    eddis.setText("0");
                }
                double dis = Double.parseDouble(eddis.getText().toString());
                Findphotobygps(latitude,longitude,dis);
            }
        });

    }

    private void checkPermissions() {
        if(!checkLocationServicesStatus()){
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSIONS_REQUEST_CODE && grantResults.length==REQUIRED_PERMISSIONS.length){
            boolean checkResult = true;

            for(int result : grantResults){
                if(result!=PackageManager.PERMISSION_GRANTED){
                    checkResult = false;
                    break;
                }
            }
            if(checkResult){

            }
            else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])||ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[1])){
                    Toast.makeText(context, "위치 권한이 거부되었습니다", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }else{
                    Toast.makeText(context, "위치 권한이 거부되었습니다. 설정(앱 정보)에서 위치 권한을 허용해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getCurrentAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7
            );
        }catch (IOException ioException){
            return "지오코더 서비스 사용불가";
        }catch (IllegalArgumentException illegalArgumentException){
            return "잘못된 GPS 좌표";
        }
        if(addresses==null || addresses.size()==0){
            return "주소 미발견";
        }
        Address address = addresses.get(0);
        return address.getAddressLine(0).toString();
    }

    private void checkRunTimePermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(hasFineLocationPermission== PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission==PackageManager.PERMISSION_GRANTED){

        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])){
                Toast.makeText(context, "근처의 게시물을 보기 위해서는 위치 접근 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }else{
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("근처의 게시물을 보기 위해서는 위치 서비스가 필요합니다.\n위치 권한을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, GPS_ENABLE_REQUEST_CODE);
        });
        builder.setNegativeButton("취소", (dialog, which) -> {
            dialog.cancel();
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GPS_ENABLE_REQUEST_CODE:
                if(checkLocationServicesStatus()){
                    checkRunTimePermission();
                    return;
                }
                break;
        }
    }

    private boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void setFloatingButton() {
        FloatingActionButton fab = view.findViewById(R.id.btnMap);
        fab.setOnClickListener(v -> {
            if(latitude!=0.0 && longitude!=0.0){
                Bundle bundle = new Bundle();
                bundle.putDouble("latitude", latitude);
                bundle.putDouble("longitude", longitude);
                bundle.putStringArrayList("path", path);
                bundle.putStringArrayList("postLoc", postLoc);
                SubMapFragment subMapFragment = SubMapFragment.newInstance();
                subMapFragment.setArguments(bundle);
                ((MainActivity)getActivity()).follow(subMapFragment);
            }
        });
    }

    private void Findphotobygps(double latitude, double longitude,double distance)
    {
        path.clear();
        postLoc.clear();
        adapter.notifyDataSetChanged();
        db.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                path.clear();
                postLoc.clear();
                for(DataSnapshot usersnap : snapshot.getChildren())
                {
                    String userId = usersnap.getKey();
                    db.child("Users").child(userId).child("post").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot postsnap : snapshot.getChildren())
                            {
                                PostItem post = postsnap.getValue(PostItem.class);
                                String postId = postsnap.getKey();
                                if(distance >= ruler(latitude,longitude,post.getLati(),post.getLongi()))
                                {
                                    path.add(userId+"/"+postId);
                                    postLoc.add(post.getLati()+"/"+post.getLongi());
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private double ruler(double first_latitude,double first_longitude,double second_latitude,double second_longitude)
    {
        double distance=0.0;

        double R = 6372.8;

        double dLat = Math.toRadians(first_latitude-second_latitude);
        double dLon = Math.toRadians(first_longitude-second_longitude);
        double fr_latitude = Math.toRadians(first_latitude);
        double sr_latitude = Math.toRadians(second_latitude);

        double tempt = Math.pow(Math.sin(dLat/2),2)+Math.pow(Math.sin(dLon/2),2)*Math.cos(fr_latitude)*Math.cos(sr_latitude);
        double c = 2*Math.asin(Math.sqrt(tempt));

        distance = R*c*1000;

        return distance;
    }

}