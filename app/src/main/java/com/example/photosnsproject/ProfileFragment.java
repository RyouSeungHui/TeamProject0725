package com.example.photosnsproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Context context;
    TextView pf_id;
    String string_pf_id;
    Button pf_follow_btn,pf_follower_btn,pf_following_btn;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference db = database.getReference();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.activity_profile_fragment, container, false);
        pf_id = (TextView) view.findViewById(R.id.pf_id); //2
        string_pf_id = getArguments().getString("id");
        pf_follow_btn=(Button) view.findViewById(R.id.pf_follow_btn);
        pf_follower_btn=(Button) view.findViewById(R.id.pf_follower_btn);
        pf_following_btn=(Button) view.findViewById(R.id.pf_following_btn);
        pf_follow_btn.setOnClickListener(this);
        pf_follower_btn.setOnClickListener(this);
        pf_following_btn.setOnClickListener(this);
        pf_id.setText(string_pf_id);




        return view;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.pf_follow_btn :
                db.child("Follow").child("Follower").child(string_pf_id).push().setValue(NowUser.id);
                db.child("Follow").child("Following").child(NowUser.id).push().setValue(string_pf_id);
                break;

            case R.id.pf_follower_btn:
                FollowFragment.userid=string_pf_id;
                ((MainActivity)context).follow(FollowFragment.newInstance());
                break;

        }
    }
}