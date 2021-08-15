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
    private ArrayList<String> list2;
    private ArrayList<User> wholelist;
    private ArrayList<PostItem> wholelist2;
    private FirebaseDatabase database;
    private DatabaseReference db;
    private String temp;
    EditText search_edit;
    EditText search_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_edit=findViewById(R.id.search_edit);
        search_tag=findViewById(R.id.search_tag);
        recyclerView=findViewById(R.id.search_rcy);
        recyclerView.setHasFixedSize(true); //정리한번
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        list=new ArrayList<>();
        list2=new ArrayList<>();
        wholelist=new ArrayList<>();
        wholelist2=new ArrayList<>();
        adapter=new SearchAdapter(list, this);
        recyclerView.setAdapter(adapter);
        database=FirebaseDatabase.getInstance();
        db=database.getReference();


        list.clear();
        list2.clear();


        db.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
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

         db.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wholelist2.clear();
                for (DataSnapshot sn : snapshot.getChildren()){
                    User user=sn.getValue(User.class);
                    String strId = user.getId();
                    db.child("Users").child(strId).child("post").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot sn2 : snapshot.getChildren()) {
                                PostItem postitem = sn2.getValue(PostItem.class);

                                try {
                                    ArrayList<String> arrTag = postitem.getTag();

                                    for(int i=0; i<arrTag.size(); i++)
                                    {
                                        wholelist2.add(postitem);
                                    }
                                }

                                catch(NullPointerException e) {

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
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

        // 태그를 이용한 검색

        search_tag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String string_tag;
                string_tag=search_tag.getText().toString();
                search_text2(string_tag);

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


    public void search_text2(String s){
        list2.clear();
        for(int i=0; i<wholelist2.size(); i++){

            for(int j=0; j<wholelist2.get(i).getTag().size(); j++)
            {
                String strTag=wholelist2.get(i).getTag().get(j);
                if(strTag.contains(s)) {
                    list2.add(strTag);
                }
            }
        }

        adapter.notifyDataSetChanged(); //데이터 바뀐걸 어뎁터한테 알려줌.
    }

}