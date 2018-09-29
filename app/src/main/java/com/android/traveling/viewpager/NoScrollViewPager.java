package com.android.traveling.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.viewpager
 * 文件名：NoScrollViewPager
 * 创建者：HY
 * 创建时间：2018/9/29 10:18
 * 描述：  去除滚动效果
 */

public class NoScrollViewPager extends ViewPager {

    public NoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    //禁用滑动手势
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            performClick();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public void setCurrentItem(int item) {
        //去除切换时的滑动效果
        super.setCurrentItem(item,false);
    }
}
