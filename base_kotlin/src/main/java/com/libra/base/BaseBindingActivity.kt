package com.libra.base

import android.app.ProgressDialog
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.libra.R
import com.libra.api.ApiObservable
import com.libra.view.LoadingDialog

/**
 * Created by libra on 2017/7/28.
 */
abstract class BaseBindingActivity<B : ViewDataBinding> : AppCompatActivity() {
    protected val TAG: String = "ID3_UI"
    protected var progressDialog: ProgressDialog? = null
    protected var binding: B? = null
    protected var toolbar: Toolbar? = null
    protected var toolbarTitle: TextView? = null
    protected val observableList: ArrayList<ApiObservable<*>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutID())
        initIntentData()
        initToolBar()
        initXmlModel()
        initCustomView()
    }

    /**
     * 布局文件
     */
    abstract fun getLayoutID(): Int

    /**
     * 初始化intent数据
     */
    abstract fun initIntentData()

    /**
     * 初始化xmlmodel
     */
    abstract fun initXmlModel()

    /**
     * 初始化toolbar
     */
    open fun initToolBar() {
        toolbar = findViewById(R.id.titlebar)
        if (toolbar != null) {
            this.setSupportActionBar(toolbar)
            toolbar!!.setNavigationOnClickListener({ onBackPressed() })
            toolbarTitle = toolbar!!.findViewById(R.id.titleName)
            if (toolbarTitle != null) {
                supportActionBar?.setDisplayShowTitleEnabled(false)
            }
            showBackButton(true)
        }
    }

    fun showBackButton(visible: Boolean) {
        supportActionBar?.setDisplayShowHomeEnabled(visible)
        supportActionBar?.setDisplayHomeAsUpEnabled(visible)
    }

    override fun onTitleChanged(title: CharSequence?, color: Int) {
        super.onTitleChanged(title, color)
        toolbarTitle?.text = title
    }

    /**
     * 按需要初始化一些视图控件
     */
    open fun initCustomView() {
    }

    fun showLoadingDialog() {
        showLoadingDialog("")
    }

    fun showLoadingDialog(message: String) {
        showLoadingDialog(message, true)
    }

    fun showLoadingDialog(cancelable: Boolean) {
        showLoadingDialog("", cancelable)
    }

    fun showLoadingDialog(message: String, cancelable: Boolean) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this, "", message)
            progressDialog?.setCanceledOnTouchOutside(false)
            progressDialog?.setCancelable(cancelable)
        } else {
            when (progressDialog?.isShowing) {
                true -> progressDialog?.dismiss()
            }
            progressDialog?.setMessage(message)
            progressDialog?.show()
        }
    }


    fun showLoadingDialog(timeOut: Int) {
        if (null == progressDialog) {
            progressDialog = LoadingDialog(this, timeOut)
            progressDialog?.setCanceledOnTouchOutside(false)
        }

        progressDialog?.setTitle("")
        if (!progressDialog?.isShowing()!!) progressDialog?.show() //        timeout

    }

    fun closeLoadingDialog() {
        if (isFinishing) {
            return
        }
        when (progressDialog?.isShowing) {
            true -> progressDialog?.dismiss()
        }
    }

    /**
     * 添加接口调用
     */
    fun <T> addObservable(observable: ApiObservable<T>): ApiObservable<T> {
        observableList.add(observable)
        observable.subscribe()
        return observable
    }

    override fun onDestroy() { //取消接口订阅，防止内存泄露
        for (observable in observableList) {
            observable.dispose()
        }
        super.onDestroy()
    }

}
