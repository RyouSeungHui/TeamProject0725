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

import java.util.ArrayList;

public class SearchTagAdapter extends RecyclerView.Adapter<SearchTagAdapter.SearchTagViewHolder> {

    private ArrayList<String> list_string;
    private Context context_postitem;
    private FirebaseDatabase database;
    private DatabaseReference db;
    private ArrayList<String> arrId_adapter;
    private ArrayList<String> postId_adapter;


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
        arrId_adapter=new ArrayList<>();
        postId_adapter=new ArrayList<>();
        holder.search_text.setText(list_string.get(position));
        holder.sp_linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String key = holder.search_text.getText().toString();

                db.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

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
                                            for(int i=0; i<arrTag_Adapter.size(); i++)
                                            {
                                                if(arrTag_Adapter.get(i).equals(key))
                                                {
                                                    arrId_adapter.add(strId_Adapter);
                                                    postId_adapter.add(sn2.getKey());

                                                }
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

                Intent intent=new Intent();
                intent.putStringArrayListExtra("user_id", arrId_adapter);
                intent.putStringArrayListExtra("post_id", postId_adapter);
                ((Activity)context_postitem).setResult(Activity.RESULT_OK,intent); //결과설정
                ((Activity)context_postitem).finish();


            }
        });

    }

    @Override
    public int getItemCount() {

        return (list_string !=null ? list_string.size() : 0);
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
