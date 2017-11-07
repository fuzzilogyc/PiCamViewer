package org.fuzz.motiondetection.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import org.fuzz.motiondetection.WebcamApplication
import org.fuzz.motiondetection.webcamimage.WebcamImageActivity
import org.fuzz.motiondetection.login.LoginActivity
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class, AppModule::class))
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun appModule(module: AppModule) : Builder

        fun build(): AppComponent
    }

    fun inject(app: WebcamApplication)
    fun inject(app: WebcamImageActivity)
    fun inject(app: LoginActivity)

}