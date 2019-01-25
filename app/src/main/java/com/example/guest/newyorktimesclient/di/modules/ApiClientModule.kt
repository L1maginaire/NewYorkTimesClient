package com.example.guest.newyorktimesclient.di.modules

import com.example.guest.newyorktimesclient.api.NytApi
import com.example.guest.newyorktimesclient.helper.gson.JsonConverter
import com.example.guest.newyorktimesclient.utils.NetworkChecker
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [
    NetworkModule::class,
    SchedulerProviderModule::class
])
class ApiClientModule {

//    @Singleton
//    @Provides
//    fun provideApiClient(apiService: NytApi, jsonConverter: JsonConverter, networkChecker: NetworkChecker) = ApiClientImpl(apiService, jsonConverter, networkChecker)
}