package com.example.guest.newyorktimesclient.service.impl2;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.guest.newyorktimesclient.BuildConfig;
import com.example.guest.newyorktimesclient.api.NytApi;
import com.example.guest.newyorktimesclient.mvp.model.Article;
import com.example.guest.newyorktimesclient.mvp.model.LatestModel.Response;
import com.example.guest.newyorktimesclient.utils.NetworkChecker;
import com.example.guest.newyorktimesclient.utils.QueryPreferences;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by l1maginaire on 3/9/18.
 */

public class PollService extends IntentService {
    private static final String TAG = "PollService";

    @Inject
    protected NytApi apiService;

    private List<Article> articles;
    private NotificationManagerCompat notificationManager;
    private String API_KEY = BuildConfig.API_KEY;


    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }

    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(!(NetworkChecker.isNetAvailable(this)))
            return;

        String query = QueryPreferences.getStoredQuery(this);
        String lastResultId = QueryPreferences.getLastResultTitle(this);

        Observable<Response> observable = apiService.getDefault(20, API_KEY, 0);
        observable.subscribe();

//       else{     articles = new FlickrFetchr().searchPhotos(query);
        if (articles.size() == 0) {
            return;
        }
        String resultId = articles.get(0).getTitle();
        if (resultId.equals(lastResultId)) {
            Log.i(TAG, "Got an old result: " + resultId);
        } else {
            Log.i(TAG, "Got a new result: " + resultId);
            QueryPreferences.setLastResultId(this, resultId);
        }
    }

    /*private void cancelNotification() {
    }

    private void getNotificationIntent() {
    }

    private NotificationManagerCompat getNotificationManager() {
        if (notificationManager == null)
            notificationManager = NotificationManagerCompat.from(this);
        return notificationManager;
    }

    private void resolveDaggerDependencies(){

    }

    @Override
    public void onDestroy() {
        cleanUp();
        super.onDestroy();
    }

    private void cleanUp() {}

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }*/
}