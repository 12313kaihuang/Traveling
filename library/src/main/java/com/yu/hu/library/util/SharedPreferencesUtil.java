package com.yu.hu.library.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 文件名：SharedPreferencesUtil
 * 创建者：HY
 * 创建时间：2018/9/22 10:55
 * 描述：  SharedPreference工具类
 */

@SuppressWarnings("unused")
public class SharedPreferencesUtil {

    //AndroidUtilCode--SPUtils

    private static SharedPreferences mSharedPreferences;

    public static void init(String name, Context context) {
        mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getPreferences() {
        return mSharedPreferences;
    }

    public static void setmSharedPreferences(SharedPreferences mSharedPreferences) {
        SharedPreferencesUtil.mSharedPreferences = mSharedPreferences;
    }

    /**
     * 存入String类型值
     *
     * @param key   key
     * @param value value
     */
    public static void putString(String key, String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    /**
     * 取出String类型值
     *
     * @param key      key
     * @param defValue 默认值
     * @return String
     */
    public static String getString(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }


    /**
     * 存入Int类型值
     *
     * @param key   key
     * @param value value
     */
    public static void putInt(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    /**
     * 取出Int类型值
     *
     * @param key      key
     * @param defValue 默认值
     * @return int
     */
    public static int getInt(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }


    /**
     * 存入Long类型值
     *
     * @param key   key
     * @param value value
     */
    public static void putLong(String key, long value) {
        mSharedPreferences.edit().putLong(key, value).apply();
    }

    /**
     * 取出long类型值
     *
     * @param key      key
     * @param defValue 默认值
     * @return long
     */
    public static long getLong(String key, long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }


    /**
     * 存入float类型值
     *
     * @param key   key
     * @param value value
     */
    public static void putFloat(String key, float value) {
        mSharedPreferences.edit().putFloat(key, value).apply();
    }

    /**
     * 取出float类型值
     *
     * @param key      key
     * @param defValue 默认值
     * @return float
     */
    public static float getFloat(String key, float defValue) {
        return mSharedPreferences.getFloat(key, defValue);
    }


    /**
     * 存入Boolean类型值
     *
     * @param key   key
     * @param value value
     */
    public static void putBoolean(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    /**
     * 取出Boolean类型值
     *
     * @param key      key
     * @param defValue 默认值
     * @return boolean
     */
    public static Boolean getBoolean(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }


    /*
     * 获取到SharedPreferences对象
     *
     * @param context context
     * @return SharedPreferences
     */
    // private static SharedPreferences getSharedPreferences(Context context) {
    //      return context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    //  }

}
