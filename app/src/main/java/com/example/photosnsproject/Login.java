package com.example.photosnsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button Login_btn_signin;
    EditText Login_id,Login_pwd;
    String string_id, string_pwd;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference db = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        Login_id=(EditText)findViewById(R.id.Login_id);
        Login_pwd=(EditText)findViewById(R.id.Login_pwd);

    }

    public void Login_signin(View view) {
        Intent intent=new Intent(this, Signin.class);
        startActivity(intent);
    }

    public void Login_main(View view) {
        string_id=Login_id.getText().toString();
        string_pwd=Login_pwd.getText().toString();


        Intent intent=new Intent(this, MainActivity.class);

        db.child("Users").child(string_id).addValueEventListener(new ValueEventListener() { //addValue함수는 데이터베이스에 접근은 하는데 서버에 변화가 있으면 ValueEventListener 실행.
            @Override
            //정상적으로 실행될 때.
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {
                    User user = snapshot.getValue(User.class);

                    if (user.getPassword().equals(string_pwd)) {
                        PreferenceManager.setUserID(getApplicationContext(), string_id);

                        startActivity(intent);
                    } else {
                        sendmsg("비밀번호가 틀렸습니다.");
                    }
                }
                catch (NullPointerException e){
                    sendmsg("잘못된 아이디입니다.");
                }
            }
            @Override
            //오류 생길때.
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void sendmsg(String s)
    {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
}