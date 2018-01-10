package org.fuzz.motiondetection.di

import android.content.Context
import dagger.Binds
import dagger.Module
import org.fuzz.motiondetection.WebcamApplication
import org.fuzz.motiondetection.login.ILoginProvider
import org.fuzz.motiondetection.login.LoginProvider
import org.fuzz.motiondetection.network.IRetrofit
import org.fuzz.motiondetection.network.ISharedPreferences
import org.fuzz.motiondetection.network.RetrofitWrapper
import org.fuzz.motiondetection.network.SharedPreferencesImpl
import org.fuzz.motiondetection.webcamimage.IWebcamImageProvider
import org.fuzz.motiondetection.webcamimage.WebcamImageProvider

@Module(includes = arrayOf(ViewModelModule::class))
abstract class AppModule {

    @Binds
    abstract fun provideLoginRepository(repository: LoginProvider): ILoginProvider

    @Binds
    abstract fun provideWebcamImageRepository(repository: WebcamImageProvider): IWebcamImageProvider

    @Binds
    abstract fun provideSharedPreferences(sharedPreferences: SharedPreferencesImpl): ISharedPreferences

    @Binds
    abstract fun provideRetrofit(retrofit: RetrofitWrapper): IRetrofit

    @Binds
    abstract fun provideApplication(application: WebcamApplication): Context

}