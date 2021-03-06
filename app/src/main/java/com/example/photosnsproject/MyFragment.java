package com.example.photosnsproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class MyFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Context context;
    private static final int IMAGE_REQUEST = 1;
    private Uri filePath;
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference db=database.getReference();
    private FirebaseStorage storage=FirebaseStorage.getInstance();
    private StorageReference storageReference=storage.getReference();
    private LinearLayout mf_gallery;
    private LinearLayout mf_follower;
    private LinearLayout mf_following;
    private TextView mf_id;
    private TextView num_gallery;
    private TextView num_follower;
    private TextView num_following;
    CircleImageView profile;
    private ArrayList<String> imagelist;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String userID;
    private Follow_main follow_main;








    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        context = getActivity();
        view = inflater.inflate(R.layout.activity_my_fragment, container, false);
        userID =PreferenceManager.getUserId(view.getContext());
        profile=view.findViewById(R.id.profile);
        mf_id=view.findViewById(R.id.mf_id);
        mf_gallery=view.findViewById(R.id.mf_gallery);
        mf_follower=view.findViewById(R.id.mf_follower);
        mf_following=view.findViewById(R.id.mf_following);
        mf_id=view.findViewById(R.id.mf_id);
        num_gallery=view.findViewById(R.id.num_gallery);
        num_follower=view.findViewById(R.id.num_follower);
        num_following=view.findViewById(R.id.num_following);
        mf_gallery.setOnClickListener(this);
        mf_follower.setOnClickListener(this);
        mf_following.setOnClickListener(this);
        profile.setOnClickListener(this);
        recyclerView = (view).findViewById(R.id.mf_rcy);
        recyclerView.setHasFixedSize(true); //????????????
        layoutManager = new GridLayoutManager(view.getContext(),3);
        recyclerView.setLayoutManager(layoutManager);
        imagelist = new ArrayList<>();
        adapter = new GalleryAdapter(imagelist,context,userID);
        recyclerView.setAdapter(adapter);
        mf_id.setText(userID);
        follow_main=new Follow_main();

        //?????????
        db.child("Follow").child("Follower").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int cnt=0;
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    if(snapshot1 !=null){
                        cnt++;
                    }
                }
                num_follower.setText(cnt+"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //?????????
        db.child("Follow").child("Following").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int cnt=0;
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    if(snapshot1 !=null){
                        cnt++;
                    }
                }
                num_following.setText(cnt+"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //?????????
        db.child("Users").child(userID).child("post").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int cnt=0;
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    if(snapshot1 !=null){
                        cnt++;
                    }
                }
                num_gallery.setText(cnt+"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        StorageReference loadreference=storageReference.child("profile").child(userID+".png");

        loadreference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(profile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                profile.setImageResource(R.mipmap.ic_launcher);
            }
        });

        db.child("Users").child(userID).child("post").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot sn : snapshot.getChildren()) {
                    imagelist.add(sn.getKey());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*
        StorageReference mgreference=storageReference.child(userID);


        mgreference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for(StorageReference item:listResult.getItems()){
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imagelist.add(uri.toString());
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
        */


        setFloatingButton();
        return view;
    }

    //?????????????????????
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
                StorageReference deletereference=storageReference.child("profile").child(userID+".png");
                deletereference.delete();
                bottomSheetDialog.dismiss();
                profile.setImageResource(R.mipmap.ic_launcher);
            }
        });





    }
    //??????????????????
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
            //??????????????? ?????????.
            StorageReference uploadreference=storageReference.child("profile").child(userID+".png");
            uploadreference.delete();
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("????????????...");
            progressDialog.show();
            uploadreference.putFile(filePath)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //????????? ?????? Dialog ?????? ??????
                            Toast.makeText(getContext(), "????????? ??????!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "????????? ??????!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
        }
    }







    private void setFloatingButton() {
        FloatingActionButton fab = view.findViewById(R.id.btnPlus);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlusPosting.class);
            startActivity(intent);
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.profile:
                setBottomSheetDialog(view);
                break;


            case R.id.mf_follower:
                String id1 = userID;
                Bundle bundle1=new Bundle(2);
                bundle1.putString("id",id1);
                bundle1.putInt("page",0);
                follow_main.setArguments(bundle1);
                ((MainActivity)context).follow(follow_main);

                break;

            case R.id.mf_following:
                String id2 = userID;
                Bundle bundle2=new Bundle(2);
                bundle2.putString("id",id2);
                bundle2.putInt("page",1);
                follow_main.setArguments(bundle2);
                ((MainActivity)context).follow(follow_main);


                break;

        }
    }
}