package com.example.guest.newyorktimesclient.di.modules

import com.example.guest.newyorktimesclient.helper.gson.JsonConverter
import com.example.guest.newyorktimesclient.helper.gson.JsonConverterImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class GsonModule {

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

    @Singleton
    @Provides
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory = GsonConverterFactory.create(gson)

    @Singleton
    @Provides
    fun provideJsonConverter(gson: Gson): JsonConverter = JsonConverterImpl(gson)
}