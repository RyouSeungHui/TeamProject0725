package com.example.photosnsproject;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.photosnsproject.Notifications.APIService;
import com.example.photosnsproject.Notifications.Client;
import com.example.photosnsproject.Notifications.MyResponse;
import com.example.photosnsproject.Notifications.NotificationData;
import com.example.photosnsproject.Notifications.SendData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostInfoFragment extends Fragment {
    private View view;
    private Context context;

    private String user_id;  //작성자
    private String post_id;  //데이터베이상 postItem key

    private ImageView user_img;  //프로필사진
    private ImageView post_img;  //게시물사진
    private TextView user_name;  //게시자 이름

    private ImageView heart;    //좋아요버튼
    private TextView tvLike;
    private boolean like = false;
    private long likeNum;
    private ImageView comment;  //댓글버튼

    private LinearLayout ll_hash; //해쉬태그
    private LinearLayout ll_tag;  //친구 tag
    private TextView post_comment; //postitem에서 한마디

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();
    private StorageReference ref;

    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference db=database.getReference();

    private ArrayList<String> userlistfortag;
    private ArrayList<String> postlistfortag;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        context = getActivity();
        view = inflater.inflate(R.layout.postinfofragment, container, false);

        init();

        ImageLoad();

        CallHashtag();

        SetOnClick();  //승희 파트

        return view;
    }

    public void init() {
        user_id=getArguments().getString("user_id");
        post_id=getArguments().getString("post_id");

        user_img = (view).findViewById(R.id.user_img);
        post_img = (view).findViewById(R.id.post_img);
        user_name = (view).findViewById(R.id.user_name);

        heart = (view).findViewById(R.id.post_heart);
        tvLike = (view).findViewById(R.id.tvLike);
        comment = (view).findViewById(R.id.post_comment);

        ll_hash = (view).findViewById(R.id.ll_tag);
        ll_tag = (view).findViewById(R.id.ll_friend);
        post_comment = (view).findViewById(R.id.mention);

        userlistfortag=new ArrayList<>();
        postlistfortag=new ArrayList<>();

    }
    public void ImageLoad() {
        user_name.setText(user_id);
        ref=storageReference.child("profile/"+user_id+".png");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(user_img)
                        .load(uri)
                        .into(user_img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        ref=storageReference.child(user_id+"/"+post_id);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(post_img)
                        .load(uri)
                        .into(post_img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void CallHashtag() {
        db.child("Users").child(user_id).child("post").child(post_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostItem postitem = snapshot.getValue(PostItem.class);

                post_comment.setText(postitem.getContents());
                ArrayList<String> taglist = postitem.getTag();
                ArrayList<String> friendlist = postitem.getFriend();

                try {
                    for (int i = 0; i < taglist.size(); i++) {
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.leftMargin = 30;

                        TextView tv_tag = new TextView(context);
                        tv_tag.setTextColor(Color.parseColor("#476600"));
                        tv_tag.setTextSize(14);
                        tv_tag.setText("# " + taglist.get(i));
                        tv_tag.setLayoutParams(layoutParams);
                        tv_tag.setTypeface(null, Typeface.BOLD);

                        tv_tag.setClickable(true);

                        String tag = taglist.get(i);
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

                                                            for (int k = 0; k < arrTag_Adapter.size(); k++) {
                                                                if (arrTag_Adapter.get(k).equals(tag)) {
                                                                    adding(postitem_Adapter, strId_Adapter, sn2.getKey());
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
                        ll_hash.addView(tv_tag);
                    }
                }catch (NullPointerException e){

                }


                try {
                    for (int i = 0; i < friendlist.size(); i++) {
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.leftMargin = 30;

                        TextView tv_tag = new TextView(context);
                        tv_tag.setTextColor(Color.parseColor("#476600"));
                        tv_tag.setTextSize(14);

                        tv_tag.setLayoutParams(layoutParams);
                        tv_tag.setTypeface(null, Typeface.BOLD);
                        tv_tag.setClickable(true);


                        String friend_id = friendlist.get(i);

                        db.child("Users").child(friend_id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = snapshot.getValue(User.class);
                                String friend_name = user.getNick();

                                tv_tag.setText("@ " + friend_name);
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
                                bundle.putString("id", friend_id);
                                profileFragment.setArguments(bundle);
                                ((MainActivity) context).follow(profileFragment);
                            }
                        });


                        ll_tag.addView(tv_tag);
                    }
                }catch (NullPointerException e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void SetOnClick() {
        String myID = PreferenceManager.getUserId(context);
        db.child("Users").child(user_id).child("post").child(post_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("like")){ //좋아요 있을때
                    if (snapshot.child("like").hasChild(myID)) {
                        heart.setImageResource(R.drawable.full_favorite);
                        like = true;
                    }
                    likeNum = snapshot.child("like").getChildrenCount();
                    tvLike.setText(likeNum+"명이 이 글을 좋아합니다");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //좋아요 버튼 click
        heart.setOnClickListener(v -> {


            if(like){ //현재 좋아요
                db.child("Users").child(user_id).child("post").child(post_id).child("like").child(myID).removeValue();
                heart.setImageResource(R.drawable.empty_favorite);
                likeNum--;
                if(likeNum==0) tvLike.setText("좋아요를 눌러주세요!");
                else tvLike.setText(likeNum+"명이 이 글을 좋아합니다");
                like=false;
            }
            else{ //현재 안좋아요
                db.child("Users").child(user_id).child("post").child(post_id).child("like").child(myID).setValue(getCurrentTime());
                heart.setImageResource(R.drawable.full_favorite);
                likeNum++;
                tvLike.setText(likeNum+"명이 이 글을 좋아합니다");
                like=true;

                db.child("Users").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {

                    String send_id = PreferenceManager.getUserId(view.getContext());

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final User item=snapshot.getValue(User.class);

                        Runnable runnable=new Runnable() {
                            @Override
                            public void run() {

                                APIService apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
                                apiService.sendNotification(new NotificationData(new SendData(post_id,user_id,send_id,"2"),item.getToken()))
                                        .enqueue(new Callback<MyResponse>() {
                                            @Override
                                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                                if(response.code()==200){
                                                    if(response.body().success==1){
                                                        Log.e("Notification", "success");
                                                    }
                                                }

                                            }

                                            @Override
                                            public void onFailure(Call<MyResponse> call, Throwable t) {

                                            }
                                        });

                            }
                        };
                        Thread tr = new Thread(runnable);
                        tr.start();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        tvLike.setOnClickListener(v -> {
            ArrayList<String> likePeople= new ArrayList<String>();
            if(likeNum>0){
                db.child("Users").child(user_id).child("post").child(post_id).child("like").orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()) likePeople.add(ds.getKey());
                        LikeList likeList = new LikeList(likePeople);
                        likeList.show(getActivity().getSupportFragmentManager(), likeList.getTag());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        //댓글 버튼 click
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentFragment commentFragment = new CommentFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user_id",user_id);
                bundle.putString("post_id",post_id);
                commentFragment.setArguments(bundle);
                ((MainActivity)context).follow(commentFragment);
            }
        });
    }

    private String getCurrentTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

    public void transport() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
        TagPostingFragment tagPostingFragment = new TagPostingFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("user_id",userlistfortag);
        bundle.putStringArrayList("post_id",postlistfortag);
        tagPostingFragment.setArguments(bundle);
        ((MainActivity)context).follow(tagPostingFragment);
    }

    public void adding(PostItem postitem_Adapter,String strId_Adapter,String postkey) {
        userlistfortag.add(strId_Adapter);
        postlistfortag.add(postkey);
    }
}
