package com.libra.utils

import android.app.Application
import android.content.pm.ApplicationInfo
import com.libra.api.ApiException
import com.libra.api.ApiObservable
import com.libra.api.ApiService
import io.reactivex.functions.Consumer
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.Long
import java.nio.charset.Charset

/**
 * Created by libra on 2017/8/12.
 */

object Utils {
    fun isDebug(): Boolean {
        try {
            val application = Class.forName("android.app.ActivityThread").getMethod(
                    "currentApplication").invoke(null) as Application
            if (application != null) {
                return application.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }
}
