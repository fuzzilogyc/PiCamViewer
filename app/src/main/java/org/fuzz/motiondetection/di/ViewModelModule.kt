package org.fuzz.motiondetection.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.fuzz.motiondetection.webcamimage.WebcamImageViewModel
import org.fuzz.motiondetection.login.LoginViewModel

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WebcamImageViewModel::class)
    abstract fun bindWebcamImageViewModel(webcamImageViewModel: WebcamImageViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}