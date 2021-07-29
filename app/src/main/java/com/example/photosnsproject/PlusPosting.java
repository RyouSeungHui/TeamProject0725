package com.example.photosnsproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class PlusPosting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_posting);
    }

    public void onClickCancel(View view) {
        finish();
    }

    public void onClickOkay(View view) {

    }
}