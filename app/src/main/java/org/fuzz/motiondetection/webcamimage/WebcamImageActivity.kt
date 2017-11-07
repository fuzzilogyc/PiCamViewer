package org.fuzz.motiondetection.webcamimage

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
import android.text.TextUtils
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.fuzz.motiondetection.R
import org.fuzz.motiondetection.WebcamApplication
import org.fuzz.motiondetection.login.LoginActivity
import javax.inject.Inject


class WebcamImageActivity : AppCompatActivity() {

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: WebcamImageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val token = if (intent.hasExtra("token")) {
            intent.extras["token"] as String
        } else {
            ""
        }

        (application as WebcamApplication).getAppComponent().inject(this)
        viewModel = ViewModelProviders.of(this, mViewModelFactory).get(WebcamImageViewModel::class.java)

        (findViewById<SwipeRefreshLayout>(R.id.swiperefresh)).isRefreshing = true
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swiperefresh)
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshImage(token)
        }

        viewModel.getWebcamImageState().observe(this, object : Observer<String> {
            override fun onChanged(message: String?) {
                (findViewById<SwipeRefreshLayout>(R.id.swiperefresh)).isRefreshing = false
                if (TextUtils.equals("Login required", message)) {
                    startLogin()
                } else {
                    errorTextView.text = message
                    errorTextView.visibility = View.VISIBLE
                }
            }
        })

        viewModel.getWebcamImage(token).observe(this, object : Observer<Bitmap> {
            override fun onChanged(bitmap: Bitmap?) {
                if (bitmap == null) {
                    errorTextView.text = getString(R.string.null_bitmap_error_text)
                    errorTextView.visibility = View.VISIBLE
                } else {
                    errorTextView.visibility = View.GONE
                    imageView.setImageBitmap(bitmap)
                    (findViewById<SwipeRefreshLayout>(R.id.swiperefresh)).isRefreshing = false
                }
            }
        })
    }

    private fun startLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}
