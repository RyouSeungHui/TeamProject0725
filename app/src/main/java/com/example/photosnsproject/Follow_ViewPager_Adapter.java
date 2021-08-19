package com.example.photosnsproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class Follow_ViewPager_Adapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> items=new ArrayList<>();
    private ArrayList<String> titles=new ArrayList<>();
    String id;

    public Follow_ViewPager_Adapter(@NonNull FragmentManager fm) {

        super(fm);
    }
    public void addFragment(Fragment fragment, String title){
        items.add(fragment);
        titles.add(title);
    }

    //자동으로 자리 매칭
    public CharSequence getPageTitle(int position){
        return titles.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
