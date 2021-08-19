package com.example.photosnsproject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class SearchTag extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter_tag;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> list_tag;
    private ArrayList<PostItem> wholelist_tag;
    private FirebaseDatabase database;
    private DatabaseReference db;
    EditText search_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tag);

        search_tag=findViewById(R.id.search_tag);
        recyclerView=findViewById(R.id.search_rcy);
        recyclerView.setHasFixedSize(true); //정리한번
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        list_tag=new ArrayList<>();
        wholelist_tag=new ArrayList<>();

        adapter_tag=new SearchTagAdapter(list_tag, this);

        recyclerView.setAdapter(adapter_tag);
        database=FirebaseDatabase.getInstance();
        db=database.getReference();

        list_tag.clear();


        db.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wholelist_tag.clear();
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
                                        wholelist_tag.add(postitem);
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

    public void search_text2(String s){
        list_tag.clear();
        for(int i=0; i<wholelist_tag.size(); i++){

            for(int j=0; j<wholelist_tag.get(i).getTag().size(); j++)
            {
                String strTag=wholelist_tag.get(i).getTag().get(j);
                if(strTag.contains(s)) {
                    list_tag.add(strTag);
                }
            }
        }

        adapter_tag.notifyDataSetChanged(); //데이터 바뀐걸 어뎁터한테 알려줌.
    }

}