package com.libra.app

import android.databinding.ViewDataBinding
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.libra.app.databinding.ActivityTestBinding
import com.libra.base.BaseRecyclerViewActivity
import com.libra.base.BaseXmlModel
import com.libra.superrecyclerview.SuperRecyclerView
import com.libra.utils.toast
import io.reactivex.functions.Consumer

class TestActivity : BaseRecyclerViewActivity<ActivityTestBinding>() {
    override fun initIntentData() {
    }

    override fun initXmlModel() {
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_test
    }

    override fun initRecycleView() {
        super.initRecycleView()
        getRecyclerView().setRefreshing(true)
        onRefresh()
    }

    override fun initLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(this)
    }

    override fun getRecyclerView(): SuperRecyclerView {
        return binding?.recyclerView!!
    }

    override fun isCanRefresh(): Boolean {
        return true
    }

    override fun isCanLoadMore(): Boolean {
        return true
    }

    override fun onRefresh() {
        page = 1
        getRecyclerView().setLoadingComplete(false)
        getnews()
    }

    override fun onLoadMore() {
        getnews()
    }

    private var page: Int = 1

    fun getnews() {

        Api.getInstance().getNewsList(page, 10)
                .success(Consumer {
                    if (page == 1) {
                        getBaseAdapter().clear()
                    }
                    getBaseAdapter().append(it.rows ?: ArrayList())
                    if (it.total <= (getBaseAdapter().getData()?.size ?: 0).toLong()) {
                        getRecyclerView().setLoadingComplete(true)
                    }
                    getRecyclerView().setRefreshing(false)
                    page++
                })
                .error(Consumer {
                    toast(it.message)
                    getRecyclerView().setRefreshing(false)
                    getRecyclerView().isLoadingMore = false
                    getRecyclerView().moreProgressView.visibility = View.GONE
                }).subscribe()
    }


    override fun getItemLayoutID(): Int {
        return R.layout.item_test
    }

    override fun initItemXmlModel(item: Any?, binding: ViewDataBinding, position: Int): BaseXmlModel {
        val itemXmlModel = TextItemXmlModel()
        if (item is NewBean) {
            itemXmlModel.text.set(item.title)
        }
        return itemXmlModel
    }


}
