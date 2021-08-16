package com.example.photosnsproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.FollowViewHolder>{

    private ArrayList<String> nicklist;
    private ArrayList<String> idlist;
    private Context context;
    private FirebaseStorage storage=FirebaseStorage.getInstance();
    private StorageReference storageReference=storage.getReference();

    public FollowAdapter( ArrayList<String> nicklist, ArrayList<String> idlist, Context context){

        this.nicklist=nicklist;
        this.idlist=idlist;
        this.context=context;
    }

    @NonNull
    @Override
    public FollowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_profile,parent,false);
        FollowAdapter.FollowViewHolder holder = new FollowAdapter.FollowViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FollowViewHolder holder, int position) {
        holder.followlist_nick.setText(nicklist.get(position));
        holder.followlist_id.setText(idlist.get(position));

        StorageReference loadreference=storageReference.child("profile").child(idlist.get(position)+".png");

        loadreference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.followlist_profile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.followlist_profile.setImageResource(R.mipmap.ic_launcher);
            }
        });
        /*
        holder.fp_linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("id", list.get(position).getId());
                ((Activity)context).setResult(Activity.RESULT_OK,intent); //결과설정
                ((Activity)context).finish();

            }
        });*/
    }

    @Override
    public int getItemCount() {

        return (nicklist !=null ? nicklist.size() : 0);

    }


    public class FollowViewHolder extends RecyclerView.ViewHolder {
        TextView followlist_nick;
        TextView followlist_id;
        CircleImageView followlist_profile;
        LinearLayout fp_linear1;
        public FollowViewHolder(@NonNull View itemView) {
            super(itemView);
            this.followlist_nick=itemView.findViewById(R.id.followlist_nick);
            this.fp_linear1=itemView.findViewById(R.id.fp_linear1);
            this.followlist_id=itemView.findViewById(R.id.followlist_id);
            this.followlist_profile=itemView.findViewById(R.id.followlist_profile);
        }
    }
}
