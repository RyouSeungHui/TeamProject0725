package com.example.photosnsproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FollowFragment extends Fragment {

    private View view;
    private Context context;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference db = database.getReference();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> list;
    private ArrayList<String> nick;
    public static String userid;

    public static FollowFragment newInstance() {
        return new FollowFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getActivity();
        view = inflater.inflate(R.layout.activity_follow_fragment, container, false);
        recyclerView = (view).findViewById(R.id.ff_rcy);
        recyclerView.setHasFixedSize(true); //정리한번
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        nick = new ArrayList<>();
        adapter = new FollowAdapter(nick, context);
        recyclerView.setAdapter(adapter);


        //Singlevalue로 고쳐봐라. 데이터 터짐.
        db.child("Follow").child("Follower").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                for (DataSnapshot sn : snapshot.getChildren()) {
                    String id = sn.getValue().toString();
                    list.add(id);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        nick.clear();

        for (int i = 0; i < list.size(); i++) {

            db.child("Users").child(list.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    User user = snapshot.getValue(User.class);
                    nick.add(user.getNick());
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
        return view;
    }
}
