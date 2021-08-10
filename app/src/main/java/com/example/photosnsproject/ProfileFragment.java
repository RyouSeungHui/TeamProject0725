package com.example.photosnsproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private static final int IMAGE_REQUEST = 1;
    private Uri filePath;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference db = database.getReference();
    private FirebaseStorage storage=FirebaseStorage.getInstance();
    private StorageReference storageReference=storage.getReference();
    TextView pf_id;
    String string_pf_id;
    Button pf_follow_btn,pf_follower_btn,pf_following_btn;
    CircleImageView profile;
    private ArrayList<String> imagelist;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.activity_profile_fragment, container, false);
        profile=view.findViewById(R.id.profile);
        pf_id = (TextView) view.findViewById(R.id.pf_id); //2
        string_pf_id = getArguments().getString("id");
        pf_follow_btn=(Button) view.findViewById(R.id.pf_follow_btn);
        pf_follower_btn=(Button) view.findViewById(R.id.pf_follower_btn);
        pf_following_btn=(Button) view.findViewById(R.id.pf_following_btn);
        pf_follow_btn.setOnClickListener(this);
        pf_follower_btn.setOnClickListener(this);
        pf_following_btn.setOnClickListener(this);
        profile.setOnClickListener(this);
        pf_id.setText(string_pf_id);
        recyclerView = (view).findViewById(R.id.myprofile_rcy);
        recyclerView.setHasFixedSize(true); //정리한번
        layoutManager = new GridLayoutManager(view.getContext(),3);
        recyclerView.setLayoutManager(layoutManager);
        imagelist = new ArrayList<>();
        adapter = new GalleryAdapter(imagelist, context);
        recyclerView.setAdapter(adapter);



        //프로필
        StorageReference loadreference=storageReference.child("profile").child(string_pf_id+".png");

        //null이 인식이 안되는건가?
        if(loadreference==null){

        }
        else{
            loadreference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getContext()).load(uri).into(profile);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    profile.setImageResource(R.mipmap.ic_launcher);
                }
            });
        }

        //https://www.geeksforgeeks.org/how-to-view-all-the-uploaded-images-in-firebase-storage/
        //자기 프로필 게시물
        StorageReference mgreference=storageReference.child(string_pf_id);

        mgreference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imagelist.add(uri.toString());
                                    adapter.notifyDataSetChanged();
                                    Log.e("Itemvalue",uri.toString());
                                }
                            });

                        }
                    }
                });


        return view;
    }


    //바텀다이어로그
    private void setBottomSheetDialog(View view){

        BottomSheetBehavior mBehavior;
        View v;

        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(view.getContext());
        v= LayoutInflater.from(getContext()).inflate(R.layout.layout_bottomsheetdialog_profile,(LinearLayout)view.findViewById(R.id.bottomSheetContainer));


        LinearLayout bs1 = v.findViewById(R.id.bs1);
        LinearLayout bs2 = v.findViewById(R.id.bs2);

        bottomSheetDialog.setContentView(v);
        mBehavior = BottomSheetBehavior.from((View)v.getParent());
        mBehavior.setPeekHeight(4000);
        bottomSheetDialog.show();

        bs1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
                bottomSheetDialog.dismiss();
            }
        });

        bs2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference deletereference=storageReference.child("profile").child(string_pf_id+".png");
                deletereference.delete();
                bottomSheetDialog.dismiss();
                profile.setImageResource(R.mipmap.ic_launcher);
            }
        });





    }

    //갤러리드가기
    public void openImage(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK){
            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                uploadFile();
                profile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile() {

        if (filePath != null) {
            //원래있던거 지우기.
            StorageReference uploadreference=storageReference.child("profile").child(string_pf_id+".png");
            uploadreference.delete();
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("업로드중...");
            progressDialog.show();
            uploadreference.putFile(filePath)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            Toast.makeText(getContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    @SuppressWarnings("VisibleForTests")
                    double progress = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                }
            });

        } else {
            Toast.makeText(getContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.pf_follow_btn :
                db.child("Follow").child("Follower").child(string_pf_id).push().setValue(NowUser.id);
                db.child("Follow").child("Following").child(NowUser.id).push().setValue(string_pf_id);
                break;

            case R.id.pf_follower_btn:
                FollowFragment.userid=string_pf_id;
                ((MainActivity)context).follow(FollowFragment.newInstance());
                break;

            case R.id.profile:
                setBottomSheetDialog(view);
                break;

        }
    }
}