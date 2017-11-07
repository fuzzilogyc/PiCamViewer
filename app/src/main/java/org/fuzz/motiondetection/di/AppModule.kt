package org.fuzz.motiondetection.di

import android.arch.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import org.fuzz.motiondetection.login.ILoginProvider
import org.fuzz.motiondetection.login.LoginLoginProvider
import org.fuzz.motiondetection.webcamimage.IWebcamImageProvider
import org.fuzz.motiondetection.webcamimage.WebcamImageProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import android.R.raw
import android.content.Context
import org.fuzz.motiondetection.R
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


@Module(subcomponents = arrayOf(ViewModelSubComponent::class))
class AppModule(context: Context) {

    val mContext : Context = context

    @Provides
    @Singleton
    fun provideLoginRepository(repository: LoginLoginProvider): ILoginProvider {
        return repository
    }

    @Singleton
    @Provides
    fun provideViewModelFactory(viewModelSubComponent: ViewModelSubComponent.Builder): ViewModelProvider.Factory {
        return ViewModelFactory(viewModelSubComponent.build())
    }

    @Provides
    @Singleton
    fun provideWebcamImageRepository(repository: WebcamImageProvider): IWebcamImageProvider {
        return repository
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(): Retrofit {
        val certificatePinner = CertificatePinner.Builder()
                .add("fuzzilogyc.ddns.net", "sha256/2LHctiQ4nRY6bH9ovINSxvG4ZmkA5kxxG7Et3ABYcZY=")
                .build()

        val cf = CertificateFactory.getInstance("X.509")
        val cert = mContext.getResources().openRawResource(R.raw.cert)
        val ca: Certificate
        try {
            ca = cf.generateCertificate(cert)
        } finally {
            cert.close()
        }

        // creating a KeyStore containing our trusted CAs
        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", ca)

        // creating a TrustManager that trusts the CAs in our KeyStore
        val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
        tmf.init(keyStore)

        // creating an SSLSocketFactory that uses our TrustManager
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tmf.getTrustManagers(), null)

        val trustManagers = tmf.getTrustManagers()
        if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
            throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers))
        }
        val trustManager = trustManagers[0] as X509TrustManager

        val client = OkHttpClient.Builder()
                .certificatePinner(certificatePinner)
                .sslSocketFactory(sslContext.socketFactory, trustManager)
                .build()

        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://fuzzilogyc.ddns.net:8443/")
                .client(client)
                .build()
    }

}