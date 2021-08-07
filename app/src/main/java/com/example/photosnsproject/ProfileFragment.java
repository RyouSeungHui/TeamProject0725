package com.example.photosnsproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Context context;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageuri;
    TextView pf_id;
    String string_pf_id;
    Button pf_follow_btn,pf_follower_btn,pf_following_btn;
    CircleImageView profile;
    int flag=-1;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference db = database.getReference();
    private FirebaseStorage storage=FirebaseStorage.getInstance();
    private StorageTask uploadTask;


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



        //프로필
        db.child("Users").child(string_pf_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);

                if(user.getProfileuri()==null){
                    profile.setImageResource(R.mipmap.ic_launcher);
                }
                else{
                    /*
                    Activity activity = (Activity) view.getContext();
                    if(activity.isFinishing()){
                        return;
                    }
                     */
                    Glide.with(view.getContext()).load(user.getProfileuri()).into(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void setBottomSheetDialog(View view){

        BottomSheetBehavior mBehavior;
        View v;

        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(view.getContext());
        v= LayoutInflater.from(getContext()).inflate(R.layout.layout_bottomsheetdialog_profile,(LinearLayout)view.findViewById(R.id.bottomSheetContainer));


        LinearLayout bs1 = v.findViewById(R.id.bs1);
        LinearLayout bs2 = v.findViewById(R.id.bs2);

        bs1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(v);
        mBehavior = BottomSheetBehavior.from((View)v.getParent());
        mBehavior.setPeekHeight(4000);
        bottomSheetDialog.show();



    }

    public void openImage(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private void uploadImage(){
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("이미지 업로드");
        progressDialog.show();

        if(imageuri!=null){
            StorageReference storageReference=storage.getReferenceFromUrl("gs://photosns-ac77b.appspot.com/");
            final String profileimagename=System.currentTimeMillis()+"";

            Uri file=imageuri;
            final StorageReference riverRef=storageReference.child("profile/"+profileimagename);
            UploadTask uploadTask=riverRef.putFile(file);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    flag=1;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "업로드 실패", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    progressDialog.setMessage("업로드 중..");
                }
            });

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return riverRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful() && flag == 1){
                        Uri downloadUri = task.getResult();
                        profileupdate(downloadUri.toString(), profileimagename);
                        progressDialog.dismiss();
                    }
                }
            });

        }
        else{
            Toast.makeText(getContext(),"이미지를 선택해주세요",Toast.LENGTH_SHORT).show();
        }

    }

    private void profileupdate(final String downloadUri, final String profileimagename){

        db.child("Users").child(string_pf_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                user.setProfileimagename(profileimagename);
                user.setProfileuri(downloadUri);
                db.setValue(user);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMAGE_REQUEST && requestCode==RESULT_OK && data!=null && data.getData()!=null){
            imageuri=data.getData();

            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
            }
            else{

                db.child("Users").child(string_pf_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user=snapshot.getValue(User.class);
                        if(user.getProfileuri()==null){
                            uploadImage();
                        }
                        else{
                            StorageReference reference5 = FirebaseStorage.getInstance().getReferenceFromUrl("gs://photosns-ac77b.appspot.com/").child("profile").child(user.getProfileimagename());
                            reference5.delete();
                            uploadImage();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }



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