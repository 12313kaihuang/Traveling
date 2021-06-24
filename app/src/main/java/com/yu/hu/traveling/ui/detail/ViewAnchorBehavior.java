package com.yu.hu.traveling.ui.detail;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.ninegridlayout.utils.PixUtils;
import com.yu.hu.traveling.R;

/**
 * @author Hy
 * created on 2020/04/27 16:00
 * <p>
 * 用来设置CoordinateLayout中各控件之间的相对位置
 **/
@SuppressWarnings("unused")
public class ViewAnchorBehavior extends CoordinatorLayout.Behavior<View> {

    //锚点View的id
    private int anchorId;

    /**
     * 评论列表的高度计算时需要减去布局互动栏（评论点赞框）的高度，
     * 而其高度固定为{@code 48dp}，所以这里直接赋值了，如果不减去该高度的话，
     * 最后一条评论会被底部交互栏盖住
     */
    private int extraUsed;

    public ViewAnchorBehavior() {
    }

    @SuppressWarnings("WeakerAccess")
    public ViewAnchorBehavior(int anchorId) {
        this.anchorId = anchorId;
        extraUsed = PixUtils.dp2px(48);
    }

    public ViewAnchorBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.view_anchor_behavior);
        anchorId = typedArray.getResourceId(R.styleable.view_anchor_behavior_anchor_behavior_anchorId, 0);
        typedArray.recycle();
        extraUsed = PixUtils.dp2px(48);
    }

    /**
     * CoordinatorLayout 在测量每一个view的时候 会调用该方法，如果返回true,
     * CoordinatorLayout 就不会再次测量child了。会使用咱们给的测量的值 去摆放view的位置
     * <p>
     * 这里只需要考虑垂直方向的定位关系，
     * {@code  heightUsed}计算方法应为{@code anchorView.bottom + child.topMargin + extraUsed}
     *
     * @param parent                  CoordinatorLayout
     * @param child                   child
     * @param parentWidthMeasureSpec  parentWidthMeasureSpec
     * @param widthUsed               水平方向上CoordinateLayout已使用的宽度
     * @param parentHeightMeasureSpec parentHeightMeasureSpec
     * @param heightUsed              垂直方向上CoordinateLayout已使用的高度
     * @return 如果返回{@code true},就不会再次测量child了。会使用咱们给的测量的值 去摆放view的位置
     */
    @Override
    public boolean onMeasureChild(@NonNull CoordinatorLayout parent, @NonNull View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        View anchorView = parent.findViewById(anchorId);
        if (anchorView == null) {
            return false;
        }

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        int topMargin = layoutParams.topMargin;
        int bottom = anchorView.getBottom();

        //在测量子View时，需要告诉CoordinatorLayout。垂直方向上 已经有多少空间被占用了
        //如果heightUsed给0，那么评论列表这个view它测量出来的高度 将会大于它实际的高度。以至于会被底部互动区域给遮挡
        heightUsed = bottom + topMargin + extraUsed;
        parent.onMeasureChild(child, parentWidthMeasureSpec, 0, parentHeightMeasureSpec, heightUsed);
        return true;
    }

    /**
     * CoordinatorLayout摆放子view时回调此方法
     *
     * @param parent          CoordinatorLayout
     * @param child           child
     * @param layoutDirection layoutDirection
     * @return 如果返回{@code true},CoordinatorLayout不会再拜访此view了
     */
    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull View child, int layoutDirection) {

        LogUtil.d("onLayoutChild - name = " + child.getClass().getSimpleName());
        View anchorView = parent.findViewById(anchorId);
        if (anchorView == null) {
            return false;
        }
        LogUtil.d("onLayoutChild22 - name = " + child.getClass().getSimpleName());
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        int topMargin = params.topMargin;
        int bottom = anchorView.getBottom();
        parent.onLayoutChild(child, layoutDirection);
        //设置垂直方向上的偏移量
        child.offsetTopAndBottom(bottom + topMargin);
        return true;
    }

    /**
     * @param parent     CoordinatorLayout
     * @param child      应用behavior的那个view
     * @param dependency 所依赖的那个view
     * @return 判断{@code child}是否依赖于{@code dependency}
     */
    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        return anchorId == dependency.getId();
    }
}
