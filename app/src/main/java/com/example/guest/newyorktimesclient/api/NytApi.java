package com.example.guest.newyorktimesclient.api;

import com.example.guest.newyorktimesclient.mvp.model.LatestModel.Response;
import com.example.guest.newyorktimesclient.mvp.model.QueryModel.QueryArr;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NytApi {

    @GET("svc/news/v3/content/all/all.json")
    Observable<Response> getDefault(@Query("limit") int limit, @Query("api-key") String key, @Query("offset") int offset);

    @GET("svc/search/v2/articlesearch.json")
    Observable<QueryArr> getQuery(@Query("q") String query, @Query("page") int offset, @Query("api-key") String key);
}

