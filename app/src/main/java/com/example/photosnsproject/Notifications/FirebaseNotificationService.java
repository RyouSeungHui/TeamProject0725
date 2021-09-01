package com.example.photosnsproject.Notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.photosnsproject.MainActivity;
import com.example.photosnsproject.PostInfoFragment;
import com.example.photosnsproject.PreferenceManager;
import com.example.photosnsproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FirebaseNotificationService extends FirebaseMessagingService {


    String post_id;
    String user_id;
    String send_id;
    private String userID;
    public FirebaseNotificationService() {
    }
//받는사람
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e("Service", "zzz");
        Map<String, String> data_notify = remoteMessage.getData();

        if (data_notify != null) {
            Log.e("FCMService", "received");
            post_id = data_notify.get("post_id");
            user_id = data_notify.get("user_id");
            send_id=data_notify.get("send_id");
            userID = PreferenceManager.getUserId(getApplicationContext());
            final String userID = PreferenceManager.getUserId(getApplicationContext());



                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                Bundle bundle=new Bundle(2);
                intent.putExtra("user_id",user_id);
                intent.putExtra("post_id",post_id);
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



                PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


                String channelId = "mychannel";

                Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(getApplicationContext(), channelId)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(send_id+"님이 댓글을 작성하였습니다.")
                                .setAutoCancel(true)
                                .setSound(defaultUri)
                                .setContentIntent(pendingIntent);

                NotificationManager notificationManger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String channelName = "channelName";
                    NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                    notificationManger.createNotificationChannel(channel);
                }
                notificationManger.notify(0, notificationBuilder.build());

        }

    }

}



