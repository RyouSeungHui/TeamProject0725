package com.example.photosnsproject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photosnsproject.Notifications.APIService;
import com.example.photosnsproject.Notifications.Client;
import com.example.photosnsproject.Notifications.MyResponse;
import com.example.photosnsproject.Notifications.NotificationData;
import com.example.photosnsproject.Notifications.SendData;
import com.example.photosnsproject.Notifications.Token;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentFragment extends Fragment {

    private View view;
    private Context context;

    private ArrayList<Comment> list;

    private String user_id;
    private String post_id;

    private EditText ed_send;
    private ImageView btn_send;
    private ImageView btn_back;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference db=database.getReference();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        context = getActivity();
        view = inflater.inflate(R.layout.commentfragment, container, false);

        init();

        SetOnclick();

        Adapting();

        db.child("Users").child(user_id).child("post").child(post_id).child("Comment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot sn : snapshot.getChildren()) {
                    Comment comment = sn.getValue(Comment.class);
                    list.add(comment);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    public void init() {
        user_id=getArguments().getString("user_id");
        post_id=getArguments().getString("post_id");

        ed_send=(view).findViewById(R.id.msg);
        btn_send=(view).findViewById(R.id.btn_send);
        btn_back=(view).findViewById(R.id.btn_back);

        list=new ArrayList<>();

        recyclerView=(view).findViewById(R.id.cmt_rcy);
    }

    public void SetOnclick() {
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=ed_send.getText().toString();
                String send_id = PreferenceManager.getUserId(view.getContext());

                Comment newcmt = new Comment(send_id,text);

                db.child("Users").child(user_id).child("post").child(post_id).child("Comment").push().setValue(newcmt);

                //댓글 시 알림

                db.child("Users").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final User item=snapshot.getValue(User.class);

                        Runnable runnable=new Runnable() {
                            @Override
                            public void run() {

                                APIService apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
                                apiService.sendNotification(new NotificationData(new SendData(post_id,user_id,send_id),item.getToken()))
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

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostInfoFragment postInfoFragment = new PostInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user_id",user_id);
                bundle.putString("post_id",post_id);
                postInfoFragment.setArguments(bundle);
                ((MainActivity)context).follow(postInfoFragment);
            }
        });

    }

    public void Adapting() {
        adapter=new CommentAdapter(context,list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
