package com.example.guest.newyorktimesclient.di.components;

import android.content.Context;

import com.example.guest.newyorktimesclient.di.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by l1maginaire on 2/28/18.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    Retrofit exposeRetrofit();
    Context exposeContext();
}