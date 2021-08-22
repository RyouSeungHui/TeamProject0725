package com.example.photosnsproject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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


    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference db=database.getReference();

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

                tv_tag.setClickable(true);

                String tag = posting.get(position).getTag().get(i);

                tv_tag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ArrayList<PostItem> item;
                        ArrayList<String> user_id;
                        ArrayList<String> post_id;

                        item = new ArrayList<>();
                        user_id = new ArrayList<>();
                        post_id = new ArrayList<>();

                        db.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                item.clear();
                                user_id.clear();
                                post_id.clear();
                                for (DataSnapshot sn : snapshot.getChildren()) {
                                    User user_Adapter = sn.getValue(User.class);
                                    String strId_Adapter = user_Adapter.getId();
                                    db.child("Users").child(strId_Adapter).child("post").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot sn2 : snapshot.getChildren()) {
                                                PostItem postitem_Adapter = sn2.getValue(PostItem.class);

                                                try {
                                                    ArrayList<String> arrTag_Adapter = postitem_Adapter.getTag();

                                                    if (arrTag_Adapter.equals(tag)) {
                                                        item.add(postitem_Adapter);
                                                        user_id.add(strId_Adapter);
                                                        post_id.add(sn2.getKey());
                                                    }
                                                } catch (NullPointerException e) {

                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        TagPostingFragment tagPostingFragment = new TagPostingFragment();
                        Bundle bundle = new Bundle();
                       // bundle.putParcelableArrayList("postitem",(ArrayList<? extends Parcelable>) item);
                        bundle.putStringArrayList("user_id",user_id);
                        bundle.putStringArrayList("post_id",post_id);
                        tagPostingFragment.setArguments(bundle);
                        ((MainActivity)context).follow(tagPostingFragment);

                    }
                });

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

                tv_tag.setLayoutParams(layoutParams);
                tv_tag.setTypeface(null, Typeface.BOLD);
                tv_tag.setClickable(true);

                // 아래 사항 조치후 삭제할 line
                tv_tag.setText("@ " + posting.get(position).getFriend().get(i));


                /*   <- PostItem - Friends ArrayList 이름을 id로 수정한 후 적용
                String user_id = posting.get(position).getFriend().get(i);

                db.child("Users").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        String user_name=user.getId();

                        tv_tag.setText("@ " + user_name);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                tv_tag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProfileFragment profileFragment = new ProfileFragment();
                        Bundle bundle = new Bundle(1);
                        bundle.putString("id",user_id);
                        profileFragment.setArguments(bundle);
                        ((MainActivity)context).follow(profileFragment);
                    }
                });

                */

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