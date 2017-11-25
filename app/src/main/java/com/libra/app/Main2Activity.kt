package com.libra.app

import com.libra.app.databinding.ActivityMainmBinding
import com.libra.base.BaseBindingActivity

class Main2Activity : BaseBindingActivity<ActivityMainmBinding>() {

    override fun getLayoutID(): Int {
        return R.layout.activity_mainm
    }

    override fun initIntentData() {
    }

    override fun initXmlModel() {
    }

    override fun initToolBar() {
        super.initToolBar()
        showBackButton(false)
    }

    override fun initCustomView() {
        showLoadingDialog()
    }

}
