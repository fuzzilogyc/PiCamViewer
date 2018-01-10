package org.fuzz.motiondetection.webcamimage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Observable
import okhttp3.ResponseBody
import org.fuzz.motiondetection.network.IRetrofit
import org.fuzz.motiondetection.network.WebcamImageService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


class WebcamImageProvider : IWebcamImageProvider {

    private val mRetroFit : IRetrofit

    @Inject
    constructor(retrofit: IRetrofit) {
        mRetroFit = retrofit
    }

    override fun fetchWebcamImage(token : String) : Observable<Bitmap> {
        return Observable.create<Bitmap> { emitter ->
            val service = mRetroFit.getRetrofitInstance().create<WebcamImageService>(WebcamImageService::class.java)
            service.get(token).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.code() == 401) {
                        emitter.onError(Throwable("Login required"))
                        return
                    }
                    if (!response.isSuccessful) {
                        emitter.onError(IOException("Unexpected code " + response))
                        return
                    }
                    // TODO check for a "needs login" response
                    val inputStream = response.body()!!.byteStream()
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    if (bitmap == null) {
                        // Not able to decode bitmap - must've received non-bitmap data
                        emitter.onError(Throwable(response.message() + ":" + response.errorBody()))
                        return
                    }
                    emitter.onNext(bitmap)
                    emitter.onComplete()
                }

                override fun onFailure(call: Call<ResponseBody>, e: Throwable) {
                    e.printStackTrace()
                    emitter.onError(e)
                }
            })
        }
    }

}