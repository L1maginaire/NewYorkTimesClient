package com.example.guest.newyorktimesclient;

import com.example.guest.newyorktimesclient.Model.NewsArr;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NYTAPI {
    int hours = 168;
    @GET("svc/news/v3/content/all/all.json")
    Call<NewsArr> getData(@Query("limit") int count, @Query("api-key") String key);
}

