package com.example.photosnsproject.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAUj5mnIw:APA91bGkTkkoYEzQjZq-iHBc7j3aHARmHKdXp56htaxFzEQdWB_8bNcf4u_gR_1Ie1oT1XEowTXoaRWjVjSFri-aw9816mofIJ43gKadfuhpVi3Nnrtp4d6sK7dJikNSePrtYfCYkIeU"

                    //서버키
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationData body);
}
