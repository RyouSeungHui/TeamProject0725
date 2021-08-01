package com.example.photosnsproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileFragment extends Fragment {
    private View view;
    private Context context;
    TextView pf_id;
    String string_pf_id;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        context = getActivity();
        view = inflater.inflate(R.layout.activity_profile_fragment, container, false);
        pf_id=(TextView)view.findViewById(R.id.pf_id); //2
        string_pf_id=getArguments().getString("id");
        pf_id.setText(string_pf_id);


        return view;
    }
}