package com.yu.hu.library.util;

import android.util.Log;

import com.yu.hu.library.application.BaseApplication;

/**
 * 文件名：LogUtil
 * 创建者：HY
 * 创建时间：2018/9/22 10:48
 * 描述：  Log工具类
 *
 * @see #d(String)
 * @see #d(String, String)
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class LogUtil {

    //开关
    public static boolean DEBUG = true;

    //TAG
    public static String TAG = "AppName";

    //四个等级 DIWE

    /**
     * 使用初始化时的{@link #TAG}
     * {@link BaseApplication#getApplicationName()}
     *
     * @param text text
     */
    public static void d(String text) {
        d(TAG, text);
    }

    public static void i(String text) {
        i(TAG, text);
    }

    public static void w(String text) {
        w(TAG, text);
    }

    public static void e(String text) {
        e(TAG, text);
    }

    /**
     * 使用参数中的TAG
     *
     * @param text text
     */
    public static void d(String TAG, String text) {
        if (DEBUG) {
            Log.d(TAG, text);
        }
    }

    public static void i(String TAG, String text) {
        if (DEBUG) {
            Log.i(TAG, text);
        }
    }

    public static void w(String TAG, String text) {
        if (DEBUG) {
            Log.w(TAG, text);
        }
    }

    public static void e(String TAG, String text) {
        if (DEBUG) {
            Log.d(TAG, text);
        }
    }

}
