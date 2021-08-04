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

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.FollowViewHolder>{

    private ArrayList<String> list;
    private Context context;

    public FollowAdapter( ArrayList<String> list, Context context){

        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    public FollowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_profile,parent,false);
        FollowAdapter.FollowViewHolder holder = new FollowAdapter.FollowViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FollowViewHolder holder, int position) {
        holder.followlist_text.setText(list.get(position));
        /*
        holder.fp_linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("id", list.get(position).getId());
                ((Activity)context).setResult(Activity.RESULT_OK,intent); //결과설정
                ((Activity)context).finish();

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return (list !=null ? list.size() : 0);
    }


    public class FollowViewHolder extends RecyclerView.ViewHolder {
        TextView followlist_text;
        LinearLayout fp_linear1;
        public FollowViewHolder(@NonNull View itemView) {
            super(itemView);
            this.followlist_text=itemView.findViewById(R.id.followlist_text);
            this.fp_linear1=itemView.findViewById(R.id.fp_linear1);
        }
    }
}
