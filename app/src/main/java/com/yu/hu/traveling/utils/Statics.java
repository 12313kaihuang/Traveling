package com.yu.hu.traveling.utils;

import android.Manifest;

/**
 * @author Hy
 * created on 2020/04/15 23:00
 * <p>
 * 用于存储一些静态常亮
 **/
public class Statics {

    //所需要的所有权限
    public static final String[] PERMISSIONS_ALL_NEED =
            new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};

    public static final String CACHE_KEY_REQUEST_PERMISSIONS = "cache_key_request_permissions";

    public static final String QQ_APP_ID = "101512394";

    public static final String CHATKIT_APP_ID = "23MM7AIgH17cxvBsq3hg0C4i-gzGzoHsz";
    public static final String CHATKIT_APP_KEY = "fW1k7weKPWepuakSXlxPQj3w";

    public static final String BASE_URL = "http://47.106.131.58/traveling-jetpack/";
    public static final String BASE_URL_TEST = "http://192.168.0.102:8080/traveling-jetpack/";
}
