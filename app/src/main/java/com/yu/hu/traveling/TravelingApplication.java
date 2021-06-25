package com.yu.hu.traveling;


import com.yu.hu.common.application.BaseApplication;
import com.yu.hu.emoji.utils.EmojiManager;
import com.yu.hu.libnetwork.RetrofitManager;
import com.yu.hu.traveling.utils.Statics;
import com.yu.hu.traveling.utils.TravelingUserProvider;

import cn.leancloud.chatkit.LCChatKit;

/**
 * @author Hy
 * created on 2020/04/22 21:50
 **/
public class TravelingApplication extends BaseApplication {

    private static final boolean DEBUG = false;

    @Override
    public void onCreate() {
        super.onCreate();
        //Emoji表情初始化
        EmojiManager.init();
        //leanclound
        LCChatKit.getInstance().setProfileProvider(TravelingUserProvider.getInstance());
        LCChatKit.getInstance().init(getApplicationContext(), Statics.CHATKIT_APP_ID, Statics.CHATKIT_APP_KEY);
        //baseUrl设置
        RetrofitManager.init(DEBUG ? Statics.BASE_URL_TEST : Statics.BASE_URL);
    }
}
