package com.example.guest.newyorktimesclient;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.guest.newyorktimesclient.Model.Result;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    boolean mIsLoading;
    private List<Result> mItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchItemsTask().execute();
            }
        });
    }

    private class FetchItemsTask extends AsyncTask<Integer, Void, List<Result>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mIsLoading = true;
        }

        @Override
        protected List<Result> doInBackground(Integer... params) {
//            return new FlickrFetchr().fetchItems();
            return new Fetcher().fetchRecentPhotos();
        }

        @Override
        protected void onPostExecute(List<Result> items) {
            mItems.addAll(items);
            mIsLoading = false;
        }
    }
}