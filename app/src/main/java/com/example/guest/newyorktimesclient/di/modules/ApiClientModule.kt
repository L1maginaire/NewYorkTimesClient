package com.example.guest.newyorktimesclient.di.modules

import com.example.guest.newyorktimesclient.api.NytApi
import com.example.guest.newyorktimesclient.helper.gson.JsonConverter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [
    NetworkModule::class,
    SchedulerProviderModule::class
])
class ApiClientModule {

    @Singleton
    @Provides
    fun provideApiClient(apiService: NytApi, jsonConverter: JsonConverter, netUtils: NetUtils, dispatchersProvider: DispatchersProvider) =
            ApiClientImpl(apiService, jsonConverter, netUtils, dispatchersProvider)
}