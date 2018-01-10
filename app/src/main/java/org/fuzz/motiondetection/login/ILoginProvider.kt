package org.fuzz.motiondetection.login

import io.reactivex.Observable

interface ILoginProvider {

    fun login(username: String, password: String) : Observable<String>

    fun fetchSavedCredentials() : Observable<Pair<String, String>>

    fun saveCredentials(credentials: Pair<String, String>)

}