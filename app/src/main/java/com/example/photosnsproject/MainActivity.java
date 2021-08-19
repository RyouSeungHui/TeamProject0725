package com.example.photosnsproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private AppCompatActivity appCompatActivity;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FriendsFragment friendsFragment;
    private MapFragment mapFragment;
    private MyFragment myFragment;
    private ProfileFragment profileFragment;
    private RandomFragment randomFragment;
    private ViewPager viewPager;

    private ImageView[] imgGroup;
    private Drawable[] drawables_g, drawables_b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appCompatActivity = this;
        Init();
    }

    private void Init() {
        fragmentManager = getSupportFragmentManager();
        friendsFragment = new FriendsFragment();
        mapFragment = new MapFragment();
        myFragment = new MyFragment();
        randomFragment = new RandomFragment();
        profileFragment= new ProfileFragment();


        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, friendsFragment).commitAllowingStateLoss();

        imgGroup = new ImageView[4];
        imgGroup[0] = findViewById(R.id.img1);
        imgGroup[1] = findViewById(R.id.img2);
        imgGroup[2] = findViewById(R.id.img3);
        imgGroup[3] = findViewById(R.id.img4);

        drawables_g = new Drawable[4];
        drawables_g[0] = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_baseline_map_24);
        drawables_g[1] = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_baseline_star_border_24);
        drawables_g[2] = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_baseline_people_outline_24);
        drawables_g[3] = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_baseline_person_outline_24);

        drawables_b = new Drawable[4];
        drawables_b[0] = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_baseline_map_24);
        drawables_b[1] = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_baseline_star_24);
        drawables_b[2] = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_baseline_people_24);
        drawables_b[3] = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_baseline_person_pin_24);

        clickHandler(findViewById(R.id.ll3));
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

    @SuppressLint("ResourceAsColor")
    private void SetView(int targetI) {
        for(int i = 0; i<4; i++){
            if(i==targetI) {
                imgGroup[i].setImageDrawable(drawables_b[i]);
                imgGroup[i].setColorFilter(Color.parseColor("#4F4F4F"));
            }
            else {
                imgGroup[i].setImageDrawable(drawables_g[i]);
                imgGroup[i].setColorFilter(Color.parseColor("#BDBDBD"));
            }
        }
    }


    public void search(View view) {

        Intent intent = new Intent(this,Search.class);
        startActivityForResult(intent,1);
    }

    public void searchtag(View view) {
        Intent intent = new Intent(this, SearchTag.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
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
        else if(requestCode==2){

        }
    }


    public void follow(Fragment fragment){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, fragment).commitAllowingStateLoss();
    }
}