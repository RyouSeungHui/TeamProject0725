package com.example.photosnsproject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<String> spinnerNames;
    Context context;

    public CustomSpinnerAdapter(@NonNull Context context, ArrayList<String> spinnerNames) {
        super(context, R.layout.spinner_friend_list);
        this.spinnerNames = spinnerNames;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position,convertView,parent);
    }


    @Override
    public int getCount() {
        return spinnerNames.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder sub_menu_view_holder = new ViewHolder();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        if (convertView == null) {
            LayoutInflater mInflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.spinner_friend_list, parent, false);

            sub_menu_view_holder.sub_menu_spinner_item_image = (ImageView) convertView.findViewById(R.id.sub_menu_spinner_item_image);
            sub_menu_view_holder.sub_menu_spinner_item = (TextView) convertView.findViewById(R.id.sub_menu_spinner_item);

            sub_menu_view_holder.sub_spinner_item_layout = (LinearLayout)convertView.findViewById(R.id.sub_spinner_item_layout);
            convertView.setTag(sub_menu_view_holder);
        } else {
            sub_menu_view_holder = (ViewHolder) convertView.getTag();
        }

        ViewHolder finalSub_menu_view_holder = sub_menu_view_holder;
        View finalConvertView = convertView;
        if(position!=0){
            storageReference = firebaseStorage.getReference().child("profile/"+spinnerNames.get(position)+".png");
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(finalConvertView).
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
                            .into(finalSub_menu_view_holder.sub_menu_spinner_item_image);
                }
            });
            databaseReference = firebaseDatabase.getReference().child("Users");
            databaseReference.orderByKey().equalTo(spinnerNames.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds: snapshot.getChildren()){
                        finalSub_menu_view_holder.sub_menu_spinner_item.setText(ds.child("nick").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            finalSub_menu_view_holder.sub_menu_spinner_item_image.setVisibility(View.GONE);
            sub_menu_view_holder.sub_menu_spinner_item.setText(spinnerNames.get(position));
        }



        return convertView;
    }

    private static class ViewHolder{
        LinearLayout sub_spinner_item_layout;

        ImageView sub_menu_spinner_item_image;
        TextView sub_menu_spinner_item;

    }
}

