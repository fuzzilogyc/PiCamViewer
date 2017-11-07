package org.fuzz.motiondetection.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface WebcamImageService {
    @GET("/")
    fun get(@Header("Authorization") authorization: String): Call<ResponseBody>
}