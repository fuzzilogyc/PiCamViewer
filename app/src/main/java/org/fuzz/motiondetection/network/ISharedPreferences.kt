package org.fuzz.motiondetection.network

interface ISharedPreferences {

    fun save(username: String, password: String)
    fun get() : Pair<String, String>

}