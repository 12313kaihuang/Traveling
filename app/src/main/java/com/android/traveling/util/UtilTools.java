package com.android.traveling.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.widget.TextView;
import android.widget.Toast;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.util
 * 文件名：UtilTools
 * 创建者：HY
 * 创建时间：2018/9/22 11:35
 * 描述：  工具类
 */

@SuppressWarnings("unused")
public class UtilTools {

    //设置字体
    public static void setFont(Context context, TextView textView, String fontId) {
        Typeface fontType = Typeface.createFromAsset(context.getAssets(), fontId);
        textView.setTypeface(fontType);
    }

    //设置默认字体
    public static void setDefaultFontType(Context context, TextView textView) {
        Typeface fontType = Typeface.createFromAsset(context.getAssets(), StaticClass.NORMAL_FONT);
        textView.setTypeface(fontType);
    }

    /**
     * toast
     *
     * @param context 上下文
     * @param text    需要显示的文字
     */
    @SuppressWarnings("SameParameterValue")
    public static void toast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * toast
     * @param context 上下文
     * @param text 需要显示的文字
     * @param duration 显示时长
     */
    public static void toast(Context context, String text, int duration) {
        Toast.makeText(context, text, duration).show();
    }

    /**
     * 获取版本信息
     *
     * @param context 上下文
     * @return 返回versionName，出错则返回null
     */
    public static String getVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),
                    StaticClass.PACKAGE_INFO_FLAG);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }


    /**
     * 获取retrofit对象
     *
     * @return retrofit对象
     */
    public static Retrofit getRetrofit() {
        return new Retrofit.Builder().baseUrl(StaticClass.URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

}
