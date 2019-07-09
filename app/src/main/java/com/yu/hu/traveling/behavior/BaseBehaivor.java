package com.yu.hu.traveling.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.behavior
 * 文件名：BaseBehaivor
 * 创建者：HY
 * 创建时间：2019/7/5 14:56
 * 描述：  Behaivor基类
 */
public abstract class BaseBehaivor<V extends View> extends CoordinatorLayout.Behavior<V> {

    //记得构造函数别忘了
    public BaseBehaivor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
