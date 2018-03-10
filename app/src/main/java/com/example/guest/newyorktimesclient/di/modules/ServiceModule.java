package com.example.guest.newyorktimesclient.di.modules;

import com.example.guest.newyorktimesclient.api.NytApi;
import com.example.guest.newyorktimesclient.di.scope.PerActivity;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by l1maginaire on 3/10/18.
 */

@Module
public class ServiceModule {

    @PerActivity
    @Provides
    NytApi provideApiService(Retrofit retrofit) {
        return retrofit.create(NytApi.class);
    }
}