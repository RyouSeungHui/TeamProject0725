package com.example.photosnsproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private ArrayList<String> imagelist;
    private Context context;




    public GalleryAdapter(ArrayList<String> imagelist, Context context){
        this.imagelist=imagelist;
        this.context=context;
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
        Glide.with(holder.itemView.getContext()).load(imagelist.get(position)).into(holder.mg_image);
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
