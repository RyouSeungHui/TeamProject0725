package com.example.photosnsproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class SearchTagAdapter extends RecyclerView.Adapter<SearchTagAdapter.SearchTagViewHolder> {

    private ArrayList<String> list_string;
    private Context context_postitem;
    private FirebaseDatabase database;
    private DatabaseReference db;
    private ArrayList<PostItem> list_postitem;


    public SearchTagAdapter(ArrayList<String> list_string, Context context_postitem){ //생성자

        this.list_string=list_string;
        this.context_postitem=context_postitem;
    }

    @NonNull
    @Override
    public SearchTagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_profile,parent,false);
        SearchTagAdapter.SearchTagViewHolder holder = new SearchTagAdapter.SearchTagViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull SearchTagViewHolder holder, int position) {

        database = FirebaseDatabase.getInstance();
        db = database.getReference();

        holder.search_text.setText(list_string.get(position));
        holder.sp_linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String key = holder.search_text.getText().toString();

                db.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list_postitem.clear();
                        for (DataSnapshot sn : snapshot.getChildren()) {
                            User user_Adapter = sn.getValue(User.class);
                            String strId_Adapter = user_Adapter.getId();
                            db.child("Users").child(strId_Adapter).child("post").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot sn2 : snapshot.getChildren()) {
                                        PostItem postitem_Adapter = sn2.getValue(PostItem.class);

                                        try {
                                            ArrayList<String> arrTag_Adapter = postitem_Adapter.getTag();

                                            if (arrTag_Adapter.equals(key)) {
                                                list_postitem.add(postitem_Adapter);
                                            }
                                        } catch (NullPointerException e) {

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
            }
        });
    }
    @Override

    public int getItemCount() {

        return (list_postitem !=null ? list_postitem.size() : 0);
    }

    //기능 설정
    public class SearchTagViewHolder extends RecyclerView.ViewHolder {

        TextView search_text;
        LinearLayout sp_linear1;

        public SearchTagViewHolder(@NonNull View itemView) {
            super(itemView);
            this.search_text=itemView.findViewById(R.id.search_text);
            this.sp_linear1=itemView.findViewById(R.id.sp_linear1);
        }
    }

}