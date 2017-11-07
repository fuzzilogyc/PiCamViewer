package org.fuzz.motiondetection.login

import io.reactivex.Observable

interface ILoginProvider {

    fun login(username: String, password: String) : Observable<String>

}