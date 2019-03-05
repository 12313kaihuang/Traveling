package com.android.traveling.util;

import android.graphics.Rect;
import android.support.annotation.StringDef;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.util
 * 文件名：RecyclerViewSpacesItemDecoration
 * 创建者：HY
 * 创建时间：2019/3/3 10:27
 * 描述：  用于设置RecyclerView item之间的间隔
 */

@SuppressWarnings({"unused"})
public class RecyclerViewSpacesItemDecoration extends RecyclerView.ItemDecoration {

    @SuppressWarnings("WeakerAccess")
    public static final String TOP_DECORATION = "top_decoration";
    @SuppressWarnings("WeakerAccess")
    public static final String BOTTOM_DECORATION = "bottom_decoration";
    @SuppressWarnings("WeakerAccess")
    public static final String LEFT_DECORATION = "left_decoration";
    @SuppressWarnings("WeakerAccess")
    public static final String RIGHT_DECORATION = "right_decoration";

    @StringDef({TOP_DECORATION, BOTTOM_DECORATION, LEFT_DECORATION,RIGHT_DECORATION})
    @Retention(RetentionPolicy.SOURCE)
    private  @interface Decoration {}

    private int rightSpace = 0;  //右边间距
    private int topSpace = 0;  //上边边间距
    private int leftSpace = 0; //左边间距
    private int bottomSpace = 0; //下边间距

    /**
     *
     * @param bottomSpace 下间距
     */
    public RecyclerViewSpacesItemDecoration(int bottomSpace) {
        this.bottomSpace = bottomSpace;
    }

    /**
     * 指定某一个属性
     * @param decoration  decoration
     * @param space 间距
     */
    public RecyclerViewSpacesItemDecoration(@Decoration String decoration,int space){
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
     *
     * @param rightSpace 右间距
     * @param topSpace 上间距
     * @param leftSpace 左间距
     * @param bottomSpace 下间距
     */
    public RecyclerViewSpacesItemDecoration(int rightSpace, int topSpace, int leftSpace, int bottomSpace) {
        this.rightSpace = rightSpace;
        this.topSpace = topSpace;
        this.leftSpace = leftSpace;
        this.bottomSpace = bottomSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = topSpace;
        outRect.left = leftSpace;
        outRect.right = rightSpace;
        outRect.bottom = bottomSpace;
    }
}
