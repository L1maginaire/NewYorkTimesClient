package com.example.guest.newyorktimesclient.interfaces;

import com.example.guest.newyorktimesclient.model.LatestModel.NewsArr;
import com.example.guest.newyorktimesclient.model.QueryModel.QueryArr;

import retrofit2.http.GET;
import retrofit2.http.Query;
import io.reactivex.Single;

public interface NytApi {

    @GET("svc/news/v3/content/all/all.json")
    Single<NewsArr> getDefault(@Query("limit") int limit, @Query("api-key") String key, @Query("offset") int offset);

    @GET("svc/search/v2/articlesearch.json")
    Single<QueryArr> getQuery(@Query("q") String query, @Query("page") int offset, @Query("api-key") String key);
}

