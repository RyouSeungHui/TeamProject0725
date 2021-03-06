package com.example.photosnsproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
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

    ArrayList<PostItem> item=new ArrayList<>();
    ArrayList<String> user_id=new ArrayList<>();
    ArrayList<String> post_id=new ArrayList<>();


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

        db.child("Users").child(postuser.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                holder.user_name.setText(user.getId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.user_name.setText(postuser.get(position));

        holder.user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment profileFragment = new ProfileFragment();
                Bundle bundle = new Bundle(1);
                bundle.putString("id",postuser.get(position));
                profileFragment.setArguments(bundle);
                ((MainActivity)context).follow(profileFragment);
            }
        });


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
            holder.timeline.setText(""+diff/31536000+"??? ???");
        }

        else if(diff>2678400) {
            holder.timeline.setText(""+diff/2678400+"??? ???");
        }

        else if(diff>604800) {
            holder.timeline.setText(""+diff/604800+"??? ???");
        }

        else if(diff>86400) {
            holder.timeline.setText(""+diff/86400+"??? ???");
        }

        else if(diff>3600) {
            holder.timeline.setText(""+diff/3600+"?????? ???");
        }

        else if(diff>60) {
            holder.timeline.setText(""+diff/60+"??? ???");
        }

        else {
            holder.timeline.setText(""+diff+"??? ???");
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



        holder.post_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostInfoFragment postInfoFragment = new PostInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user_id",postuser.get(position));
                bundle.putString("post_id",postname.get(position));
                postInfoFragment.setArguments(bundle);
                ((MainActivity)context).follow(postInfoFragment);

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

        holder.user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment profileFragment = new ProfileFragment();
                Bundle bundle = new Bundle(1);
                bundle.putString("id",postuser.get(position));
                profileFragment.setArguments(bundle);
                ((MainActivity)context).follow(profileFragment);
            }
        });

        try {
            for (int i = 0; i < posting.get(position).getTag().size(); i++) {

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = 30;

                TextView tv_tag = new TextView(context);
                tv_tag.setTextColor(Color.parseColor("#00093C"));
                tv_tag.setTextSize(14);
                tv_tag.setText("# " + posting.get(position).getTag().get(i));
                tv_tag.setLayoutParams(layoutParams);
                tv_tag.setTypeface(null, Typeface.BOLD);

                tv_tag.setClickable(true);

                String tag = posting.get(position).getTag().get(i);
                tv_tag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
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

                                                    for(int k=0;k<arrTag_Adapter.size();k++) {
                                                        if (arrTag_Adapter.get(k).equals(tag)) {
                                                            adding(postitem_Adapter,strId_Adapter,sn2.getKey());
                                                            break;
                                                        }
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
                                transport();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
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
                tv_tag.setTextColor(Color.parseColor("#00093C"));
                tv_tag.setTextSize(14);

                tv_tag.setLayoutParams(layoutParams);
                tv_tag.setTypeface(null, Typeface.BOLD);
                tv_tag.setClickable(true);



                String user_id = posting.get(position).getFriend().get(i);

                db.child("Users").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        String user_name=user.getNick();

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



                holder.ll_friend.addView(tv_tag);
            }
        } catch(NullPointerException e) {

        }
        GradientDrawable drawable = (GradientDrawable) context.getDrawable(R.drawable.imageview_round);
        holder.post_img.setBackground(drawable);
        holder.post_img.setClipToOutline(true);

        db.child("Users").child(postuser.get(position)).child("post").child(postname.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("like")){ //????????? ?????????
                    long likeNum = snapshot.child("like").getChildrenCount();
                    holder.like.setText("??? "+likeNum);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

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

        TextView like;


        public postviewholder(@NonNull View itemView) {
            super(itemView);
            this.user_img = itemView.findViewById(R.id.fr_user_img);
            this.user_name = itemView.findViewById(R.id.fr_user_name);
            this.post_img = itemView.findViewById(R.id.fr_post_img);
            this.timeline = itemView.findViewById(R.id.timeline);
            this.ll_tag=itemView.findViewById(R.id.ll_tag);
            this.mention=itemView.findViewById(R.id.mention);
            this.ll_friend=itemView.findViewById(R.id.ll_friend);
            this.like=itemView.findViewById(R.id.like);

            GradientDrawable drawable = (GradientDrawable) context.getDrawable(R.drawable.imageview_round);
            post_img.setBackground(drawable);
            post_img.setClipToOutline(true);
        }
    }

    public void transport() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
        TagPostingFragment tagPostingFragment = new TagPostingFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("user_id",user_id);
        bundle.putStringArrayList("post_id",post_id);
        tagPostingFragment.setArguments(bundle);
        ((MainActivity)context).follow(tagPostingFragment);
    }

    public void adding(PostItem postitem_Adapter,String strId_Adapter,String postkey) {
        item.add(postitem_Adapter);
        user_id.add(strId_Adapter);
        post_id.add(postkey);
    }
}