package com.libra.utils


import io.reactivex.Flowable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

/**
 * Created by libra on 16/3/30 下午4:40.
 */
class RxBus private constructor() {
    private val bus: FlowableProcessor<Any> = PublishProcessor.create<Any>().toSerialized()


    // 提供了一个新的事件
    fun post(o: Any) {
        bus.onNext(o)
    }


    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    fun <T> toObserverable(eventType: Class<T>): Flowable<*> {
        return bus.ofType(eventType)
    }

    fun hasSubscribers(): Boolean {
        return bus.hasSubscribers()
    }

    private object SingletonHolder {
        internal val INSTANCE = RxBus()
    }

    companion object {
        val default: RxBus
            get() = SingletonHolder.INSTANCE
    }
}
