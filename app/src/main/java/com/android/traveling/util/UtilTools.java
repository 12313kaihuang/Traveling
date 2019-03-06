package com.android.traveling.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.android.traveling.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wx.goodview.GoodView;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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
     *
     * @param context  上下文
     * @param text     需要显示的文字
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

    private static Retrofit retrofit;

    /**
     * 获取retrofit对象
     *
     * @return retrofit对象
     */
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            //配置OkHttpClient
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            //连接超时时间
            builder.connectTimeout(StaticClass.CONNECT_TIMEOUT, TimeUnit.SECONDS);
            //日期转换
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
            retrofit = new Retrofit.Builder().baseUrl(StaticClass.URL)
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create(gson)).build();
        }

        return retrofit;
    }


    /**
     * 显示点赞效果
     *
     * @param v       v
     * @param context context
     */
    public static void showGoodView(View v, Context context) {
        final GoodView goodView = new GoodView(context);
        goodView.setTextInfo("+1", ContextCompat.getColor(context, R.color.red), 16);
        goodView.show(v);
    }
}
