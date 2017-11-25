package com.libra.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.support.v4.app.Fragment
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.io.Serializable
import java.util.regex.Pattern

/**
 * 一些扩展
 * Created by libra on 2017/8/2.
 */

/* String */
fun String.equalsIgnoreCase(other: String) = this.toLowerCase().contentEquals(other.toLowerCase())

fun String.isMobileNo(): Boolean {
    var isMobileNo = false
    try {
        val p = Pattern.compile("^(1[3-8]+\\d{9})?$")
        val m = p.matcher(this)
        isMobileNo = m.matches()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return isMobileNo
}

fun String.isIdcard(): Boolean {
    val p18 = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]\$".toRegex()
    val p15 = "^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]\$".toRegex()
    return matches(p18) || matches(p15)
}

/* View */
fun View.hideSoftKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.showSoftkeyboard() {
    val config = this.context.resources.configuration
    if (config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
        val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

/* Context */
fun Context.toast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.dp2px(dpValue: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                                     this.resources.displayMetrics).toInt()
}

fun Context.getVersionName(): String {
    return try {
        val manager = this.packageManager
        val info = manager.getPackageInfo(this.packageName, 0)
        info.versionName
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun Context.getVersionCode(): Int {
    return try {
        val manager = this.packageManager
        val info = manager.getPackageInfo(this.packageName, 0)
        info.versionCode
    } catch (e: Exception) {
        e.printStackTrace()
        0
    }
}

inline fun <reified T : Activity> Context.startActivity(vararg params: Pair<String, Any?>) {
    val intent = Intent(this, T::class.java)
    params.forEach {
        when (it.second) {
            is String -> {
                intent.putExtra(it.first, it.second as String)
            }
            is Int -> {
                intent.putExtra(it.first, it.second as Int)
            }
            is Double -> {
                intent.putExtra(it.first, it.second as Double)
            }
            is Float -> {
                intent.putExtra(it.first, it.second as Float)
            }
            is Boolean -> {
                intent.putExtra(it.first, it.second as Boolean)
            }
            is Serializable -> {
                intent.putExtra(it.first, it.second as Serializable)
            }
        }
    }
    startActivity(intent)
}

inline fun <reified T : Activity> Activity.startActivityForResult(requestCode: Int,
                                                                  vararg params: Pair<String, Any?>) {
    val intent = Intent(this, T::class.java)
    params.forEach {
        when (it.second) {
            is String -> {
                intent.putExtra(it.first, it.second as String)
            }
            is Int -> {
                intent.putExtra(it.first, it.second as Int)
            }
            is Double -> {
                intent.putExtra(it.first, it.second as Double)
            }
            is Float -> {
                intent.putExtra(it.first, it.second as Float)
            }
            is Boolean -> {
                intent.putExtra(it.first, it.second as Boolean)
            }
            is Serializable -> {
                intent.putExtra(it.first, it.second as Serializable)
            }
        }
    }
    startActivityForResult(intent, requestCode)
}


inline fun <reified T : Activity> Fragment.startActivity(vararg params: Pair<String, Any?>) {
    val intent = Intent(this.context, T::class.java)
    params.forEach {
        when (it.second) {
            is String -> {
                intent.putExtra(it.first, it.second as String)
            }
            is Int -> {
                intent.putExtra(it.first, it.second as Int)
            }
            is Double -> {
                intent.putExtra(it.first, it.second as Double)
            }
            is Float -> {
                intent.putExtra(it.first, it.second as Float)
            }
            is Boolean -> {
                intent.putExtra(it.first, it.second as Boolean)
            }
            is Serializable -> {
                intent.putExtra(it.first, it.second as Serializable)
            }
        }
    }
    startActivity(intent)
}

inline fun <reified T : Activity> Fragment.startActivityForResult(requestCode: Int,
                                                                  vararg params: Pair<String, Any?>) {
    val intent = Intent(this.context, T::class.java)
    params.forEach {
        when (it.second) {
            is String -> {
                intent.putExtra(it.first, it.second as String)
            }
            is Int -> {
                intent.putExtra(it.first, it.second as Int)
            }
            is Double -> {
                intent.putExtra(it.first, it.second as Double)
            }
            is Float -> {
                intent.putExtra(it.first, it.second as Float)
            }
            is Boolean -> {
                intent.putExtra(it.first, it.second as Boolean)
            }
            is Serializable -> {
                intent.putExtra(it.first, it.second as Serializable)
            }
        }
    }
    startActivityForResult(intent, requestCode)
}
