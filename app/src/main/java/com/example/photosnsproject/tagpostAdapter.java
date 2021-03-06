package com.example.photosnsproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class tagpostAdapter extends RecyclerView.Adapter<tagpostAdapter.tagViewHolder>{

    private ArrayList<String> userlist;
    private ArrayList<String> postlist;
    private Context context;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference submitProfile;

    public tagpostAdapter(ArrayList<String> userlist,ArrayList<String> postlist, Context context) {
        this.userlist =userlist;
        this.postlist =postlist;
        this.context = context;
    }

    @NonNull
    @Override
    public tagpostAdapter.tagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_profile,parent,false);
        tagpostAdapter.tagViewHolder holder = new tagpostAdapter.tagViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull tagpostAdapter.tagViewHolder holder, int position) {

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        String user_path = userlist.get(position)+"/"+postlist.get(position);
        submitProfile = storageReference.child(user_path);
        submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(holder.mg_image)
                        .load(uri)
                        .into(holder.mg_image);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
        holder.mg_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostInfoFragment postInfoFragment = new PostInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user_id",userlist.get(position));
                bundle.putString("post_id",postlist.get(position));
                postInfoFragment.setArguments(bundle);
                ((MainActivity)context).follow(postInfoFragment);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (userlist !=null ? userlist.size() : 0);
    }

    public class tagViewHolder extends RecyclerView.ViewHolder {
        ImageView mg_image;
        public tagViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mg_image=itemView.findViewById(R.id.mg_image);
        }
    }
}
