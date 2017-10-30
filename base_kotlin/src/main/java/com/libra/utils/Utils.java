package com.libra.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by libra on 2017/8/12.
 */

public class Utils {
    public static boolean getBuildConfig() {
        try {
            Application application = (Application) Class.forName("android.app.ActivityThread")
                                                         .getMethod("currentApplication")
                                                         .invoke(null, (Object[]) null);
            if (application != null) {
                Class<?> clazz = Class.forName(application.getPackageName() + ".BuildConfig");
                Field field = clazz.getField("DEBUG");
                Object o = field.get(null);
                if (o != null && o instanceof Boolean) {
                    return ((Boolean) o).booleanValue();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取当前时区
     */
    public static String getCurrentTimeZone() {
        TimeZone timeZone = TimeZone.getDefault();
        return String.format(Locale.getDefault(), "%s (%s) offset %d", timeZone.getID(),
                             timeZone.getDisplayName(false, TimeZone.SHORT),
                             timeZone.getRawOffset());
    }

    /**
     * 判断网络是否可用
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     * <p>需要异步ping，如果ping不通就说明网络不可用</p>
     * <p>ping的ip为阿里巴巴公共ip：223.5.5.5</p>
     *
     * @return {@code true}: 可用<br>{@code false}: 不可用
     */
    public static boolean isAvailableByPing() {
        return isAvailableByPing(null);
    }

    /**
     * 判断网络是否可用
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     * <p>需要异步ping，如果ping不通就说明网络不可用</p>
     *
     * @param ip ip地址（自己服务器ip），如果为空，ip为阿里巴巴公共ip
     * @return {@code true}: 可用<br>{@code false}: 不可用
     */
    public static boolean isAvailableByPing(String ip) {
        if (ip == null || ip.length() <= 0) {
            ip = "223.5.5.5";// 阿里巴巴公共ip
        }
        ShellUtils.CommandResult result =
                ShellUtils.execCmd(String.format("ping -c 1 %s", ip), false);
        boolean ret = result.result == 0;
        if (result.errorMsg != null) {
            Log.d("Utils", "isAvailableByPing() called" + result.errorMsg);
        }
        if (result.successMsg != null) {
            Log.d("Utils", "isAvailableByPing() called" + result.successMsg);
        }
        return ret;
    }
}
