package org.fuzz.motiondetection

import android.app.Activity
import android.app.Application
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import org.fuzz.motiondetection.di.AppComponent
import org.fuzz.motiondetection.di.AppModule
import org.fuzz.motiondetection.di.DaggerAppComponent
import javax.inject.Inject


class WebcamApplication : HasActivityInjector, Application() {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    lateinit var component : AppComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .application(this)
                .build()
        component.inject(this)
    }

    fun getAppComponent() : AppComponent {
        return component
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }
}