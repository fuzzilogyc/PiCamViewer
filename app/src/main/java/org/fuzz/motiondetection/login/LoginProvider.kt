package org.fuzz.motiondetection.login

import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import org.fuzz.motiondetection.network.IRetrofit
import org.fuzz.motiondetection.network.ISharedPreferences
import org.fuzz.motiondetection.network.JsonWebToken
import org.fuzz.motiondetection.network.LoginService
import javax.inject.Inject


class LoginProvider : ILoginProvider {

    private val mRetroFit: IRetrofit
    private val mSharedPreferences: ISharedPreferences

    @Inject
    constructor(retrofit: IRetrofit, sharedPreferences: ISharedPreferences) {
        mRetroFit = retrofit
        mSharedPreferences = sharedPreferences
    }

    override fun login(username: String, password: String) : Observable<String> {
        return Observable.create<String> { emitter ->
            try {
                networkLogin(username, password, emitter)
            } catch (e: Exception) {
                Log.d("LoginProvider", "problem with networkLogin", e)
                emitter.onError(e)
            }
        }
    }

    override fun fetchSavedCredentials(): Observable<Pair<String, String>> {
        return Observable.create<Pair<String, String>> { emitter ->
            emitter.onNext(mSharedPreferences.get())
        }
    }

    override fun saveCredentials(credentials: Pair<String, String>) {
        mSharedPreferences.save(credentials.first, credentials.second)
    }

    private fun networkLogin(username: String, password: String, emitter : ObservableEmitter<String>) {
        val service = mRetroFit.getRetrofitInstance().create<LoginService>(LoginService::class.java)
        service.login(username, password).enqueue(object : retrofit2.Callback<JsonWebToken> {
            override fun onResponse(call: retrofit2.Call<JsonWebToken>, response: retrofit2.Response<JsonWebToken>) {
                if (response.code() == 200) {
                    val token = response.body()!!.token
                    emitter.onNext(token)
                } else {
                    emitter.onError(Throwable("Stink"))
                }
            }

            override fun onFailure(call: retrofit2.Call<JsonWebToken>, e: Throwable) {
                e.printStackTrace()
                emitter.onError(e)
            }
        })
    }

}