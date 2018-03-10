package com.example.guest.newyorktimesclient.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.example.guest.newyorktimesclient.BuildConfig;
import com.example.guest.newyorktimesclient.api.NytApi;
import com.example.guest.newyorktimesclient.app.NewsApp;
import com.example.guest.newyorktimesclient.di.components.DaggerNewsComponent;
import com.example.guest.newyorktimesclient.di.components.DaggerServiceComponent;
import com.example.guest.newyorktimesclient.di.components.ServiceComponent;
import com.example.guest.newyorktimesclient.di.modules.NewsModule;
import com.example.guest.newyorktimesclient.di.modules.ServiceModule;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PollService extends IntentService {
    private static final String TAG = "PollService";

    @Inject
    protected NytApi apiService;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String title;

    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.d(TAG, "onHandleIntent: ");
        resolveDaggerDependencies();
            compositeDisposable.add(apiService.getDefault(20, BuildConfig.API_KEY, 0)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(data -> data.getNews())
                    .subscribe(news -> title = news.get(0).getTitle()));
        }
    }

    protected void resolveDaggerDependencies() {
        DaggerServiceComponent.builder()
                .applicationComponent(((NewsApp) getApplication()).getApplicationComponent())
                .serviceModule(new ServiceModule())
                .build()
                .inject(this);
    }
}
