package com.android.traveling.util;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.util
 * 文件名：StaticClass
 * 创建者：HY
 * 创建时间：2018/9/22 11:32
 * 描述：  数据/常量
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class StaticClass {

    //getPackageInfo flag
    public static final int PACKAGE_INFO_FLAG = 0;

    //闪屏页延时
    public static final int HANDLER_SPLASH = 1001;

    //判断程序是否是第一次运行
    public static final String IS_FIRST_RUN = "isFirstRun";

    //版本更新
    public static final String CHECK_UPDATE_URL = "http://47.106.131.58:8080/traveling/config.json";

    //服务条款
    public static final String SERVICE_TERMS_URL = "https://www.baidu.com";

    //fontId
    public static final String FONT = "fonts/FONT.TTF";
    public static final String HANGOVER = "fonts/HangoverScript.ttf";
    public static final String HANGOVER_BOLD = "fonts/HangoverScript Bold.ttf";
    public static final String SPLASH_FONT = "fonts/splash.TTF";
    public static final String NORMAL_FONT = "fonts/normalFont.TTF";

    /*json url*/
    //news
    public static final String LASTEST_NEWS = "http://47.106.131.58:8080/traveling/news.json";
    public static final String LOAD_MORE_NEWS = "http://47.106.131.58:8080/traveling/loadMore.json";
}
