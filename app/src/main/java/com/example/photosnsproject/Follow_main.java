package com.example.photosnsproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

public class Follow_main extends Fragment{

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ProfileFragment profileFragment;
    private FollowerFragment followerFragment;
    private FollowingFragment followingFragment;
    private FollowFragment followFragment;
    private View view;
    private Context context;
    private String id;
    private int page;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.activity_follow_main, container, false);
        if(getArguments()!=null) {
            id = getArguments().getString("id");
            page = getArguments().getInt("page", 0);
        }

        fragmentManager =getActivity().getSupportFragmentManager();
        profileFragment= new ProfileFragment();
        followFragment=new FollowFragment(id,page); //index : 1 => Follow_main
        //시작화면.
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.follow_fragment,followFragment ).commitAllowingStateLoss();



        return view;

    }

}