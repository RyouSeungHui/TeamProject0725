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

public class MappathAdapter extends RecyclerView.Adapter<MappathAdapter.pathHolder>{

    ArrayList<String> path;
    Context context;

    public MappathAdapter(ArrayList<String> path, Context context) {
        this.path = path;
        this.context = context;
    }

    @NonNull
    @Override
    public pathHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.mappath,parent,false);
        MappathAdapter.pathHolder holder = new MappathAdapter.pathHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull pathHolder holder, int position) {
        holder.tv_path.setText(path.get(position));

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference submitProfile = storageReference.child(path.get(position));
        submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(MapFragment.newInstance())
                        .load(uri)
                        .into(holder.map_img);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.tv_path.setText(""+e);
            }
        });
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    @Override
    public int getItemCount() {
        return (path !=null ? path.size() : 0);
    }

    public class pathHolder extends RecyclerView.ViewHolder{
        TextView tv_path;
        ImageView map_img;
        public pathHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_path=itemView.findViewById(R.id.distance);
            this.map_img=itemView.findViewById(R.id.img);
        }
    }
}
