package org.fuzz.motiondetection.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory : ViewModelProvider.Factory {

    private var mCreators: Map<Class<out ViewModel>, Provider<ViewModel>>

    @Inject
    constructor(creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) {
        mCreators = creators
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var creator = mCreators!!.get(modelClass)
        if (creator == null) {
            for (entry in mCreators!!.entries) {
                if (modelClass.isAssignableFrom(entry.key)) {
                    creator = entry.value
                    break
                }
            }
        }
        if (creator == null) {
            throw IllegalArgumentException("unknown viewmodel class " + modelClass)
        }
        try {
            return creator!!.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

}