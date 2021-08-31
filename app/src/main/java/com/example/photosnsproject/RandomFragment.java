package com.example.photosnsproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RandomFragment extends Fragment implements View.OnClickListener{
    private static final int RESULT_OK = -1;
    private View view;
    private Context context;
    private TextView search_tag;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private TagPostingFragment TagPostingFragment;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        view = inflater.inflate(R.layout.activity_random_fragment, container, false);

        context = getActivity();
        search_tag=(view).findViewById(R.id.search_tag);
        fragmentManager = getActivity().getSupportFragmentManager();

        search_tag.setOnClickListener(this);
        TagPostingFragment = new TagPostingFragment();

        // 주석
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_tag:
                Intent intent = new Intent(getActivity(), SearchTag.class);
                startActivityForResult(intent, 2);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2){
            if(resultCode==RESULT_OK){
                ArrayList<String> user_id= data.getStringArrayListExtra("user_id");
                ArrayList<String> post_id= data.getStringArrayListExtra("post_id");
                Bundle bundle = new Bundle(2);
                bundle.putStringArrayList("user_id", user_id);
                bundle.putStringArrayList("post_id", post_id);
                TagPostingFragment.setArguments(bundle);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flFragment, TagPostingFragment).commitAllowingStateLoss();
            }
        }
    }

}