package com.example.guest.newyorktimesclient.di.components;

/**
 * Created by l1maginaire on 1/3/18.
 */

import com.example.guest.newyorktimesclient.di.modules.NewsModule;
import com.example.guest.newyorktimesclient.interfaces.ApplicationScope;
import dagger.Component;
import com.example.guest.newyorktimesclient.interfaces.NytApi;

@ApplicationScope
@Component(modules = {NewsModule.class})
public interface NewsComponent {

    NytApi getNewsService();
}
