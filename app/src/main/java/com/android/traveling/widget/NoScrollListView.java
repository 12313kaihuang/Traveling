package com.android.traveling.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.widget
 * 文件名：NoScrollListView
 * 创建者：HY
 * 创建时间：2019/3/7 20:01
 * 描述：  禁用滑动的ListView
 */

public class NoScrollListView extends ListView {

    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
