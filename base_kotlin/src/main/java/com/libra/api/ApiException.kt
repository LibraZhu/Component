package com.libra.api

import android.text.TextUtils
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.charset.Charset

/**
 * Created by libra on 2017/8/1.
 */

class ApiException : Exception {
    var code: Int = 0
    var errorResponseMessage: String? = null
    private var response: Response<*>? = null

    override fun getLocalizedMessage(): String {
        if (!TextUtils.isEmpty(errorResponseMessage)) {
            return errorResponseMessage!!
        }
        return super.getLocalizedMessage()
    }

    private constructor(throwable: Throwable) : super(throwable) {}

    private constructor(code: Int, message: String?) : super(message ?: "") {
        this.code = code
    }

    private constructor(code: Int, message: String?, response: Response<*>?) : super(message
            ?: "") {
        this.code = code
        this.response = response
        try {
            val json = response?.errorBody()?.source()?.readString(Charset.forName("UTF-8"))
            if (!TextUtils.isEmpty(json)) {
                val jsonObj = JSONObject(json)
                if (jsonObj.has("status_code")) {
                    this.code = jsonObj.optInt("status_code")
                }
                if (jsonObj.has("message")) {
                    this.errorResponseMessage = jsonObj.optString("message")
                }
            }
        } catch (e: Throwable) {

        }
    }

    companion object {
        private val serialVersionUID = -5967830393343122572L

        fun error(code: Int): ApiException {
            val errorMessage: String
            if (ERROR_HTTP_CONNECT_TIMEOUT == code) {
                errorMessage = "连接超时，请检查网络"
            } else if (ERROR_HTTP_HOST_UNKNOWN == code) {
                errorMessage = "连接失败，请检查网络"
            } else if (ERROR_HTTP_CONNECT == code) {
                errorMessage = "连接错误，请检查网络"
            } else {
                errorMessage = "连接异常，请检查网络"
            }
            return ApiException(code, errorMessage)
        }

        fun error(code: Int, message: String?): ApiException {
            return ApiException(code, message)
        }

        fun error(code: Int, message: String?, response: Response<*>?): ApiException {
            return ApiException(code, message, response)
        }

        fun error(throwable: Throwable): ApiException {
            if (throwable is SocketTimeoutException) {
                return error(ERROR_HTTP_CONNECT_TIMEOUT)
            } else if (throwable is UnknownHostException || throwable is ConnectException) {
                return error(ERROR_HTTP_HOST_UNKNOWN)
            } else if (throwable is HttpException) {
                return error(throwable.code(), throwable.message, throwable.response())
            }
            return ApiException(throwable)
        }

        val ERROR_HTTP_CONNECT = 100
        val ERROR_HTTP_CONNECT_TIMEOUT = 101
        val ERROR_HTTP_HOST_UNKNOWN = 102
    }
}
