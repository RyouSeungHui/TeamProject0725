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

import java.util.ArrayList;


public class SearchTagAdapter extends RecyclerView.Adapter<SearchTagAdapter.SearchTagViewHolder> {

    private ArrayList<String> list_postitem;
    private Context context_postitem;


    public SearchTagAdapter(ArrayList<String> list_postitem, Context context_postitem){ //생성자

        this.list_postitem=list_postitem;
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

        holder.search_text.setText(list_postitem.get(position));
        holder.sp_linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent=new Intent();
                intent.putExtra("postitem", list_postitem.get(position));
                ((Activity)context_postitem).setResult(Activity.RESULT_OK,intent); //결과설정
                ((Activity)context_postitem).finish();

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