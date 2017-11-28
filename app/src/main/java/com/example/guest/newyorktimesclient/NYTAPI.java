package com.example.guest.newyorktimesclient;

import com.example.guest.newyorktimesclient.Model.NewsArr;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NYTAPI {

    @GET("svc/news/v3/content/all/all.json")
    Call<NewsArr> getDefault(@Query("limit") int limit, @Query("api-key") String key, @Query("offset") int offset);

    @GET("svc/news/v3/content/all/world.json")
    Call<NewsArr> getWorld(@Query("api-key") String key);

    @GET("svc/news/v3/content/all/fashion.json")
    Call<NewsArr> getFashion(@Query("api-key") String key);

    @GET("svc/news/v3/content/all/business.json")
    Call<NewsArr> getBusiness(@Query("api-key") String key);

    @GET("svc/news/v3/content/all/arts.json")
    Call<NewsArr> getArts(@Query("api-key") String key);

    @GET("svc/news/v3/content/all/science.json")
    Call<NewsArr> getScience(@Query("api-key") String key);


}

