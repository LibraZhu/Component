package com.libra.app

import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by libra on 2017/12/12.
 */

internal interface ApiService {
    @get:GET("/LibraZhu/Library/blob/master/Switch")
    val readme: Flowable<ResponseBody>


    @FormUrlEncoded
    @POST("api/news/list")
    fun getNewsAllList(
            @Field("p") page: Int,
            @Field("offset") pageSize: Int
    ): Flowable<HttpResponse<NewsResponse>>
}
