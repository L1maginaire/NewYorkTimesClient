package com.example.guest.newyorktimesclient.di.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by l1maginaire on 2/28/18.
 */

@Module
open class ApplicationModule(private val application: Application) {
    @Singleton
    @Provides
    open fun provideApplication() = application

    @Singleton
    @Provides
    @Named("app_context")
    open fun provideContext() = application.applicationContext
}