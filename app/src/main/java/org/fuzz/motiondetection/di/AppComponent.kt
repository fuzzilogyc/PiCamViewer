package org.fuzz.motiondetection.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import org.fuzz.motiondetection.WebcamApplication
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class, ActivityModule::class, AppModule::class))
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: WebcamApplication): Builder

        fun build(): AppComponent
    }

    fun inject(app: WebcamApplication)

}