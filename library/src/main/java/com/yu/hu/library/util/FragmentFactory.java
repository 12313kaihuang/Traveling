package com.yu.hu.library.util;

import android.content.Context;

import com.yu.hu.library.fragment.BaseFragment;

import androidx.fragment.app.Fragment;

/**
 * 项目名：CoolMusic
 * 包名：  com.yu.hu.library.util
 * 文件名：FragmentFactory
 * 创建者：HY
 * 创建时间：2019/6/10 12:00
 * 描述：  Fragment工厂
 */
@SuppressWarnings("unused")
public class FragmentFactory {


    public static <T extends BaseFragment> BaseFragment createFragment(Context context,Class<T> fragmentCLass) {
        String name = fragmentCLass.getName();
        return createFragment(context, name);
    }

    @SuppressWarnings("WeakerAccess")
    public static <T extends BaseFragment> BaseFragment createFragment(Context context, String fragmentName) {
        return (BaseFragment) Fragment.instantiate(context, fragmentName);
    }
}
