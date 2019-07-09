package com.yu.hu.library.fragment;


import android.view.View;

import com.yu.hu.library.activity.BaseActivity;

import androidx.annotation.IdRes;

/**
 * 文件名：FragmentImpl
 * 创建者：HY
 * 创建时间：2019/4/21 20:10
 * 描述：  Fragment需要实现的接口
 *
 * @see BaseFragment
 */
public interface FragmentImpl {

    /**
     * 获取当前Fragment
     * <p>
     * 要求Fragment继承自BaseFragment
     */
    <T extends BaseFragment> T getCurrentFragment();

    /**
     * 获取当前Activity
     * <p>
     * 要求Activity继承自BaseActivity
     */
    <T extends BaseActivity> T getCurrentActivity();

    <V extends View> V findViewById(@IdRes int idRes);
}
