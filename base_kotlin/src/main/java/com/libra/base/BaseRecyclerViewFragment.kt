package com.libra.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.libra.superrecyclerview.SuperRecyclerView
import com.libra.superrecyclerview.WrapperAdapter

/**
 * Created by libra on 2017/8/22.
 */
abstract class BaseRecyclerViewFragment<B : ViewDataBinding> : BaseBindingFragment<B>() {

    private var baseAdapter: BaseAdapter? = null
    protected var viewHolder: BaseBindingViewHolder? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
    }

    /**
     * 初始化列表视图
     */
    open fun initRecycleView() {
        getRecyclerView().setLayoutManager(initLayoutManager())
        val decoration: RecyclerView.ItemDecoration? = initItemDecoration()
        if (decoration != null) {
            getRecyclerView().addItemDecoration(decoration)
        }
        baseAdapter = initBaseAdapter()
        getRecyclerView().adapter = WrapperAdapter(context, baseAdapter)
        if (isCanRefresh()) {
            getRecyclerView().setRefreshListener { onRefresh() }
        }
        if (isCanLoadMore()) {
            getRecyclerView().setOnMoreListener { overallItemsCount, itemsBeforeMore, maxLastVisiblePosition ->
                onLoadMore()
            }
        }
    }


    open fun initBaseAdapter(): BaseAdapter {
        return Adapter(getItemLayoutID())
    }


    /**
     * 添加列表头部视图
     */
    fun addHeaderView(headerView: View) {
        if (getRecyclerView().adapter == null) {
            throw NullPointerException("setAdapter must be called first!")
        }
        (getRecyclerView().adapter as WrapperAdapter).addHeaderView(headerView)
    }

    /**
     * 添加列表底部视图
     */
    fun addFooterView(footerView: View) {
        if (getRecyclerView().adapter == null) {
            throw NullPointerException("setAdapter must be called first!")
        }
        (getRecyclerView().adapter as WrapperAdapter).addFooterView(footerView)
    }

    /**
     * 获取BaseAdapter
     */
    fun getBaseAdapter(): BaseAdapter {
        return baseAdapter!!
    }

    /**
     * 刷新控制开关
     */
    open fun isCanRefresh(): Boolean {
        return false
    }

    /**
     * 上拉加载开关
     */
    open fun isCanLoadMore(): Boolean {
        return false
    }

    /**
     * 获取列表间隔线
     */
    open fun initItemDecoration(): RecyclerView.ItemDecoration? {
        return null
    }

    /**
     * 刷新回调
     */
    open fun onRefresh() {

    }

    /**
     * 下拉加载回调
     *
     */
    open fun onLoadMore() {

    }

    /**
     * 获取设置的LayoutManager
     */
    abstract fun initLayoutManager(): RecyclerView.LayoutManager

    /**
     * 获取RecycleView
     */
    abstract fun getRecyclerView(): SuperRecyclerView

    abstract fun getItemLayoutID(): Int

    abstract fun initItemXmlModel(item: Any?, binding: ViewDataBinding, position: Int): BaseXmlModel
    open fun initViewHolderView(binding: ViewDataBinding) {}

    inner class Adapter(var layoutID: Int) : BaseAdapter() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BaseBindingViewHolder {
            return object : BaseBindingViewHolder(
                    DataBindingUtil.inflate(LayoutInflater.from(context), layoutID, p0, false)
            ) {
                init {
                    initViewHolderView(getBinding())
                }

                override fun initXmlModel(any: Any?, position: Int): BaseXmlModel {
                    return initItemXmlModel(any, getBinding(), position)
                }

            }
        }
    }
}