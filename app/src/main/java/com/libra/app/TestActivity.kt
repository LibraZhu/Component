package com.libra.app

import com.libra.app.databinding.ActivityTestBinding
import com.libra.base.BaseBindingActivity

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
