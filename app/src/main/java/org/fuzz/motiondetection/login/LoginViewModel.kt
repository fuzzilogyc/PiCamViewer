package org.fuzz.motiondetection.login

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class LoginViewModel : ViewModel {

    private val mLoginProvider : ILoginProvider

    var loginResult = MutableLiveData<String>()

    private val compositeDisposable = CompositeDisposable()

    @Inject
    constructor(provider: ILoginProvider) {
        mLoginProvider = provider
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear();
    }

    fun attemptLogin(username: String, password: String)  {
        val disposable = mLoginProvider.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                { token ->
                    // on next
                    loginResult.value = token
                },
                { error ->
                    // on error
                    loginResult.value = "error"
                }
        )

        compositeDisposable.add(disposable)
    }

}