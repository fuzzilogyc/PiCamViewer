package org.fuzz.motiondetection.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.util.ArrayMap
import org.fuzz.motiondetection.webcamimage.WebcamImageViewModel
import org.fuzz.motiondetection.login.LoginViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelFactory : ViewModelProvider.Factory {

    private var mCreators: ArrayMap<Class<out ViewModel>, () -> ViewModel> = ArrayMap()

    @Inject
    constructor(viewModelSubComponent : ViewModelSubComponent) {
        // View models cannot be injected directly because they won't be bound to the owner's
        // view model scope.
        mCreators.put(LoginViewModel::class.java, { viewModelSubComponent.loginViewModel() } )
        mCreators.put(WebcamImageViewModel::class.java, { viewModelSubComponent.webcamImageViewModel() })
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var creator = mCreators.get(modelClass)
        if (creator == null) {
            for (entry in mCreators.entries) {
                if (modelClass.isAssignableFrom(entry.key)) {
                    creator = entry.value
                    break
                }
            }
        }
        if (creator == null) {
            throw IllegalArgumentException("Unknown model class " + modelClass)
        }
        try {
            return creator.invoke() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

}