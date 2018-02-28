package com.example.guest.newyorktimesclient.di.modules;

import com.example.guest.newyorktimesclient.api.NytApi;
import com.example.guest.newyorktimesclient.di.scope.PerActivity;
import com.example.guest.newyorktimesclient.mvp.view.MainView;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by l1maginaire on 2/28/18.
 */

@Module
public class NewsModule {

    private MainView view;

    public NewsModule(MainView view) {
        this.view = view;
    }

    @PerActivity
    @Provides
    NytApi provideApiService(Retrofit retrofit) {
        return retrofit.create(NytApi.class);
    }

    @PerActivity
    @Provides
    MainView provideView() {
        return view;
    }
}