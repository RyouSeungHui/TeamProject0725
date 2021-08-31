package com.example.photosnsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class Signin extends AppCompatActivity {

    EditText Sigin_id,Sigin_pwd,Sigin_nick;
    Button Signin_btn_signin;
    String string_id, string_pwd,string_nick;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference db = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Sigin_id=(EditText)findViewById(R.id.Signin_id);
        Sigin_pwd=(EditText)findViewById(R.id.Signin_pwd);
        Sigin_nick=(EditText)findViewById(R.id.Signin_nick);

        Signin_btn_signin=(Button)findViewById(R.id.Signin_btn_signin);


    }


    public void signin(View view) {

        string_id=Sigin_id.getText().toString();
        string_pwd=Sigin_pwd.getText().toString();
        string_nick=Sigin_nick.getText().toString();



        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(Signin.this, "토큰 생성 실패", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String token = task.getResult();

                        User newuser = new User(string_id, string_pwd, string_nick, token);


                        db.child("Users").child(string_id).setValue(newuser); //id로 구분. setValue->정보 입력.



                        Intent intent=new Intent(Signin.this, Login.class);
                        startActivity(intent);
                    }
                });









    }
}