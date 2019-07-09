package com.yu.hu.library.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 文件名：RetrofitUtil
 * 创建者：HY
 * 创建时间：2019/6/5 10:54
 * 描述：  Retrofit工具包
 * 单例模式
 * 需要进行初始化{@link #init(String)}
 * <p>
 * {@link #getService(Class)}
 */
@SuppressWarnings("unused")
public class RetrofitUtil {

    private static RetrofitUtil instance;

    public static void init(String baseUrl) {
        if (BASE_URL == null) {
            BASE_URL = baseUrl;
        } else {
            throw new RuntimeException("RetrofitUtil has already initialized");
        }
    }

    public static RetrofitUtil getInstance() {
        if (instance == null) {
            synchronized (RetrofitUtil.class) {
                if (instance == null) {
                    instance = new RetrofitUtil();
                }
            }
        }
        return instance;
    }

    /*=========================================================*/

    /**
     * 默认超时时长
     * 单位：秒
     */
    private static final int DEFAULT_TIMEOUT = 10;

    /**
     * 默认日期转换格式
     */
    private static final String DEFAULT_DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * baseUrl
     */
    private static String BASE_URL;

    //retrofit
    private Retrofit mRetrofit;

    /**
     * 缓存已加载过的Service
     */
    private HashMap<String, Object> mServiceMap;

    private RetrofitUtil() {
        //配置OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //连接超时时间
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //写超时时间
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //读超时时间
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //日期转换
        Gson gson = new GsonBuilder().setDateFormat(DEFAULT_DATA_FORMAT)
                .create();

        //缓存

        //拦截器

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                //rx
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mServiceMap = new HashMap<>();
    }

    /**
     * 获取 Service
     */
    @SuppressWarnings({"unchecked", "WeakerAccess"})
    public <T> T getService(final Class<T> serviceClass) {
        String serviceName = serviceClass.getName();
        if (mServiceMap.containsKey(serviceName)) {
            return (T) mServiceMap.get(serviceName);
        } else {
            T service = mRetrofit.create(serviceClass);
            mServiceMap.put(serviceName, service);
            return service;
        }
    }

}
