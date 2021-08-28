package com.example.photosnsproject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.shape.CornerSize;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class LikeList extends BottomSheetDialogFragment {
    private ArrayList<String> likePeople;
    private LinearLayout llLike;
    private Context context;
    private View view;

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    public LikeList(ArrayList<String> likePeople) {
        this.likePeople = likePeople;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        context = getActivity();
        view = inflater.inflate(R.layout.activity_like_list, container, false);

        setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, 0);
        llLike = view.findViewById(R.id.llLike);

        Log.e("@@", likePeople.size()+"");

        for(int i=0; i<likePeople.size(); i++){
            String person = likePeople.get(i);

            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setGravity(Gravity.CENTER_VERTICAL);
            ll.setPadding(10, 10, 10, 10);
            ll.setOnClickListener(v -> {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(LikeList.this).commit();
                fragmentManager.popBackStack();

                ProfileFragment profileFragment = new ProfileFragment();
                Bundle bundle = new Bundle(1);
                bundle.putString("id",person);
                profileFragment.setArguments(bundle);
                ((MainActivity)context).follow(profileFragment);
            });

            LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(150, 150);
            lp.rightMargin = 20;
            ImageView img = new ImageView(context);
            img.setLayoutParams(lp);
            storageReference.child("profile/"+person+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).
                            load(uri)
                            .override(2000,2000)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .thumbnail(0.1f)
                            .centerCrop()
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }
                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(img);
                }
            });
            ll.addView(img);

            TextView tv = new TextView(context);
            databaseReference.child("Users").orderByKey().equalTo(person).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds: snapshot.getChildren()){
                        tv.setText(ds.child("nick").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            tv.setTextSize(15);
            tv.setTextColor(getResources().getColor(R.color.blackPrimary));
            tv.setPadding(10, 10, 10, 10);
            ll.addView(tv);

            llLike.addView(ll);
        }

        return view;
    }
}