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
//    public static final String LASTEST_NEWS = "http://47.106.131.58:8081/traveling/news";
    public static final String LOAD_MORE_NEWS = "http://47.106.131.58:8080/traveling/loadMore.json";
//    public static final String LOAD_MORE_NEWS = "http://47.106.131.58:8081/traveling/loadMore";

    //颜色
    public static final int FOCUS_ON_TEXT_COLOR = 0xFFBFC1C4;
    public static final int NEWS_FLAG1_COLOR = 0xFFF4DB75;
    public static final int NEWS_FLAG2_COLOR = 0xFFF98080;
    public static final int NEWS_FLAG3_COLOR = 0xFF6EDFD2;

    //登录方式
    public static final int LOGIN_BY_VERIFIED = 1;
    public static final int LOGIN_BY_PHONE = 2;
    public static final int LOGIN_BY_EMAIL = 3;

    //最大长度
    public static final int PHONE_MAX_LENGTH = 11;
    public static final int VERIFIED_CODE_MAX_LENGTH = 6;
    public static final int EMAIL_MAX_LENGTH = 50;
    public static final int PASSWORD_MIN_LENGTH = 5;

    //检测email的正则表达式
    public static final String EMAIL_REGULAR = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    //Bmob AppId
    public static final String BMOB_APPLICATION_ID = "839622a09a889d9c7473c5ab04c606cf";
    //bugly AppId
    public static final String BUGLY_APP_ID = "00f39d0cd3";

    //短信模板
    public static final String BMOB_SMS_TEMPLATE = "register";

    //性别
    public static final String GENDER_SECRET = "保密";
    public static final String GENDER_MALE = "男";
    public static final String GENDER_FEMALE = "女";

    //广播
    public static final String BROADCAST_LOGIN = "com.traveling.broadcast.LOGIN";
    public static final String BROADCAST_LOGOUT = "com.traveling.broadcast.LOGOUT";

    //手机格式转换
    public static final String FORMAT_PHONE_REGEX = "(\\d{3})\\d{4}(\\d{4})";

    public static final String URL = "http://47.106.131.58:8081/traveling-0.0.1-SNAPSHOT/";
//    public static final String URL = "http://10.5.62.53:8080/traveling/";
    public static final String IMG_URL = URL + "imgs/";
//    public static final String IMG_URL = "http://47.106.131.58:8081/traveling-0.0.1-SNAPSHOT/imgs/";
    public static final String VERIFY_EMAIL_URL = URL + "verifyEmail?code=";
}
