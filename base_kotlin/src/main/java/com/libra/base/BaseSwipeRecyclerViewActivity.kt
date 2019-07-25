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
import com.libra.superrecyclerview.swipe.SwipeLayout

/**
 * Created by libra on 2017/8/22.
 */
abstract class BaseSwipeRecyclerViewActivity<B : ViewDataBinding> : BaseBindingActivity<B>() {

    private var baseAdapter: SwipeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        baseAdapter = initSwipeAdapter()
        getRecyclerView().adapter = WrapperAdapter(this, baseAdapter)
        if (isCanRefresh()) {
            getRecyclerView().setRefreshListener { onRefresh() }
        }
        if (isCanLoadMore()) {
            getRecyclerView().setOnMoreListener { overallItemsCount, itemsBeforeMore, maxLastVisiblePosition ->
                onLoadMore()
            }
        }
    }


    open fun initSwipeAdapter(): SwipeAdapter {
        return object : SwipeAdapter() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeViewHolder {
                return object : SwipeViewHolder(DataBindingUtil.inflate(
                        LayoutInflater.from(this@BaseSwipeRecyclerViewActivity), getItemLayoutID(),
                        parent, false)) {
                    init {

                        initViewHolderView(getBinding(),swipeLayout!!)
                    }
                    override fun initXmlModel(any: Any?): BaseXmlModel {
                        return initItemXmlModel(any, getBinding())
                    }
                }
            }
        }
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
    fun getBaseAdapter(): SwipeAdapter {
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

    abstract fun initItemXmlModel(item: Any?, binding: ViewDataBinding): BaseXmlModel

    open fun initViewHolderView(binding: ViewDataBinding, swipeLayout: SwipeLayout) {}
}