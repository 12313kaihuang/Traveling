package com.yu.hu.library.impl;

import rx.Subscription;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.library.impl
 * 文件名：SubscriptionImpl
 * 创建者：HY
 * 创建时间：2019/6/24 16:12
 * 描述：  添加Subscription，
 * 这样Fragment或Activity均可强转并调用此方法
 */
public interface SubscriptionImpl {

    void addSubscription(Subscription s);

}
