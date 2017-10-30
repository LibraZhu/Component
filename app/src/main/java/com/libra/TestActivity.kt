package com.libra

import com.libra.base.BaseBindingActivity
import com.libra.databinding.ActivityTestBinding

class TestActivity : BaseBindingActivity<ActivityTestBinding>() {
    override fun initIntentData() {
    }

    override fun initXmlModel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_test
    }


}
