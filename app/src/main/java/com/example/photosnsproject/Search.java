package com.example.photosnsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Adapter;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> list;
    private ArrayList<User> wholelist;
    private FirebaseDatabase database;
    private DatabaseReference db;
    EditText search_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_edit=findViewById(R.id.search_edit);
        recyclerView=findViewById(R.id.search_rcy);

        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        list=new ArrayList<>();
        wholelist=new ArrayList<>();
        adapter=new SearchAdapter(list, this);
        recyclerView.setAdapter(adapter);
        database=FirebaseDatabase.getInstance();
        db=database.getReference();
        list.clear();
        db.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wholelist.clear();
                for (DataSnapshot sn : snapshot.getChildren()){
                    User user = sn.getValue(User.class); //1
                    wholelist.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String string_edit;
                string_edit=search_edit.getText().toString();
                search_text(string_edit);

            }
        });




    }

    public void search_text(String s){
        list.clear();
        for(int i=0; i<wholelist.size(); i++){

            if(wholelist.get(i).getNick().contains(s)){
                list.add(wholelist.get(i));
            }
        }
        adapter.notifyDataSetChanged(); //데이터 바뀐걸 어뎁터한테 알려줌.
    }
}