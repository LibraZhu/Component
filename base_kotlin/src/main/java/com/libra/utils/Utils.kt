package com.libra.utils

import android.app.Application

/**
 * Created by libra on 2017/8/12.
 */

object Utils {
    fun getBuildConfig(): Boolean {
        try {
            val application = Class.forName("android.app.ActivityThread").getMethod(
                    "currentApplication").invoke(null) as Application
            if (application != null) {
                val clazz = Class.forName(application.packageName + ".BuildConfig")
                val field = clazz.getField("DEBUG")
                val o = field.get(null)
                if (o != null && o is Boolean) {
                    return o
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }
}
