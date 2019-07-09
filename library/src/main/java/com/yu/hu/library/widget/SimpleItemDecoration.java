package com.yu.hu.library.widget;

import android.graphics.Rect;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 文件名：SimpleItemDecoration
 * 创建者：HY
 * 创建时间：2019/5/6 12:46
 * 描述：  RecyclerView每个item的间隔样式
 */
@SuppressWarnings("unused")
public class SimpleItemDecoration extends RecyclerView.ItemDecoration {

    @SuppressWarnings("WeakerAccess")
    public static final String TOP_DECORATION = "top_decoration";
    @SuppressWarnings("WeakerAccess")
    public static final String BOTTOM_DECORATION = "bottom_decoration";
    @SuppressWarnings("WeakerAccess")
    public static final String LEFT_DECORATION = "left_decoration";
    @SuppressWarnings("WeakerAccess")
    public static final String RIGHT_DECORATION = "right_decoration";

    @StringDef({TOP_DECORATION, BOTTOM_DECORATION, LEFT_DECORATION, RIGHT_DECORATION})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Decoration {
    }

    private int rightSpace = 0;  //右边间距
    private int topSpace = 0;  //上边边间距
    private int leftSpace = 0; //左边间距
    private int bottomSpace = 0; //下边间距

    /**
     * @param bottomSpace 下间距
     */
    public SimpleItemDecoration(int bottomSpace) {
        this.bottomSpace = bottomSpace;
    }

    /**
     * 指定某一个属性
     *
     * @param decoration decoration
     * @param space      间距
     */
    public SimpleItemDecoration(@Decoration String decoration, int space) {
        switch (decoration) {
            case RIGHT_DECORATION:
                this.rightSpace = space;
                break;
            case TOP_DECORATION:
                this.topSpace = space;
                break;
            case LEFT_DECORATION:
                this.leftSpace = space;
                break;
            case BOTTOM_DECORATION:
                this.bottomSpace = space;
        }
    }


    /**
     * @param rightSpace  右间距
     * @param topSpace    上间距
     * @param leftSpace   左间距
     * @param bottomSpace 下间距
     */
    public SimpleItemDecoration(int rightSpace, int topSpace, int leftSpace, int bottomSpace) {
        this.rightSpace = rightSpace;
        this.topSpace = topSpace;
        this.leftSpace = leftSpace;
        this.bottomSpace = bottomSpace;
    }

    /**
     * @param outRect Item的矩边界
     * @param view    ItemView
     * @param parent  RecyclerView
     * @param state   RecyclerView的状态
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = topSpace;
        outRect.left = leftSpace;
        outRect.right = rightSpace;
        outRect.bottom = bottomSpace;
    }
}
