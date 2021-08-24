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

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private ArrayList<String> imagelist;
    private Context context;
    private String name;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference submitProfile;



    public GalleryAdapter(ArrayList<String> imagelist, Context context,String name){
        this.imagelist=imagelist;
        this.context=context;
        this.name=name;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_profile,parent,false);
       GalleryAdapter.GalleryViewHolder holder = new GalleryAdapter.GalleryViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        //Glide.with(holder.itemView.getContext()).load(imagelist.get(position)).into(holder.mg_image);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        String user_path = name+"/"+imagelist.get(position);
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
                bundle.putString("user_id",name);
                bundle.putString("post_id",imagelist.get(position));
                postInfoFragment.setArguments(bundle);
                ((MainActivity)context).follow(postInfoFragment);
            }
        });
    }


    @Override
    public int getItemCount() {

        return (imagelist !=null ? imagelist.size() : 0);
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder {
        ImageView mg_image;
        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mg_image=itemView.findViewById(R.id.mg_image);
        }
    }
}
