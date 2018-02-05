package com.libra.api

import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.http.GET

/**
 * Created by libra on 2017/10/30.
 */

interface ApiService {
    @GET("/LibraZhu/Library/blob/master/Switch") abstract fun readme(): Flowable<ResponseBody>
}
