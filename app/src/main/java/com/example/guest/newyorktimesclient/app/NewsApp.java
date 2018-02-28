package com.example.guest.newyorktimesclient.app;

import android.app.Application;

import com.example.guest.newyorktimesclient.di.components.ApplicationComponent;
import com.example.guest.newyorktimesclient.di.modules.ApplicationModule;

/**
 * Created by l1maginaire on 2/28/18.
 */

public class NewsApp extends Application{
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeAppComponent();
    }

    private void initializeAppComponent() {
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent(){
        return applicationComponent;
    }
}
