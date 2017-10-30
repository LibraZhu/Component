package com.libra

import com.libra.base.BaseBindingActivity
import com.libra.databinding.ActivityMainmBinding

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
