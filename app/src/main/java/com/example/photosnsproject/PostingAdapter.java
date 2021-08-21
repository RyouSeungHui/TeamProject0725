package com.example.photosnsproject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

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


        Calendar posttime = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            posttime.setTime(format.parse(posting.get(position).getGetTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar current = Calendar.getInstance();

        long diff = (current.getTimeInMillis() - posttime.getTimeInMillis())/1000;

        if(diff>31536000) {
            holder.timeline.setText(""+diff/31536000+"년 전");
        }

        else if(diff>2678400) {
            holder.timeline.setText(""+diff/2678400+"달 전");
        }

        else if(diff>604800) {
            holder.timeline.setText(""+diff/604800+"주 전");
        }

        else if(diff>86400) {
            holder.timeline.setText(""+diff/86400+"일 전");
        }

        else if(diff>3600) {
            holder.timeline.setText(""+diff/3600+"시간 전");
        }

        else if(diff>60) {
            holder.timeline.setText(""+diff/60+"분 전");
        }

        else {
            holder.timeline.setText(""+diff+"초 전");
        }

        holder.mention.setText(posting.get(position).getContents());

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        String post_path = postuser.get(position)+"/"+postname.get(position);
        submitProfile = storageReference.child(post_path);
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

        String user_path = "profile/"+postuser.get(position)+".png";
        submitProfile = storageReference.child(user_path);
        submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(holder.user_img)
                        .load(uri)
                        .into(holder.user_img);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        try {
            for (int i = 0; i < posting.get(position).getTag().size(); i++) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = 30;

                TextView tv_tag = new TextView(context);
                tv_tag.setTextColor(Color.parseColor("#476600"));
                tv_tag.setTextSize(14);
                tv_tag.setText("# " + posting.get(position).getTag().get(i));
                tv_tag.setLayoutParams(layoutParams);
                tv_tag.setTypeface(null, Typeface.BOLD);

                holder.ll_tag.addView(tv_tag);
            }
        } catch(NullPointerException e) {

        }

        try {
            for (int i = 0; i < posting.get(position).getFriend().size(); i++) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = 30;

                TextView tv_tag = new TextView(context);
                tv_tag.setTextColor(Color.parseColor("#476600"));
                tv_tag.setTextSize(14);
                tv_tag.setText("@ " + posting.get(position).getFriend().get(i));
                tv_tag.setLayoutParams(layoutParams);
                tv_tag.setTypeface(null, Typeface.BOLD);
                tv_tag.setClickable(true);


                holder.ll_friend.addView(tv_tag);
            }
        } catch(NullPointerException e) {

        }

    }

    @Override
    public int getItemCount()  {
        return (posting !=null ? posting.size() : 0);
    }

    public class postviewholder extends RecyclerView.ViewHolder{
        CircleImageView user_img;
        TextView user_name;
        ImageView post_img;
        TextView timeline;

        LinearLayout ll_tag;
        TextView mention;
        LinearLayout ll_friend;

        public postviewholder(@NonNull View itemView) {
            super(itemView);
            this.user_img = itemView.findViewById(R.id.fr_user_img);
            this.user_name = itemView.findViewById(R.id.fr_user_name);
            this.post_img = itemView.findViewById(R.id.fr_post_img);
            this.timeline = itemView.findViewById(R.id.timeline);
            this.ll_tag=itemView.findViewById(R.id.ll_tag);
            this.mention=itemView.findViewById(R.id.mention);
            this.ll_friend=itemView.findViewById(R.id.ll_friend);
        }
    }
}