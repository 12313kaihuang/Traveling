package com.yu.hu.traveling.ui.detail;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.view.MediaView;

/**
 * @author Hy
 * created on 2020/04/27 16:02
 * <p>
 * 用来处理手势滑动效果，可以通过ViewDragHelper协调拦截手势
 **/
@SuppressWarnings("unused")
public class ViewZoomBehavior extends CoordinatorLayout.Behavior<MediaView> {


    private OverScroller overScroller;
    private int minHeight;  //view最小高度

    //手势识别helper
    private ViewDragHelper viewDragHelper;

    private int scrollingId;  //
    //对应下面recyclerView
    private View scrollingView;

    private MediaView refChild;  //MediaView
    private int childOriginalHeight; //初始高度
    private boolean canFullScreen; //是否全屏显示

    //用于惯性滑动
    private FlingRunnable runnable;

    public ViewZoomBehavior() {
    }

    public ViewZoomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.view_zoom_behavior, 0, 0);
        minHeight = typedArray.getDimensionPixelOffset(R.styleable.view_zoom_behavior_vz_min_height, 0);
        scrollingId = typedArray.getResourceId(R.styleable.view_zoom_behavior_vz_scrolling_id, 0);
        typedArray.recycle();

        overScroller = new OverScroller(context);
    }

    private ViewZoomCallback mViewZoomCallback;

    @SuppressWarnings("WeakerAccess")
    public void setViewZoomCallback(ViewZoomCallback callback) {
        this.mViewZoomCallback = callback;
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull MediaView child, int layoutDirection) {
        //我们需要在这里去获取 scrollingView,
        // 并全局保存下child view.
        // 并计算出初始时 child的底部值，也就是它的高度。我们后续拖拽滑动的时候，它就是最大高度的限制
        // 与此同时 还需要计算出，当前页面是否可以进行视频的全屏展示，即h>w即可。
        if (viewDragHelper == null) {
            //sensitivity 拖拽灵敏度 1.0为最大灵敏度
            viewDragHelper = ViewDragHelper.create(parent, 1.0f, mCallback);
            scrollingView = parent.findViewById(scrollingId);
            this.refChild = child;
            this.childOriginalHeight = child.getMeasuredHeight();
            LogUtil.d("ViewZoomBehavior - childHeight = " + childOriginalHeight + ", - parent" + parent.getMeasuredHeight());
            canFullScreen = childOriginalHeight >= parent.getMeasuredHeight();
        }
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        //告诉ViewDragHelper 什么时候 可以拦截 手指触摸的这个View的手势分发
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return canFullScreen
                    && refChild.getBottom() > minHeight
                    && refChild.getBottom() <= childOriginalHeight;
        }

        //告诉ViewDragHelper 在屏幕上滑动多少距离才算是拖拽
        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            return viewDragHelper.getTouchSlop();
        }

        /**
         * 告诉ViewDragHelper手指拖拽的这个View 本次滑动最终能够移动的距离
         * @param dy 手指滑动的最终距离，dy>0 代表手指从屏幕上放往屏幕下方滑动，dy<0 代表手指从屏幕下方 往屏幕上方滑动
         */
        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            if (refChild == null || dy == 0) {
                return 0;
            }

            //手指从下往上滑动。dy<0 这意味着refchild的底部 会被向上移动。所以 它的底部的最小值 不能小于minHeight
            if (dy < 0 && refChild.getBottom() < minHeight
                    //手指从上往下滑动。dy>0 意味着refChild的底部会被向下移动。所以它的底部的最大值 不能超过父容器的高度 也即childOriginalHeight
                    || (dy > 0 && refChild.getBottom() > childOriginalHeight)
                    //手指 从屏幕上方 往下滑动。如果scrollinghview 还没有滑动到列表的最顶部，
                    // 也意味围着列表还可以向下滑动，此时咱们应该让列表自行滑动，不做拦截
                    || (dy > 0 && (scrollingView != null && scrollingView.canScrollVertically(-1)))) {
                return 0;
            }

            int maxConsumed;
            if (dy > 0) {
                //如果本次滑动的dy值 追加上 refchild的bottom 值会超过 父容器的最大高度值
                //此时 咱们应该 计算一下
                if (refChild.getBottom() + dy > childOriginalHeight) {
                    maxConsumed = childOriginalHeight - refChild.getBottom();
                } else {
                    maxConsumed = dy;
                }
            } else {
                //else 分支里面 dy的值 是负值。
                //如果本次滑动的值  dy 加上refChild的bottom 会小于minHeight。 那么咱们应该计算一下本次能够滑动的最大距离
                if (refChild.getBottom() + dy < minHeight) {
                    maxConsumed = minHeight - refChild.getBottom();
                } else {
                    maxConsumed = dy;
                }
            }

            //同时设置refChild的宽高以达到随手指缩放效果
            ViewGroup.LayoutParams layoutParams = refChild.getLayoutParams();
            layoutParams.height = (layoutParams.height > 0 ? layoutParams.height : childOriginalHeight) + maxConsumed;
            refChild.setLayoutParams(layoutParams);
            if (mViewZoomCallback != null) {
                mViewZoomCallback.onDragZoom(layoutParams.height);
            }

            return maxConsumed;
        }

        //指的是 我们的手指 从屏幕上 离开的时候 会被调用
        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            //bugfix:这里应该是refChild.getBottom() < childOriginalHeight
            //实现惯性滑动效果
            if (refChild.getBottom() > minHeight && refChild.getBottom() < childOriginalHeight && yvel != 0) {
                refChild.removeCallbacks(runnable);
                runnable = new FlingRunnable(refChild);
                runnable.fling((int) xvel, (int) yvel);
            }
        }
    };

    @Override
    public boolean onTouchEvent(@NonNull CoordinatorLayout parent, @NonNull MediaView child, @NonNull MotionEvent ev) {
        if (!canFullScreen || viewDragHelper == null) {
            return super.onTouchEvent(parent, child, ev);
        }
        viewDragHelper.processTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull MediaView child, @NonNull MotionEvent ev) {
        if (!canFullScreen || viewDragHelper == null) {
            return super.onInterceptTouchEvent(parent, child, ev);
        }
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    private class FlingRunnable implements Runnable {
        private View mFlingView;

        FlingRunnable(View flingView) {
            mFlingView = flingView;
        }

        void fling(int xvel, int yvel) {
            /*
             * startX:开始的X值，由于我们不需要再水平方向滑动 所以为0
             * startY:开始滑动时Y的起始值，那就是flingview的bottom值
             * xvel:水平方向上的速度，实际上为0的
             * yvel:垂直方向上的速度。即松手时的速度
             * minX:水平方向上 滚动回弹的越界最小值，给0即可
             * maxX:水平方向上 滚动回弹越界的最大值，实际上给0也是一样的
             * minY：垂直方向上 滚动回弹的越界最小值，给0即可
             * maxY:垂直方向上，滚动回弹越界的最大值，实际上给0 也一样
             */
            overScroller.fling(0, mFlingView.getBottom(), xvel, yvel, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
            run();
        }

        @Override
        public void run() {
            ViewGroup.LayoutParams params = mFlingView.getLayoutParams();
            int height = params.height;
            //判断本次滑动是否以滚动到最终点。
            if (overScroller.computeScrollOffset() && height >= minHeight && height <= childOriginalHeight) {
                int newHeight = Math.min(overScroller.getCurrY(), childOriginalHeight);
                if (newHeight != height) {
                    params.height = newHeight;
                    mFlingView.setLayoutParams(params);

                    if (mViewZoomCallback != null) {
                        mViewZoomCallback.onDragZoom(newHeight);
                    }
                }
                ViewCompat.postOnAnimation(mFlingView, this);
            } else {
                mFlingView.removeCallbacks(this);
            }
        }
    }

    public interface ViewZoomCallback {
        void onDragZoom(int height);
    }
}
