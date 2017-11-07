package org.fuzz.motiondetection.webcamimage

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class WebcamImageViewModel @Inject constructor(provider: IWebcamImageProvider) : ViewModel() {

    private val mWebcamImageProvider: IWebcamImageProvider = provider

    private var webcamImageState = MutableLiveData<String>()
    private var webcamImage = MutableLiveData<Bitmap>()

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getWebcamImageState() : LiveData<String> {
        return webcamImageState
    }

    fun getWebcamImage(token: String): LiveData<Bitmap> {
        refreshImage(token)
//        webcamImage.value = null
        return webcamImage
    }

    fun refreshImage(token : String)  {
        val disposable = mWebcamImageProvider.fetchWebcamImage(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                { bitmap ->
                    // on next
                    webcamImage.value = bitmap
                    webcamImageState.value = "All good"
                },
                { error ->
                    // on error
                    webcamImageState.value = error.message
                }
        )

        compositeDisposable.add(disposable)
    }

}