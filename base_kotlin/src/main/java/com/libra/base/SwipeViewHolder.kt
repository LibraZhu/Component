package com.libra.base

import android.databinding.ViewDataBinding
import com.libra.BR
import com.libra.superrecyclerview.swipe.BaseSwipeAdapter

/**
 * Created by xie on 2017/8/31.
 */
abstract class SwipeViewHolder(
        private val binding: ViewDataBinding) : BaseSwipeAdapter.BaseSwipeableViewHolder(
        binding.root) {
    open fun bindViewHolder(obj: Any?) {
        binding.setVariable(BR.xmlmodel, initXmlModel(obj))
        binding.executePendingBindings()
    }

    open fun initXmlModel(any: Any?): BaseXmlModel? {
        return null
    }

    fun getBinding(): ViewDataBinding {
        return binding
    }
}