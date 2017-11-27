package com.example.guest.newyorktimesclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.guest.newyorktimesclient.Model.Result;
import com.example.guest.newyorktimesclient.Model.NewsArr;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    //    boolean mIsLoading;
//    private List<Result> mItems = new ArrayList<>();
    private String API_KEY = "0343ec428ded42d19bb3f04b015c2e2b";
    RecyclerView recyclerView;
    List<Result> news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        news = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.posts_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Adapter adapter = new Adapter(news);
        recyclerView.setAdapter(adapter);

        App.getApi().getData(100, API_KEY).enqueue(new Callback<NewsArr>() {
            @Override
            public void onResponse(Call<NewsArr> call, Response<NewsArr> response) {
                news.addAll(response.body().getResults());
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsArr> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "An error occurred during networking", Toast.LENGTH_SHORT).show();
            }
        });
    }
}