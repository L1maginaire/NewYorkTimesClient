package com.example.guest.newyorktimesclient.di.components;

import com.example.guest.newyorktimesclient.di.modules.ServiceModule;
import com.example.guest.newyorktimesclient.di.scope.PerActivity;
import com.example.guest.newyorktimesclient.service.PollService;

import dagger.Component;

/**
 * Created by l1maginaire on 3/10/18.
 */

@PerActivity
@Component(modules = ServiceModule.class, dependencies = ApplicationComponent.class)
public interface ServiceComponent {
    void inject(PollService service);
}