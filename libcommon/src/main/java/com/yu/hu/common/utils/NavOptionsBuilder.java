package com.yu.hu.common.utils;

import androidx.navigation.NavOptions;

import com.yu.hu.common.R;

/**
 * @author Hy
 * created on 2020/04/15 17:07
 * 构建NavOptions 来设置转场动画
 **/
public class NavOptionsBuilder {


    /**
     * 默认平移动画
     * duration 500
     */
    public static NavOptions defaultSlideAnim() {
        return new NavOptions.Builder()
                //导航时 需要进入的页面的动画 即目标页面
                .setEnterAnim(R.anim.slide_in_right)
                //导航时 需要退出的页面的动画 即当前页面
                .setExitAnim(R.anim.slide_out_left)
                //返回时 需要进入动画 即目标页面
                .setPopEnterAnim(R.anim.slide_in_left)
                //返回时 需要退出的页面的动画 即原页面
                .setPopExitAnim(R.anim.slide_out_right)
                .build();
    }

    /**
     * 默认缩放动画
     * duration 200
     */
    public static NavOptions defaultScaleAnim() {
        return new NavOptions.Builder()
                //导航时 需要进入的页面的动画 即目标页面
                .setEnterAnim(R.anim.open_enter)
                //导航时 需要退出的页面的动画 即当前页面
                .setExitAnim(R.anim.open_exit)
                //返回时 需要进入动画 即目标页面
                .setPopEnterAnim(R.anim.close_enter)
                //返回时 需要退出的页面的动画 即原页面
                .setPopExitAnim(R.anim.close_exit)
                .build();
    }
}
