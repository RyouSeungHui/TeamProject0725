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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsFragment extends Fragment implements View.OnClickListener {
    private static final int RESULT_OK = -1;
    private View view;
    private Context context;
    private TextView search_nick;
    private Button notification_btn;
    private FriendsFragment friendsFragment;
    private String user;

    private ArrayList<PostItem> posting;
    private ArrayList<String> postname;
    private ArrayList<String> postuser;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference db;

    private ProfileFragment profileFragment;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        context = getActivity();
        view = inflater.inflate(R.layout.activity_friends_fragment, container, false);

        init();

        return view;
    }

    private void init() {

        fragmentManager = getActivity().getSupportFragmentManager();
        posting = new ArrayList<>();
        postname = new ArrayList<>();
        postuser = new ArrayList<>();
        profileFragment = new ProfileFragment();
        database=FirebaseDatabase.getInstance();
        db=database.getReference();
        friendsFragment = new FriendsFragment();

        recyclerView = (view).findViewById(R.id.fr_rcv);
        search_nick = (view).findViewById(R.id.search_nick);
        notification_btn=(view).findViewById(R.id.notification_btn);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PostingAdapter(posting,context,postname,postuser);
        recyclerView.setAdapter(adapter);

        search_nick.setOnClickListener(this);
        notification_btn.setOnClickListener(this);

        user=NowUser.id;

        db.child("Follow").child("Following").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                posting.clear();
                postname.clear();
                postuser.clear();
                for(DataSnapshot sn : snapshot.getChildren())
                {
                    String followname = sn.getValue(String.class);
                    db.child("Users").child(followname).child("post").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot itemsn : snapshot.getChildren()) {
                                PostItem item = itemsn.getValue(PostItem.class);
                                postuser.add(followname);
                                posting.add(item);
                                postname.add(itemsn.getKey());
                                sort();
                                adapter.notifyDataSetChanged();
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

        for(int i=0; i<postuser.size();i++) {
            for(int j=0;j<postuser.size()-1-i;j++) {
                if (posting.get(j).getGetTime().compareTo(posting.get(j + 1).getGetTime()) < 0) {
                    item_tempt = posting.get(j);
                    user_tempt = postuser.get(j);
                    name_tempt = postname.get(j);

                    posting.set(j, posting.get(j + 1));
                    postuser.set(j, postuser.get(j + 1));
                    postname.set(j, postname.get(j + 1));

                    posting.set(j + 1, item_tempt);
                    postuser.set(j + 1, user_tempt);
                    postname.set(j + 1, name_tempt);

                }
            }
        }

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_nick:
                Intent intent = new Intent(getActivity(), Search.class);
                startActivityForResult(intent,1);
                break;

            case R.id.notification_btn:
                NotificationFragment notificationFragment = new NotificationFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user_id",user);
                notificationFragment.setArguments(bundle);
                ((MainActivity)context).follow(notificationFragment);
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            if(resultCode==RESULT_OK){
                String id=data.getExtras().getString("id");
                Bundle bundle = new Bundle(1);
                bundle.putString("id",id);
                profileFragment.setArguments(bundle);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flFragment, profileFragment).commitAllowingStateLoss();
            }
        }


    }
}