package com.example.photosnsproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


import de.hdodenhof.circleimageview.CircleImageView;

public class    ProfileFragment extends Fragment implements View.OnClickListener {


    private View view;
    private Context context;
    private static final int IMAGE_REQUEST = 1;
    private Uri filePath;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference db = database.getReference();
    private FirebaseStorage storage=FirebaseStorage.getInstance();
    private StorageReference storageReference=storage.getReference();
    String string_pf_id;
    private LinearLayout pf_gallery;
    private LinearLayout pf_follower;
    private LinearLayout pf_following;
    private TextView pf_id;
    private TextView num_gallery;
    private TextView num_follower;
    private TextView num_following;
    private Button pf_follow_btn;
    CircleImageView profile;
    private ArrayList<String> imagelist;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String userID;
    private int flag;
    private FollowerFragment followerFragment;
    private FollowingFragment followingFragment;



    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.activity_profile_fragment, container, false);
        profile=view.findViewById(R.id.profile);
        pf_id = (TextView) view.findViewById(R.id.pf_id); //2
        string_pf_id = getArguments().getString("id");
        pf_follow_btn=(Button) view.findViewById(R.id.pf_follow_btn);
        pf_gallery=(view).findViewById(R.id.pf_gallery);
        pf_follower=(view).findViewById(R.id.pf_follower);
        pf_following=(view).findViewById(R.id.pf_following);
        num_gallery=view.findViewById(R.id.num_gallery);
        num_follower=view.findViewById(R.id.num_follower);
        num_following=view.findViewById(R.id.num_following);
        pf_follow_btn.setOnClickListener(this);
        pf_gallery.setOnClickListener(this);
        pf_follower.setOnClickListener(this);
        pf_following.setOnClickListener(this);
        pf_id.setText(string_pf_id);
        recyclerView = (view).findViewById(R.id.pf_rcy);
        recyclerView.setHasFixedSize(true); //정리한번
        layoutManager = new GridLayoutManager(view.getContext(),3);
        recyclerView.setLayoutManager(layoutManager);
        imagelist = new ArrayList<>();
        adapter = new GalleryAdapter(imagelist, context,string_pf_id);
        recyclerView.setAdapter(adapter);
        userID =PreferenceManager.getUserId(view.getContext());

        //팔로워
        db.child("Follow").child("Follower").child(string_pf_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int cnt=0;
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    if(snapshot1 !=null){
                        cnt++;
                    }
                }
                num_follower.setText(cnt+"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //팔로우
        db.child("Follow").child("Following").child(string_pf_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int cnt=0;
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    if(snapshot1 !=null){
                        cnt++;
                    }
                }
                num_following.setText(cnt+"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //게시물
        db.child("Users").child(string_pf_id).child("post").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int cnt=0;
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    if(snapshot1 !=null){
                        cnt++;
                    }
                }
                num_gallery.setText(cnt+"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        //프로필
        StorageReference loadreference=storageReference.child("profile").child(string_pf_id+".png");

        //null이 인식이 안되는건가?
        if(loadreference==null){

        }
        else{
            loadreference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getContext()).load(uri).into(profile);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    profile.setImageResource(R.mipmap.ic_launcher);
                }
            });
        }

        //https://www.geeksforgeeks.org/how-to-view-all-the-uploaded-images-in-firebase-storage/
        //자기 프로필 게시물

        db.child("Users").child(string_pf_id).child("post").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot sn : snapshot.getChildren()) {
                    imagelist.add(sn.getKey());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*
        StorageReference mgreference=storageReference.child(string_pf_id);

        mgreference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imagelist.add(uri.toString());
                            adapter.notifyDataSetChanged();
                            Log.e("Itemvalue",uri.toString());
                        }
                    });

                }
            }
        });

         */

        //팔로우 버튼

        db.child("Follow").child("Following").child(userID).child(string_pf_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String item=snapshot.getValue(String.class);
                if(item==null){

                    pf_follow_btn.setBackgroundResource(R.drawable.follow_btn);
                    pf_follow_btn.setTextColor(Color.WHITE);
                    pf_follow_btn.setText("팔로우");

                }
                else{
                    pf_follow_btn.setBackgroundResource(R.drawable.after_follow_btn);
                    pf_follow_btn.setTextColor(Color.BLACK);
                    pf_follow_btn.setText("팔로잉");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }



    //팔로우 버튼




    public void follow_btn_click(){
        db.child("Follow").child("Following").child(userID).child(string_pf_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String item=snapshot.getValue(String.class);
                if(item==null){
                    db.child("Follow").child("Follower").child(string_pf_id).child(userID).setValue(userID);
                    db.child("Follow").child("Following").child(userID).child(string_pf_id).setValue(string_pf_id);
                    pf_follow_btn.setBackgroundResource(R.drawable.after_follow_btn);
                    pf_follow_btn.setTextColor(Color.BLACK);
                    pf_follow_btn.setText("팔로잉");

                }
                else{
                    db.child("Follow").child("Follower").child(string_pf_id).child(userID).removeValue();
                    db.child("Follow").child("Following").child(userID).child(string_pf_id).removeValue();
                    pf_follow_btn.setBackgroundResource(R.drawable.follow_btn);
                    pf_follow_btn.setTextColor(Color.WHITE);
                    pf_follow_btn.setText("팔로우");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.pf_follow_btn :follow_btn_click();
                break;

            case R.id.pf_follower:
                FollowFragment followFragment=new FollowFragment(string_pf_id,0);
                ((MainActivity) context).follow(followFragment);

                break;

            case R.id.pf_following:

                FollowFragment followFragment2=new FollowFragment(string_pf_id,1);
                ((MainActivity) context).follow(followFragment2);

                break;

        }
    }
}