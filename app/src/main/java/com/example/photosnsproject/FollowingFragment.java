package com.example.photosnsproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FollowingFragment extends Fragment {

    private  int index;
    private View view;
    private Context context;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference db = database.getReference();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> list;
    private ArrayList<String> nicklist;
    private ArrayList<String> idlist;

    private ProfileFragment profileFragment;

    private String userID;
    /*
    public static FollowingFragment newInstance() {
        return new FollowingFragment();
    }*/


    public FollowingFragment(String id, int index) {

        this.userID=id;
        this.index=index;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_follow_fragment, container, false);
        Init();
        return view;
    }

    private void Init(){
        context = getActivity();
        recyclerView = (view).findViewById(R.id.ff_rcy);
        recyclerView.setHasFixedSize(true); //정리한번
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        nicklist = new ArrayList<>();
        idlist = new ArrayList<>();
        adapter = new FollowAdapter(nicklist, idlist, context,index);
        recyclerView.setAdapter(adapter);


        //Singlevalue로 고쳐봐라. 데이터 터짐.
        db.child("Follow").child("Following").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                for (DataSnapshot sn : snapshot.getChildren()) {
                    String id = sn.getValue().toString();
                    list.add(id);

                    db.child("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            User user = snapshot.getValue(User.class);
                            nicklist.add(user.getNick());
                            idlist.add(user.getId());
                            adapter.notifyDataSetChanged();

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

        nicklist.clear();
        idlist.clear();







    }

}
