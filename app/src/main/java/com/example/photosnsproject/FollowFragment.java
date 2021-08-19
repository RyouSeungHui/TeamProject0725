package com.example.photosnsproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.zip.Inflater;


public class FollowFragment extends Fragment {

    public FollowFragment(String id, int page,int index) {

        this.id=id;
        this.page=page;
        this.index=index;
    }
    String id;
    int page, index;
    private View view;
    private Context context;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getActivity();
        view = inflater.inflate(R.layout.fragment_follow, container, false);


        ViewPager vp = (ViewPager) (view).findViewById(R.id.Viewpager);
        Follow_ViewPager_Adapter adapter = new  Follow_ViewPager_Adapter(getChildFragmentManager());

        FollowerFragment followerFragment=new FollowerFragment(id,index);
        FollowingFragment followingFragment=new FollowingFragment(id,index);

        adapter.addFragment(followerFragment,"팔로워");
        adapter.addFragment(followingFragment,"팔로잉");

        vp.setAdapter(adapter);

        TabLayout tab = (TabLayout)(view).findViewById(R.id.Tabs);
        tab.setupWithViewPager(vp);

        vp.setCurrentItem(page);
        return view;
    }


}