package com.example.guest.newyorktimesclient.di.components

import com.example.guest.newyorktimesclient.NewsApp
import com.example.guest.newyorktimesclient.di.modules.*

import javax.inject.Singleton

import dagger.Component

/**
 * Created by l1maginaire on 2/28/18.
 */

@Singleton
@Component(modules = [
    ApplicationModule::class,
    SchedulerProviderModule::class,
    GsonModule::class,
    NetworkModule::class,
    ApiClientModule::class,
    ImageLoaderModule::class
/*,
    DispatchersProviderModule::class,
    DatabaseModule::class,
    UseCaseProviderModule::class,
    UtilsModule::class*/
])
interface ApplicationComponent {
    fun inject(application: NewsApp)
//    fun inject(pushNotificationReceiverService: PushNotificationReceiverService)
//
//    fun plus(takePhotoActivityModule: TakePhotoActivityModule): TakePhotoActivityComponent
//    fun plus(viewTakenPhotoActivityModule: ViewTakenPhotoActivityModule): ViewTakenPhotoActivityComponent
//    fun plus(photosActivityModule: PhotosActivityModule): PhotosActivityComponent
//    fun plus(uploadPhotoServiceModule: UploadPhotoServiceModule): UploadPhotoServiceComponent
//    fun plus(receivePhotosServiceModule: ReceivePhotosServiceModule): ReceivePhotosServiceComponent
//    fun plus(settingsActivityModule: SettingsActivityModule): SettingsActivityComponent
}