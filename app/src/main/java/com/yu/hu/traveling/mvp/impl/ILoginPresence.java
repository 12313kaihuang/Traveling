package com.yu.hu.traveling.mvp.impl;

import com.yu.hu.library.mvp.Presence;
import com.yu.hu.traveling.entity.user.User;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.mvp.impl
 * 文件名：ILoginPresence
 * 创建者：HY
 * 创建时间：2019/6/24 11:12
 * 描述：  LoginPresence
 *
 * @see com.yu.hu.traveling.activity.LoginActivity
 */
public interface ILoginPresence extends Presence {

    /**
     * 登录成功
     */
    void onLoginSuccess(User user);

    /**
     * 登录失败
     */
    void onLoginFailure(String reason);

    /**
     * 发送验证码成功
     */
    void onVerifiedCodeSended();

}
