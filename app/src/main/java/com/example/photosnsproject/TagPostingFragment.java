package com.example.photosnsproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TagPostingFragment extends Fragment {
    private View view;
    private Context context;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<String> user_id;
    private ArrayList<String> post_id;
    private ArrayList<String> imagelist;


    public static TagPostingFragment newInstance(){
        return new TagPostingFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        context = getActivity();
        view = inflater.inflate(R.layout.tagpostingfragment, container, false);

        user_id = new ArrayList<>();
        post_id = new ArrayList<>();
        imagelist = new ArrayList<>();

        user_id = getArguments().getStringArrayList("user_id");
        post_id = getArguments().getStringArrayList("post_id");
        for(int i=0;i<user_id.size();i++) {
            imagelist.add(user_id.get(i)+"/"+post_id.get(i));
        }

        recyclerView = (view).findViewById(R.id.tag_posting_rcv);
        recyclerView.setHasFixedSize(true); //정리한번
        layoutManager = new GridLayoutManager(view.getContext(),3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new tagpostAdapter(user_id,post_id, context);
        recyclerView.setAdapter(adapter);


        return view;
    }
}
