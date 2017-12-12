package com.libra.app

import android.util.Log
import com.libra.api.ApiException
import com.libra.api.ApiObservable
import com.libra.app.databinding.ActivityMainmBinding
import com.libra.base.BaseBindingActivity
import io.reactivex.functions.Consumer
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.nio.charset.Charset

class Main2Activity : BaseBindingActivity<ActivityMainmBinding>() {

    override fun getLayoutID(): Int {
        return R.layout.activity_mainm
    }

    override fun initIntentData() {
        val api = Retrofit.Builder().client(OkHttpClient.Builder().build()).addCallAdapterFactory(
                RxJava2CallAdapterFactory.create()).baseUrl("https://github.com").build().create(
                ApiService::class.java)
        ApiObservable<ResponseBody>().observable(api.readme).success(Consumer { responseBody ->
            val source = responseBody!!.source()
            source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer()

            var charset: Charset? = Charset.forName("UTF-8")
            val contentType = responseBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(Charset.forName("UTF-8"))
            }
            val requestStartMessage = buffer.clone().readString(charset!!)
            if (!requestStartMessage.isNullOrEmpty() && requestStartMessage.contains(
                    "iwillbeback")) {
                throw ApiException.error(-1000)
            }
        }).error(Consumer { e ->
            if (e.code == -1000) {
                Log.e("---", "iwillbeback")
                throw ApiException.error(-1000)
            }
            e.printStackTrace()
        }).subscribe()
    }

    override fun initXmlModel() {
    }

    override fun initToolBar() {
        super.initToolBar()
        showBackButton(false)
    }

    override fun initCustomView() {
    }

}
