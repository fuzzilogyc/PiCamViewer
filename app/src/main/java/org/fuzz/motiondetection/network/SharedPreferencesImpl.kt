package org.fuzz.motiondetection.network

import android.content.Context
import javax.inject.Inject

class SharedPreferencesImpl : ISharedPreferences {

    companion object {
        const val SHARED_PREFERENCES = "org.fuzz.motiondetection.sharedprefs"
        const val SAVED_USERNAME = "saved_username"
        const val SAVED_PASSWORD = "saved_password"
    }

    private val mContext: Context

    @Inject
    constructor(context: Context) {
        mContext = context
    }

    override fun save(username: String, password: String) {
        mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit()
                .putString(SAVED_USERNAME, username)
                .putString(SAVED_PASSWORD, password)
                .apply()
    }

    override fun get(): Pair<String, String> {
        val sharedPreferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(SAVED_USERNAME, "")
        val password = sharedPreferences.getString(SAVED_PASSWORD, "")

        return Pair(username, password)
    }

}