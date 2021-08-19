package com.example.photosnsproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class Follow_main extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ProfileFragment profileFragment;
    private FollowerFragment followerFragment;
    private FollowingFragment followingFragment;
    private FollowFragment followFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_main);

        Intent intent=getIntent();
        String id=intent.getStringExtra("id");
        int page=intent.getIntExtra("page",0);

        fragmentManager = getSupportFragmentManager();
        profileFragment= new ProfileFragment();
        followFragment=new FollowFragment(id,page,1);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.follow_fragment,followFragment ).commitAllowingStateLoss();



    }

    public void follow(Fragment fragment){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.follow_fragment, fragment).commitAllowingStateLoss();
    }
}