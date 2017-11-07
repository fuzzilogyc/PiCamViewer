package org.fuzz.motiondetection.webcamimage

import android.graphics.Bitmap
import io.reactivex.Observable

interface IWebcamImageProvider {

    fun fetchWebcamImage(token : String) : Observable<Bitmap>

}