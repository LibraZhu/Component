package com.libra.api

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * Created by libra on 2017/8/1.
 */

class ApiObservable<T> {
    private var observable: Flowable<T>? = null
    private val mTransformer = SchedulersCompat.ioTransformer<T>()
    private var onSuccess: Consumer<T>? = null
    private var onError: Consumer<ApiException>? = null
    private var onStatusCodeError: Consumer<ApiException>? = null

    fun observable(observable: Flowable<T>): ApiObservable<T> {
        this.observable = observable
        return this
    }

    /**
     * 订阅
     *
     * @return ApiObservable
     */
    fun subscribe(): Disposable {
        if (observable == null) {
            throw IllegalArgumentException("observable can not be null")
        }
        return this.observable!!.compose(mTransformer).subscribe({ t ->
            if (onSuccess != null) {
                onSuccess!!.accept(
                        t)
            } else {
                Log.w("ApiObservable",
                        "function success() not be called")
            }
        }, { throwable ->
            if (onError != null) {
                if (throwable is ApiException) {
                    onError!!.accept(
                            throwable)
                    onStatusCodeError?.accept(throwable)
                } else {
                    val apiException =
                            ApiException.error(throwable)
                    onError!!.accept(apiException)
                    onStatusCodeError?.accept(apiException)
                }
            }
        })
    }

    /**
     * 成功回调
     *
     * @param onSuccess 回调
     * @return ApiObservable
     */
    fun success(onSuccess: Consumer<T>): ApiObservable<T> {
        this.onSuccess = onSuccess
        return this
    }

    /**
     * 失败回调
     *
     * @param onError 回调
     * @return ApiObservable
     */
    fun error(onError: Consumer<ApiException>): ApiObservable<T> {
        this.onError = onError
        return this
    }

    /**
     * 处理statusCode
     * @param onError 回调
     * @return ApiObservable
     */
    fun statusCodeError(onError: Consumer<ApiException>): ApiObservable<T> {
        this.onStatusCodeError = onError
        return this
    }
}
