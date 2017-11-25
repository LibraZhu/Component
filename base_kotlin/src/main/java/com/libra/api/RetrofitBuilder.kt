package com.libra.api

import android.content.Context
import com.libra.utils.JSONUtils
import com.libra.utils.Utils
import com.orhanobut.logger.Logger
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.EOFException
import java.io.IOException
import java.net.URLDecoder
import java.nio.charset.Charset
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * Created by libra on 2017/8/1.
 */

object RetrofitBuilder {
    private val DEFAULT_TIMEOUT = 30

    fun build(url: String, timeout: Int): Retrofit {
        var timeout = timeout
        if (timeout <= 0) {
            timeout = DEFAULT_TIMEOUT
        }
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(timeout.toLong(), TimeUnit.SECONDS).readTimeout(timeout.toLong(),
                                                                               TimeUnit.SECONDS) //添加测试log
        if (Utils.getBuildConfig()) {
            builder.addInterceptor(object : Interceptor {
                @Throws(IOException::class) override fun intercept(
                        chain: Interceptor.Chain): Response {
                    val request = chain.request()
                    var requestStartMessage = "" + request.method() + ' ' + request.url()
                    val requestBody = request.body()
                    val hasRequestBody = requestBody != null
                    if (hasRequestBody) {
                        val buffer = Buffer()
                        requestBody!!.writeTo(buffer)
                        var charset: Charset? = Charset.forName("UTF-8")
                        val contentType = requestBody.contentType()
                        if (contentType != null) {
                            charset = contentType.charset(Charset.forName("UTF-8"))
                        }
                        if (contentType != null && contentType.toString().contains(
                                "multipart/form-data")) {
                        } else {
                            if (isPlaintext(buffer)) {
                                requestStartMessage += "\n--> request:" + buffer.readString(charset)
                            }
                        }
                    }
                    val response: Response
                    try {
                        response = chain.proceed(request)
                    } catch (e: Exception) {
                        throw e
                    }

                    //下面打印日志，只捕获不抛异常
                    try {
                        val responseBody = response.body()
                        val source = responseBody!!.source()
                        source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
                        val buffer = source.buffer()

                        var charset: Charset? = Charset.forName("UTF-8")
                        val contentType = responseBody.contentType()
                        if (contentType != null) {
                            charset = contentType.charset(Charset.forName("UTF-8"))
                        }
                        if (isPlaintext(buffer)) {
                            requestStartMessage += "\n--> response:" + buffer.clone().readString(
                                    charset!!)
                            Logger.d(URLDecoder.decode(requestStartMessage, "UTF-8"))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    return response
                }

                internal fun isPlaintext(buffer: Buffer): Boolean {
                    try {
                        val prefix = Buffer()
                        val byteCount = if (buffer.size() < 64) buffer.size() else 64
                        buffer.copyTo(prefix, 0, byteCount)
                        for (i in 0..15) {
                            if (prefix.exhausted()) {
                                break
                            }
                            val codePoint = prefix.readUtf8CodePoint()
                            if (Character.isISOControl(codePoint) && !Character.isWhitespace(
                                    codePoint)) {
                                return false
                            }
                        }
                        return true
                    } catch (e: EOFException) {
                        return false // Truncated UTF-8 sequence.
                    }

                }
            })
        }
        return Retrofit.Builder().client(builder.build()).addConverterFactory(
                GsonConverterFactory.create(JSONUtils.gson)).addCallAdapterFactory(
                RxJava2CallAdapterFactory.create()).baseUrl(url).build()
    }

    fun buildHttps(url: String, timeout: Int, context: Context, resId: Int): Retrofit {
        var timeout = timeout
        if (timeout <= 0) {
            timeout = DEFAULT_TIMEOUT
        }
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(timeout.toLong(), TimeUnit.SECONDS).readTimeout(timeout.toLong(),
                                                                               TimeUnit.SECONDS)
        sslSocketFactory(builder, context, resId)
        Logger.i("api url:" + url)
        return Retrofit.Builder().client(builder.build()).addConverterFactory(
                GsonConverterFactory.create()).addCallAdapterFactory(
                RxJava2CallAdapterFactory.create()).baseUrl(url).build()
    }

    private fun sslSocketFactory(builder: OkHttpClient.Builder, context: Context, resId: Int) {
        try {
            val x509TrustManager = getX509TrustManager(context, "BKS", resId)
            builder.sslSocketFactory(getSSLSocketFactory(arrayOf(x509TrustManager)),
                                     x509TrustManager)
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

    }


    @Throws(CertificateException::class, KeyStoreException::class, IOException::class,
            NoSuchAlgorithmException::class,
            KeyManagementException::class) private fun getX509TrustManager(context: Context,
                                                                           keyStoreType: String?,
                                                                           keystoreResId: Int): X509TrustManager {
        var keyStoreType = keyStoreType
        val cf = CertificateFactory.getInstance("X.509")
        val caInput = context.resources.openRawResource(keystoreResId)
        val ca = cf.generateCertificate(caInput)
        caInput.close()
        if (keyStoreType == null || keyStoreType.length == 0) {
            keyStoreType = KeyStore.getDefaultType()
        }
        val keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", ca)
        val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
        tmf.init(keyStore)
        return tmf.trustManagers[0] as X509TrustManager
    }


    @Throws(NoSuchAlgorithmException::class,
            KeyManagementException::class) private fun getSSLSocketFactory(
            trustManagers: Array<TrustManager>): SSLSocketFactory {
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustManagers, null)
        return sslContext.socketFactory
    }
}
