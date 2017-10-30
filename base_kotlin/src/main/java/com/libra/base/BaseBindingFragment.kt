package com.dooya.id3.module.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.libra.api.ApiObservable
import com.libra.base.BaseBindingActivity

/**
 * Created by libra on 2017/8/11.
 */
abstract class BaseBindingFragment<B : ViewDataBinding> : Fragment() {

    protected var binding: B? = null
    protected val observalbelList: ArrayList<ApiObservable<*>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initIntentData()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), getLayoutID(), container,
                                          false)
        initXmlModel()
        initCustomView()
        return binding?.root
    }

    /**
     * 初始化intent数据
     */
    open fun initIntentData() {

    }

    /**
     * 获取布局文件
     */
    abstract fun getLayoutID(): Int

    /**
     * 初始化布局文件对应的xmlmodel用于databinding
     */
    abstract fun initXmlModel()

    /**
     * 按需要初始化一些视图控件
     */
    open fun initCustomView() {

    }

    fun showLoadingDialog() {
        showLoadingDialog("")
    }

    fun showLoadingDialog(message: String) {
        if (isAdded && activity != null) {
            if (activity is BaseBindingActivity<*>) {
                (activity as BaseBindingActivity<*>).showLoadingDialog(message)
            }
        }
    }

    fun closeLoadingDialog() {
        if (isAdded && activity != null) {
            if (activity is BaseBindingActivity<*>) {
                (activity as BaseBindingActivity<*>).closeLoadingDialog()
            }
        }
    }

    /**
     * 添加接口调用,生命周期控制ApiObservable
     */
    fun <T> addObservable(observable: ApiObservable<T>): ApiObservable<T> {
        observalbelList.add(observable)
        observable.subscribe()
        return observable
    }

    override fun onDestroyView() {
        //取消接口订阅，防止内存泄露
        for (observable in observalbelList) {
            observable.dispose()
        }
        super.onDestroyView()
    }
}