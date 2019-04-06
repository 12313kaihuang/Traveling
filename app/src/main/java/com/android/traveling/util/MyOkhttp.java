package com.android.traveling.util;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.util
 * 文件名：MyOkhttp
 * 创建者：HY
 * 创建时间：2018/10/13 10:02
 * 描述：  okhttp3工具类
 */

public class MyOkhttp {

    public static void get(String url, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()  //默认就是GET请求，可以不写
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void post(String url, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder().build();

        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

}
