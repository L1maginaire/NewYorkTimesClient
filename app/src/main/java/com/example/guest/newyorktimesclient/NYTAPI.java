package com.example.guest.newyorktimesclient;

import com.example.guest.newyorktimesclient.Model.NewsArr;
import com.example.guest.newyorktimesclient.Model.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NYTAPI {
    //FIX Я так понял, что ссылка из документации? Там было all/arts/24.json
    //Возвращается то json объект, а не массив. Весь ответ упаковываем в NewsArr
    @GET("svc/news/v3/content/all/arts/24.json")
    Call<NewsArr> getData(@Query("limit") int count, @Query("api-key") String key);
}

