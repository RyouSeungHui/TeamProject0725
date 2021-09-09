package com.example.photosnsproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import okhttp3.internal.cache.DiskLruCache;

public class RandomFragment extends Fragment implements View.OnClickListener{
    private static final int RESULT_OK = -1;
    private View view;
    private Context context;
    private TextView search_tag;
    private RandomFragment randomFragment;
    private String user;


    private ArrayList<PostItem> posting;
    private ArrayList<String> postname;
    private ArrayList<String> postuser;
    private ArrayList<Integer> postlike;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference db;

    private TagPostingFragment TagPostingFragment;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        context = getActivity();
        view = inflater.inflate(R.layout.activity_random_fragment, container, false);

        init();

        return view;
    }

    private void init() {

        fragmentManager = getActivity().getSupportFragmentManager();
        posting = new ArrayList<>();
        postname = new ArrayList<>();
        postuser = new ArrayList<>();
        postlike = new ArrayList<>();
        search_tag=(view).findViewById(R.id.search_tag);
        TagPostingFragment = new TagPostingFragment();
        database= FirebaseDatabase.getInstance();
        db=database.getReference();
        randomFragment = new RandomFragment();


        recyclerView = (view).findViewById(R.id.random_rcv);
        search_tag = (view).findViewById(R.id.search_tag);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PostingAdapter(posting,context,postname,postuser);
        recyclerView.setAdapter(adapter);

        search_tag.setOnClickListener(this);

        user=NowUser.id;

        db.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot sn : snapshot.getChildren())
                {
                    User user=sn.getValue(User.class);
                    String strId = user.getId();
                    db.child("Users").child(strId).child("post").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            posting.clear();
                            postname.clear();
                            postuser.clear();

                            for(DataSnapshot itemsn : snapshot.getChildren()) {
                                PostItem item = itemsn.getValue(PostItem.class);
                                String postId= itemsn.getKey();
                                try{
                                    db.child("Users").child(strId).child("post").child(postId).child("like").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int cnt=0;

                                            for (DataSnapshot likesn : snapshot.getChildren()) {
                                                if(likesn !=null){
                                                    cnt++;
                                                }
                                            }
                                            postuser.add(strId);
                                            posting.add(item);
                                            postname.add(postId);
                                            postlike.add(cnt);
                                            sort();
                                            adapter.notifyDataSetChanged();
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                } catch (NullPointerException e){

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


    }

    public void sort() {
        PostItem item_tempt;
        String user_tempt;
        String name_tempt;
        Integer like_tempt;

        for(int i=0; i<postuser.size();i++) {
            for(int j=0;j<postuser.size()-1-i;j++) {
                if (postlike.get(j).compareTo(postlike.get(j + 1)) < 0) {
                    item_tempt = posting.get(j);
                    user_tempt = postuser.get(j);
                    name_tempt = postname.get(j);
                    like_tempt = postlike.get(j);

                    posting.set(j, posting.get(j + 1));
                    postuser.set(j, postuser.get(j + 1));
                    postname.set(j, postname.get(j + 1));
                    postlike.set(j, postlike.get(j+1));

                    posting.set(j + 1, item_tempt);
                    postuser.set(j + 1, user_tempt);
                    postname.set(j + 1, name_tempt);
                    postlike.set(j+1, like_tempt);

                }
            }
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_tag:
                Intent intent = new Intent(getActivity(), SearchTag.class);
                startActivityForResult(intent, 2);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2){
            if(resultCode==RESULT_OK){
                ArrayList<String> user_id= data.getStringArrayListExtra("user_id");
                ArrayList<String> post_id= data.getStringArrayListExtra("post_id");
                Bundle bundle = new Bundle(2);
                bundle.putStringArrayList("user_id", user_id);
                bundle.putStringArrayList("post_id", post_id);
                TagPostingFragment.setArguments(bundle);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flFragment, TagPostingFragment).commitAllowingStateLoss();
            }
        }
    }

}