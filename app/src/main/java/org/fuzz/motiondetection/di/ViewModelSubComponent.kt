package org.fuzz.motiondetection.di

import org.fuzz.motiondetection.webcamimage.WebcamImageViewModel
import org.fuzz.motiondetection.login.LoginViewModel

import dagger.Subcomponent

@Subcomponent
interface ViewModelSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): ViewModelSubComponent
    }

    fun loginViewModel(): LoginViewModel
    fun webcamImageViewModel(): WebcamImageViewModel
}