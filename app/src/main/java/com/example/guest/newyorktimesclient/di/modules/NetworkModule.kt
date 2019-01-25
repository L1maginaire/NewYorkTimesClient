package com.example.guest.newyorktimesclient.di.modules

import com.example.guest.newyorktimesclient.BuildConfig
import com.example.guest.newyorktimesclient.api.NytApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule(private val baseUrl: String) {
    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply { this.level =  HttpLoggingInterceptor.Level.BODY }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return if (BuildConfig.DEBUG) {
            OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .build()
        } else {
            OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build()
        }
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient, converterFactory: GsonConverterFactory): Retrofit = Retrofit.Builder()
                                                                                                    .baseUrl(baseUrl)
                                                                                                    .addConverterFactory(converterFactory)
                                                                                                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                                                                                    .client(client)
                                                                                                    .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): NytApi = retrofit.create(NytApi::class.java)
}