package com.example.guest.newyorktimesclient;

import com.example.guest.newyorktimesclient.Model.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NYTAPI {
    @GET("svc/news/v3/content/all/all/24.json")
    Call<List<Result>> getData(@Query("limit") int count, @Query("api-key") String key);
}

