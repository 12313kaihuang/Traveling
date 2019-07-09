package com.yu.hu.traveling.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.yu.hu.library.widget.SimpleActionBar;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.behavior
 * 文件名：MyTranslateBehavior
 * 创建者：HY
 * 创建时间：2019/7/5 14:49
 * 描述：  我的页面顶部ActionBar渐变Behavior
 *
 * @see com.yu.hu.traveling.fragment.MyFragment
 */
@SuppressWarnings("unused")
public class MyTranslateBehavior extends BaseBehaivor<SimpleActionBar> {

    //最大滑动距离
    private int maxDistance = 0;

    //一开始的Y值
    private int startY = 0;

    public MyTranslateBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param axes 滑动方向
     */
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull SimpleActionBar child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        //return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
        //高级写法？
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    /**
     * @param child  对应SimpleActionBar
     * @param target 对应RecyclerView
     */
    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull SimpleActionBar child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        changeChildAlpha(child, target);
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull SimpleActionBar child, @NonNull View target, float velocityX, float velocityY) {
        changeChildAlpha(child, target);
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    //改变ToolBar的透明度
    private void changeChildAlpha(@NonNull SimpleActionBar child, @NonNull View target) {
        // String tag = "test";
        // LogUtil.d(tag,"top = " + target.getTop());
        //LogUtil.d(tag,"child.getBottom = " + child.getBottom());
        if (maxDistance == 0) {
            child.changeAlpha(0);
            startY = target.getTop();
            maxDistance = startY - child.getBottom();
            return;
        }

        int distance = startY - target.getTop();
        float alpha = distance * 1.0f / maxDistance;
        //LogUtil.d(tag,"distance = " + distance);
        //LogUtil.d(tag,"alpha = " + alpha);
        if (alpha > 1.0f) {
            alpha = 1.0f;
        }
        child.changeAlpha(alpha);
    }
}
