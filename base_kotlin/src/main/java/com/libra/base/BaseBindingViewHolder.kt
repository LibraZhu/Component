package com.libra.base

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import com.libra.BR

/**
 * Created by libra on 2017/6/15
 */

abstract class BaseBindingViewHolder(
        private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    /**
     * 绑定数据
     *
     * @param obj obj
     */
    open fun bindViewHolder(obj: Any?) {
        binding.setVariable(BR.xmlmodel, initXmlModel(obj))
        binding.executePendingBindings()
    }

    open fun getBinding(): ViewDataBinding = binding

    abstract fun initXmlModel(any: Any?): BaseXmlModel
}
