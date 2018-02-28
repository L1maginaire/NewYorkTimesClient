package com.example.guest.newyorktimesclient.di.components;

/**
 * Created by l1maginaire on 1/3/18.
 */

import com.example.guest.newyorktimesclient.di.modules.NewsModule;
import com.example.guest.newyorktimesclient.di.scope.PerActivity;
import com.example.guest.newyorktimesclient.ui.activities.MainActivity;

import dagger.Component;

@PerActivity
@Component(modules = NewsModule.class, dependencies = ApplicationComponent.class)
public interface NewsComponent {
    void inject(MainActivity activity);
}