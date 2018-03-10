package com.example.guest.newyorktimesclient.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.guest.newyorktimesclient.BuildConfig;
import com.example.guest.newyorktimesclient.api.NytApi;
import com.example.guest.newyorktimesclient.app.NewsApp;
import com.example.guest.newyorktimesclient.di.components.DaggerServiceComponent;
import com.example.guest.newyorktimesclient.di.modules.ServiceModule;
import com.example.guest.newyorktimesclient.utils.QueryPreferences;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class PollService extends IntentService {
    private static final String TAG = "PollService";

    @Inject
    protected NytApi apiService;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String title;
    private SharedPreferences prefs;

    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.d(TAG, "onHandleIntent: ");
            resolveDaggerDependencies();
            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            boolean f = isLastMappedResultMatchingActual();
        }
    }

    protected void resolveDaggerDependencies() {
        DaggerServiceComponent.builder()
                .applicationComponent(((NewsApp) getApplication()).getApplicationComponent())
                .serviceModule(new ServiceModule())
                .build()
                .inject(this);
    }

    private boolean isLastMappedResultMatchingActual() {
        compositeDisposable.add(apiService.getDefault(20, BuildConfig.API_KEY, 0)
                .map(data -> data.getNews())
                .subscribe(news -> title = news.get(0).getTitle()));
        return title == prefs.getString(QueryPreferences.PREF_LAST_RESULT_ID, null); //todo в каком потоке слушать?
    }
}
