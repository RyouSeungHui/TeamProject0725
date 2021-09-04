package com.example.photosnsproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationFragment extends Fragment {

    private View view;
    private Context context;
    private Uri filePath;
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference db=database.getReference();
    private FirebaseStorage storage=FirebaseStorage.getInstance();
    private StorageReference storageReference=storage.getReference();
    private LinearLayout notification_linear1;
    private TextView notification_content;
    private TextView notification_getTime;
    private CircleImageView notification_profile;
    private ImageView notification_post;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Notification_Item> notification_itemArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.notification_fragment, container, false);
        notification_linear1=view.findViewById(R.id.notification_linear1);
        notification_content=view.findViewById(R.id.notification_content);
        notification_profile=view.findViewById(R.id.notification_profile);
        notification_post=view.findViewById(R.id.notification_post);
        notification_getTime=view.findViewById(R.id.notification_getTime);
        recyclerView = (view).findViewById(R.id.notification_rcy);
        recyclerView.setHasFixedSize(true); //정리한번
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        notification_itemArrayList=new ArrayList<>();

        String user=getArguments().getString("user_id");

        db.child("Alarm").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notification_itemArrayList.clear();
                for(DataSnapshot sn:snapshot.getChildren()){
                    Notification_Item notification_item=sn.getValue(Notification_Item.class);
                    notification_itemArrayList.add(notification_item);

                }
                adapter = new NotificationAdapter(context,notification_itemArrayList);
                recyclerView.setAdapter(adapter);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;
    }


}
