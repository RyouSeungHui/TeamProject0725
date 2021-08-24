package com.example.photosnsproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PostInfoFragment extends Fragment {
    private View view;
    private Context context;

    private String user_id;  //작성자
    private String post_id;  //데이터베이상 postItem key

    private ImageView user_img;
    private ImageView post_img;
    private TextView user_name;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();
    private StorageReference ref;

    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference db=database.getReference();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        context = getActivity();
        view = inflater.inflate(R.layout.postinfofragment, container, false);


        init();


        return view;
    }

    public void init() {
        user_id=getArguments().getString("user_id");
        post_id=getArguments().getString("post_id");

        user_img = (view).findViewById(R.id.user_img);
        post_img = (view).findViewById(R.id.post_img);
        user_name = (view).findViewById(R.id.user_name);

        user_name.setText(user_id);
        ref=storageReference.child("profile/"+user_id+".png");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(user_img)
                        .load(uri)
                        .into(user_img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        ref=storageReference.child(user_id+"/"+post_id);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(post_img)
                        .load(uri)
                        .into(post_img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }
























}
