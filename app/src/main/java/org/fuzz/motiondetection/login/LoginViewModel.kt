package org.fuzz.motiondetection.login

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class LoginViewModel : ViewModel {

    private val mLoginProvider: ILoginProvider

    var mIsSaveCredentialsCheckboxTicked = false

    var loginResult = MutableLiveData<String>()

    //TODO: change to single live event
    var fetchSavedCredentialsResult = MutableLiveData<Pair<String, String>>()

    private val compositeDisposable = CompositeDisposable()

    @Inject
    constructor(provider: ILoginProvider) {
        mLoginProvider = provider
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear();
    }

    fun fetchLoginCredentials() {
        val disposable = mLoginProvider.fetchSavedCredentials()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            // on next
                            fetchSavedCredentialsResult.value = result
                        },
                        { error ->
                            // nothing to do on error
                        }
                )

        compositeDisposable.add(disposable)
    }

    fun attemptLogin(username: String, password: String) {
        val disposable = mLoginProvider.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { token ->
                            loginResult.value = token
                            if (mIsSaveCredentialsCheckboxTicked) {
                                saveLoginCredentials(username, password)
                            }
                        },
                        { error ->
                            loginResult.value = "error"
                        }
                )

        compositeDisposable.add(disposable)
    }

    fun saveLoginCredentials(username: String, password: String) {
        // Fire and forget
        mLoginProvider.saveCredentials(Pair(username, password))
    }

}