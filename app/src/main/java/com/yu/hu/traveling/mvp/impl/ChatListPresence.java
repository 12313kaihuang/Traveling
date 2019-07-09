package com.yu.hu.traveling.mvp.impl;

import com.yu.hu.library.mvp.Presence;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.mvp.impl
 * 文件名：ChatListPresence
 * 创建者：HY
 * 创建时间：2019/6/24 17:52
 * 描述：  聊天列表
 * @see com.yu.hu.traveling.fragment.ChatListFragment
 */
public interface ChatListPresence extends Presence {

    /**
     * 加载聊天列表
     */
    void onLoadChatList();

    /**
     * 加载 没有登录时的页面
     */
    void onLoadNotLogin();

}
