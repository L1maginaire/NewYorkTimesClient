package com.example.guest.newyorktimesclient.di.modules

import android.content.Context
import com.example.guest.newyorktimesclient.helper.ImageLoader
import com.example.guest.newyorktimesclient.utils.NetworkChecker
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class ImageLoaderModule {
    @Singleton
    @Provides
    fun provideImageLoader(@Named("app_context") context: Context, networkChecker: NetworkChecker) = ImageLoader (context, networkChecker)
}