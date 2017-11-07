package org.fuzz.motiondetection.login

import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import org.fuzz.motiondetection.network.JsonWebToken
import org.fuzz.motiondetection.network.LoginService
import retrofit2.Retrofit
import javax.inject.Inject


class LoginLoginProvider : ILoginProvider {

    private val mRetroFit: Retrofit

    @Inject
    constructor(retrofit: Retrofit) {
        mRetroFit = retrofit
    }

    override fun login(username: String, password: String) : Observable<String> {
        return Observable.create<String> { emitter ->
            try {
                networkLogin(username, password, emitter)
            } catch (e: Exception) {
                Log.d("LoginLoginProvider", "problem with networkLogin", e)
                emitter.onError(e)
            }
        }
    }

    private fun networkLogin(username: String, password: String, emitter : ObservableEmitter<String>) {
        val service = mRetroFit.create<LoginService>(LoginService::class.java)
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