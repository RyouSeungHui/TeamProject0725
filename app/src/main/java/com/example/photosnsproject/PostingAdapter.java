package com.example.photosnsproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PostingAdapter extends RecyclerView.Adapter<PostingAdapter.postviewholder>{

    ArrayList<PostItem> posting;
    Context context;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference submitProfile;


    public PostingAdapter(ArrayList<PostItem> posting, Context context) {
        this.posting = posting;
        this.context = context;
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

    }

    @Override
    public int getItemCount()  {
        return (posting !=null ? posting.size() : 0);
    }

    public class postviewholder extends RecyclerView.ViewHolder{
        ImageView user_img;
        TextView user_name;
        ImageView post_img;
        public postviewholder(@NonNull View itemView) {
            super(itemView);
            this.user_img = itemView.findViewById(R.id.fr_user_img);
            this.user_name = itemView.findViewById(R.id.fr_user_name);
            this.post_img = itemView.findViewById(R.id.fr_post_img);
        }
    }
}
