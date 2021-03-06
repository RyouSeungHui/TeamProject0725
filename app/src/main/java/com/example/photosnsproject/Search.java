package com.example.photosnsproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    EditText search_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_edit = findViewById(R.id.search_edit);
        search_tag = findViewById(R.id.search_tag);
        recyclerView = findViewById(R.id.search_rcy);
        recyclerView.setHasFixedSize(true); //정리한번
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        wholelist = new ArrayList<>();

        adapter = new SearchAdapter(list, this);

        recyclerView.setAdapter(adapter);
        database = FirebaseDatabase.getInstance();
        db = database.getReference();

        list.clear();


        db.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wholelist.clear();
                for (DataSnapshot sn : snapshot.getChildren()) {
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
                string_edit = search_edit.getText().toString();
                if (string_edit.length() > 0) {
                    search_text(string_edit);
                }

            }
        });


    }

    public void search_text(String s) {
        list.clear();
        for (int i = 0; i < wholelist.size(); i++) {
            try {
                if (wholelist.get(i).getNick().contains(s)) {
                    list.add(wholelist.get(i));

                    adapter.notifyDataSetChanged();
                }
            } catch (NullPointerException e) {}//데이터 바뀐걸 어뎁터한테 알려줌.
        }

    }
}