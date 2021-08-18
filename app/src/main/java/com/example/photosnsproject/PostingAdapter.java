package com.example.photosnsproject;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PostingAdapter extends RecyclerView.Adapter<PostingAdapter.postviewholder>{

    ArrayList<PostItem> posting;
    ArrayList<String> postname;
    ArrayList<String> postuser;
    Context context;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference submitProfile;


    public PostingAdapter(ArrayList<PostItem> posting, Context context,ArrayList<String> postname,ArrayList<String> postuser) {
        this.posting = posting;
        this.context = context;
        this.postname=postname;
        this.postuser=postuser;
    }


    @NonNull
    @Override
    public PostingAdapter.postviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.postframe,parent,false);
        PostingAdapter.postviewholder holder = new PostingAdapter.postviewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostingAdapter.postviewholder holder, int position) {
        holder.user_name.setText(postuser.get(position));

        holder.timeline.setText(posting.get(position).getGetTime());

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        String path = postuser.get(position)+"/"+postname.get(position);
        submitProfile = storageReference.child(path);
        submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(holder.post_img)
                        .load(uri)
                        .into(holder.post_img);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }

    @Override
    public int getItemCount()  {
        return (posting !=null ? posting.size() : 0);
    }

    public class postviewholder extends RecyclerView.ViewHolder{
        ImageView user_img;
        TextView user_name;
        ImageView post_img;
        TextView timeline;
        public postviewholder(@NonNull View itemView) {
            super(itemView);
            this.user_img = itemView.findViewById(R.id.fr_user_img);
            this.user_name = itemView.findViewById(R.id.fr_user_name);
            this.post_img = itemView.findViewById(R.id.fr_post_img);
            this.timeline = itemView.findViewById(R.id.timeline);
        }
    }
}
