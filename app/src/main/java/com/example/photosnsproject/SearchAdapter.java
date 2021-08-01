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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private ArrayList<User> list;
    private Context context; //무슨화면

    public SearchAdapter( ArrayList<User> list, Context context){ //생성자

        this.list=list;
        this.context=context; //무슨 화면
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_profile,parent,false);
        SearchAdapter.SearchViewHolder holder = new SearchAdapter.SearchViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        holder.search_text.setText(list.get(position).getNick());
        holder.sp_linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("id", list.get(position).getId());
                ((Activity)context).setResult(Activity.RESULT_OK,intent);
                ((Activity)context).finish();

            }
        });

    }

    @Override
    public int getItemCount() {

        return (list !=null ? list.size() : 0);
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView search_text;
        LinearLayout sp_linear1;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            this.search_text=itemView.findViewById(R.id.search_text);
            this.sp_linear1=itemView.findViewById(R.id.sp_linear1);

        }
    }
}
