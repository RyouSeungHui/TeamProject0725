package com.example.photosnsproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyFragment extends Fragment {
    private View view;
    private Context context;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        context = getActivity();
        view = inflater.inflate(R.layout.activity_my_fragment, container, false);

        setFloatingButton();
        return view;
    }

    private void setFloatingButton() {
        FloatingActionButton fab = view.findViewById(R.id.btnPlus);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlusPosting.class);
            startActivity(intent);
        });
    }
}