package com.libra.app

import com.libra.api.ApiException
import com.libra.api.ApiObservable
import io.reactivex.Flowable
import kotlin.properties.Delegates


/**
 * Created by libra on 2017/10/30.
 */
class Api private constructor() {
    private var mApiService: ApiService by Delegates.notNull()

    init {
        val retrofit = RetrofitClient.getInstance()
        mApiService = retrofit.create(ApiService::class.java)
    }


    private object SingletonHolder {
        internal val INSTANCE = Api()
    }

    companion object {
        fun getInstance(): Api {
            return SingletonHolder.INSTANCE
        }
    }

    /**
     * 过滤成功 错误码
     *
     * @param flowable 原始接口flowable
     * @param <T> 解析对象
     * @return 转化后的flowable
     */
    private fun <T : BaseResponse> checkSuccess(flowable: Flowable<T>): Flowable<T> {
        return flowable.map { t ->
            if (t.isSuccess) {
                t
            } else {
                throw ApiException.error(t.code, t.msg)
            }
        }
    }


    fun getNewsList(page: Int, pageSize: Int): ApiObservable<NewsResponse> {
        return ApiObservable<NewsResponse>().observable(
                checkSuccess(mApiService.getNewsAllList(page, pageSize)).map { t ->
                    if (t.data == null) {
                        t.data = NewsResponse()
                    }
                    return@map t.data
                })
    }

}