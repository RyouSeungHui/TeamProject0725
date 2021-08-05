package com.example.photosnsproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class AutoLogin extends AppCompatActivity {
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_login);
        if(PreferenceManager.getUserId(getApplicationContext()).length()==0){
            intent = new Intent(AutoLogin.this, Login.class);
            startActivity(intent);
            finish();
        }else{
            intent = new Intent(AutoLogin.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}