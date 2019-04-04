package com.libra.base

import android.app.Application
import com.libra.crash.CrashHandler

/**
 * @author Libra
 * @since 2019/4/4
 */
abstract class BaseApp : Application(), CrashHandler.CrashListener {
    override fun onCreate() {
        super.onCreate()
        initCrashHandler()
    }

    private fun initCrashHandler() {
        CrashHandler.getInstance().init(this, this)
    }
}