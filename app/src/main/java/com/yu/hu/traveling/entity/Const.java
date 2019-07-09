package com.yu.hu.traveling.entity;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.entity
 * 文件名：Const
 * 创建者：HY
 * 创建时间：2019/6/21 15:42
 * 描述：  Const 存储一些静态常量
 */
@SuppressWarnings("WeakerAccess")
public class Const {

    //容联云
    public static final String APP_ID_RongLY = "8aaf07086b8862cb016b88de1e020109";
    public static final String APP_TOKEN_RongLY = "9b20279d7928bf399afbfba7d369220b";

    //Bugly
    public static final String APP_ID_BUGLY = "8f149ea07b";

    /**
     * 服务器URL
     */
    public static final String URL = "http://47.106.131.58:8081/traveling-0.0.1-SNAPSHOT/";
    //    public static final String URL = "http://10.5.63.152:8080/traveling/";
    public static final String IMG_URL = URL + "imgs/";
    //    public static final String IMG_URL = "http://47.106.131.58:8081/traveling-0.0.1-SNAPSHOT/imgs/";
    public static final String VERIFY_EMAIL_URL = URL + "verifyEmail?code=";

    /**
     * 默认字体
     * 如SplashActivity中app_name的字体
     */
    public static final String DEFAULT_FONT_SPLASH = "fonts/splash.TTF";
    public static final String DEFAULT_FONT_GUIDE = "fonts/FONT.TTF";
    public static final String DEFAULT_FONT_NORMAL = "fonts/normalFont.TTF";

    //最大长度
    public static final int PHONE_MAX_LENGTH = 11;
    public static final int VERIFIED_CODE_MAX_LENGTH = 6;
    public static final int EMAIL_MAX_LENGTH = 50;
    public static final int PASSWORD_MIN_LENGTH = 5;

    //登录方式
    public static final int LOGIN_BY_VERIFIED = 1;
    public static final int LOGIN_BY_PHONE = 2;
    public static final int LOGIN_BY_EMAIL = 3;

    //颜色
    public static final int FOCUS_ON_TEXT_COLOR = 0xFFBFC1C4;
    public static final int NEWS_FLAG1_COLOR = 0xFFF4DB75;
    public static final int NEWS_FLAG2_COLOR = 0xFFF98080;
    public static final int NEWS_FLAG3_COLOR = 0xFF6EDFD2;

    //SP的key
    public static final String SP_KEY_CURRENT_USER = "currentUser";

    //判断程序是否是第一次运行
    public static final String IS_FIRST_RUN = "isFirstRun";
}
