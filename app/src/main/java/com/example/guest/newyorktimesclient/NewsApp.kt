package com.example.guest.newyorktimesclient

import android.app.Application

import com.example.guest.newyorktimesclient.di.components.ApplicationComponent
import com.example.guest.newyorktimesclient.di.components.DaggerApplicationComponent
import com.example.guest.newyorktimesclient.di.modules.ApplicationModule

/**
 * Created by l1maginaire on 2/28/18.
 */

class NewsApp : Application() {
    var applicationComponent: ApplicationComponent? = null
        private set

    override fun onCreate() {
        super.onCreate()
        initializeAppComponent()
    }

    private fun initializeAppComponent() {
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }
}
