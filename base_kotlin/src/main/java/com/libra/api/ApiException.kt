package com.libra.api

import retrofit2.HttpException
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by libra on 2017/8/1.
 */

class ApiException : Exception {
    var code: Int = 0

    private constructor(throwable: Throwable) : super(throwable) {}

    private constructor(code: Int, message: String?) : super(message ?: "") {
        this.code = code
    }

    companion object {
        private val serialVersionUID = -5967830393343122572L

        fun error(code: Int): ApiException {
            val errorMessage: String
            if (ERROR_HTTP_CONNECT_TIMEOUT == code) {
                errorMessage = "Connect Timeout"
            } else if (ERROR_HTTP_HOST_UNKNOWN == code) {
                errorMessage = "Connect Host Unkown"
            } else if (ERROR_HTTP_CONNECT == code) {
                errorMessage = "Connect Error"
            } else {
                errorMessage = "UnKnown Exception"
            }
            return ApiException(code, errorMessage)
        }

        fun error(code: Int, message: String?): ApiException {
            return ApiException(code, message)
        }

        fun error(throwable: Throwable): ApiException {
            if (throwable is SocketTimeoutException) {
                return error(ERROR_HTTP_CONNECT_TIMEOUT)
            } else if (throwable is UnknownHostException || throwable is ConnectException) {
                return error(ERROR_HTTP_HOST_UNKNOWN)
            } else if (throwable is HttpException) {
                return error(ERROR_HTTP_CONNECT, throwable.message)
            }
            return ApiException(throwable)
        }

        val ERROR_HTTP_CONNECT = 100
        val ERROR_HTTP_CONNECT_TIMEOUT = 101
        val ERROR_HTTP_HOST_UNKNOWN = 102
    }
}
