package com.example.guest.newyorktimesclient.di.modules

import com.example.guest.newyorktimesclient.helper.rx.schedulers.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class SchedulerProviderModule {

    @Singleton
    @Provides
    open fun provideSchedulers(): SchedulerProvider {
        return NormalSchedulers()
    }
}