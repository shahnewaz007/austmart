package com.ayon.austmart.Fragments;

import com.ayon.austmart.Notifications.MyResponse;
import com.ayon.austmart.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;



    public interface APIService {
        @Headers(
                {
                        "Content-Type:application/json",
                        "Authorization:key=AAAA1Mt1_ZU:APA91bEhMArgSKX6HBg8DMU3BFmITOqz1MhFcwrTASTUZxCQgoM-jAPA-wtXBshEAGH74IPakf7PuLZtB8UkBQNA7eDlieD8_X942ICfhxt3Ifp3zcwu9WdfCDUbPjlWJzvTTcFxLvY_"
                }
        )



    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);





}
