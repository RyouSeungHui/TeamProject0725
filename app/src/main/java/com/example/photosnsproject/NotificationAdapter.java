package com.example.photosnsproject;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private StorageReference storageReference=FirebaseStorage.getInstance().getReference();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference db = database.getReference();
    private ArrayList <Notification_Item> notification_itemArrayList;


    public NotificationAdapter(Context context,ArrayList <Notification_Item> notification_itemArrayList){
        this.context=context;
        this.notification_itemArrayList=notification_itemArrayList;

    }


    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_profile,parent,false);
        NotificationAdapter.NotificationViewHolder holder=new NotificationViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {

        //바텀다이어로그

        //리니어
        holder.notification_linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                switch (notification_itemArrayList.get(position).getType()) {
                    case "1":
                        CommentFragment commentFragment = new CommentFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("user_id",notification_itemArrayList.get(position).getUser_id());
                        bundle.putString("post_id",notification_itemArrayList.get(position).getPost_id());
                        commentFragment.setArguments(bundle);
                        ((MainActivity)context).follow(commentFragment);
                        break;

                    case "2":
                        PostInfoFragment postInfoFragment = new PostInfoFragment();
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("user_id",notification_itemArrayList.get(position).getUser_id());
                        bundle2.putString("post_id",notification_itemArrayList.get(position).getPost_id());
                        postInfoFragment.setArguments(bundle2);
                        ((MainActivity)context).follow(postInfoFragment);
                        break;

                    case "3":
                        PostInfoFragment postInfoFragment3 = new PostInfoFragment();
                        Bundle bundle3 = new Bundle();
                        bundle3.putString("user_id",notification_itemArrayList.get(position).getUser_id());
                        bundle3.putString("post_id",notification_itemArrayList.get(position).getPost_id());
                        postInfoFragment3.setArguments(bundle3);
                        ((MainActivity)context).follow(postInfoFragment3);
                        break;

                }

            }
        });

        holder.notification_linear1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setBottomSheetDialog(v);
                return true;
            }

            private void setBottomSheetDialog(View v) {
                BottomSheetBehavior mBehavior;
                View view;

                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(v.getContext());
                view= LayoutInflater.from(v.getContext()).inflate(R.layout.layout_bottomsheetdialog_notification,(LinearLayout)v.findViewById(R.id.bottomSheetContainer));


                LinearLayout bs1 = view.findViewById(R.id.bs1);


                bottomSheetDialog.setContentView(view);
                mBehavior = BottomSheetBehavior.from((View)view.getParent());
                mBehavior.setPeekHeight(4000);
                bottomSheetDialog.show();

                bs1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.child("Alarm").child(notification_itemArrayList.get(position).getUser_id())
                                .child(notification_itemArrayList.get(position).getAlarm_id()).removeValue();
                        notification_itemArrayList.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                        bottomSheetDialog.dismiss();
                    }
                });






            }
        });


        //프사

        StorageReference profile_ref=storageReference.child("profile").child(notification_itemArrayList.get(position).getSend_id()+".png");
        profile_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.notification_profile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.notification_profile.setImageResource(R.mipmap.ic_launcher);
            }
        });

        holder.notification_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment profileFragment = new ProfileFragment();
                Bundle bundle = new Bundle(1);
                bundle.putString("id",notification_itemArrayList.get(position).getSend_id());
                profileFragment.setArguments(bundle);
                ((MainActivity)context).follow(profileFragment);
            }
        });

        //게시물
        StorageReference post_ref=storageReference.child(notification_itemArrayList.get(position).getUser_id())
                .child(notification_itemArrayList.get(position).getPost_id());

        post_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.notification_post);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        holder.notification_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostInfoFragment postInfoFragment = new PostInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user_id",notification_itemArrayList.get(position).getUser_id());
                bundle.putString("post_id",notification_itemArrayList.get(position).getPost_id());
                postInfoFragment.setArguments(bundle);
                ((MainActivity)context).follow(postInfoFragment);

            }
        });

        //내용
        switch (notification_itemArrayList.get(position).getType()) {
            case "1":
                SpannableStringBuilder sb = new SpannableStringBuilder();
                String str = notification_itemArrayList.get(position).getSend_id() +
                        "님이 댓글을 남겼습니다: " + notification_itemArrayList.get(position).getContent();
                sb.append(str);
                sb.setSpan(new StyleSpan(Typeface.BOLD), 0, notification_itemArrayList.get(position).getSend_id().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.notification_content.setText(sb);
                break;

            case "2":
                SpannableStringBuilder sb2 = new SpannableStringBuilder();
                String str2 = notification_itemArrayList.get(position).getSend_id() +
                        "님이 좋아요를 눌렀습니다.";
                sb2.append(str2);
                sb2.setSpan(new StyleSpan(Typeface.BOLD), 0, notification_itemArrayList.get(position).getSend_id().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.notification_content.setText(sb2);
                break;

            case "3":
                SpannableStringBuilder sb3 = new SpannableStringBuilder();
                String str3 = notification_itemArrayList.get(position).getSend_id() +
                        "님이 회원님을 언급했습니다.";
                sb3.append(str3);
                sb3.setSpan(new StyleSpan(Typeface.BOLD), 0, notification_itemArrayList.get(position).getSend_id().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.notification_content.setText(sb3);
                break;

        }

        //시간
        Calendar posttime = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            posttime.setTime(format.parse(notification_itemArrayList.get(position).getGetTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar current = Calendar.getInstance();

        long diff = (current.getTimeInMillis() - posttime.getTimeInMillis())/1000;

        if(diff>31536000) {
            holder.notification_getTime.setText(""+diff/31536000+"년 전");
        }

        else if(diff>2678400) {
            holder.notification_getTime.setText(""+diff/2678400+"달 전");
        }

        else if(diff>604800) {
            holder.notification_getTime.setText(""+diff/604800+"주 전");
        }

        else if(diff>86400) {
            holder.notification_getTime.setText(""+diff/86400+"일 전");
        }

        else if(diff>3600) {
            holder.notification_getTime.setText(""+diff/3600+"시간 전");
        }

        else if(diff>60) {
            holder.notification_getTime.setText(""+diff/60+"분 전");
        }

        else {
            holder.notification_getTime.setText(""+diff+"초 전");
        }

    }

    @Override
    public int getItemCount() {
        return(notification_itemArrayList !=null ? notification_itemArrayList.size() : 0);
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        LinearLayout notification_linear1;
        CircleImageView notification_profile;
        TextView notification_content,notification_getTime;
        ImageView notification_post;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            this.notification_linear1=itemView.findViewById(R.id.notification_linear1);
            this.notification_profile=itemView.findViewById(R.id.notification_profile);
            this.notification_content=itemView.findViewById(R.id.notification_content);
            this.notification_getTime=itemView.findViewById(R.id.notification_getTime);
            this.notification_post=itemView.findViewById(R.id.notification_post);
        }
    }
}
