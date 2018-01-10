package org.fuzz.motiondetection.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.fuzz.motiondetection.login.LoginActivity
import org.fuzz.motiondetection.webcamimage.WebcamImageActivity


@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun contributeWebcamImageActivity(): WebcamImageActivity

}