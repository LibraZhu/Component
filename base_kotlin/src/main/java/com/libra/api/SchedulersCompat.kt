package com.libra.api

import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by libra on 2017/8/1.
 */

object SchedulersCompat {
    fun <T> ioTransformer(): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> computationTransformer(): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> newThreadTransformer(): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> trampolineTransformer(): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.trampoline()).observeOn(AndroidSchedulers.mainThread())
        }
    }
}
