package com.example.photosnsproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FriendsFragment friendsFragment;
    private MapFragment mapFragment;
    private MyFragment myFragment;
    private RandomFragment randomFragment;
    private ViewPager viewPager;

    private View[] viewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();
    }

    private void Init() {
        fragmentManager = getSupportFragmentManager();
        friendsFragment = new FriendsFragment();
        mapFragment = new MapFragment();
        myFragment = new MyFragment();
        randomFragment = new RandomFragment();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, friendsFragment).commitAllowingStateLoss();

        viewGroup = new View[4];
        viewGroup[0] = findViewById(R.id.view1);
        viewGroup[1] = findViewById(R.id.view2);
        viewGroup[2] = findViewById(R.id.view3);
        viewGroup[3] = findViewById(R.id.view4);
    }

    public void clickHandler(View view) {
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (view.getId()){
            case R.id.ll1:
                fragmentTransaction.replace(R.id.flFragment, mapFragment).commitAllowingStateLoss();
                SetView(0);
                break;
            case R.id.ll2:
                fragmentTransaction.replace(R.id.flFragment, randomFragment).commitAllowingStateLoss();
                SetView(1);
                break;
            case R.id.ll3:
                fragmentTransaction.replace(R.id.flFragment, friendsFragment).commitAllowingStateLoss();
                SetView(2);
                break;
            case R.id.ll4:
                fragmentTransaction.replace(R.id.flFragment, myFragment).commitAllowingStateLoss();
                SetView(3);
                break;
        }
    }

    private void SetView(int targetI) {
        for(int i = 0; i<4; i++){
            if(i==targetI) viewGroup[i].setBackgroundColor(Color.BLACK);
            else viewGroup[i].setBackgroundColor(Color.WHITE);
        }
    }


}