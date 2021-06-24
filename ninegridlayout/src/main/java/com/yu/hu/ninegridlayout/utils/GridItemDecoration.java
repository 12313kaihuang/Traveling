package com.yu.hu.ninegridlayout.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Hy on 2019/12/31 11:04
 * <p>
 * 底部间隔
 **/
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int offset;  //sp

    /**
     * 传sp单位
     */
    public GridItemDecoration(int offset) {
        this.offset = offset;
    }

    public void changeOffset(int dpValue) {
        offset = PixUtils.dp2px(dpValue);
    }

    //https://blog.csdn.net/Summer_may/article/details/80457331
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        //判断是否需要添加右侧边距
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        if (layoutManager == null || layoutParams == null) {
            return;
        }
        int position = parent.getChildAdapterPosition(view);  //position
        int spanCount = layoutManager.getSpanCount();  //每行的个数
        if (spanCount == 1) {
            return;
        }

        if (layoutManager.getOrientation() == GridLayoutManager.VERTICAL) {
            //判断是否在第一排
            if (layoutManager.getSpanSizeLookup().getSpanGroupIndex(position, spanCount) == 0) {
                //第一排的需要上面
                outRect.top = offset;
            }
            outRect.bottom = offset;
            //这里忽略和合并项的问题，只考虑占满和单一的问题
            if (layoutParams.getSpanSize() == spanCount) {
                //占满
                outRect.left = offset;
                outRect.right = offset;
            } else {
                //这是一个很神奇的计算方式  不能想当然的去设置 = offset
                outRect.left = (int) (((float) (spanCount - layoutParams.getSpanIndex())) / spanCount * offset);
                outRect.right = (int) (((float) offset * (spanCount + 1) / spanCount) - outRect.left);
            }
        } else {
            if (layoutManager.getSpanSizeLookup().getSpanGroupIndex(position, spanCount) == 0) {
                //第一排的需要left
                outRect.left = offset;
            }
            outRect.right = offset;
            //这里忽略和合并项的问题，只考虑占满和单一的问题
            if (layoutParams.getSpanSize() == spanCount) {
                //占满
                outRect.top = offset;
                outRect.bottom = offset;
            } else {
                outRect.top = (int) (((float) (spanCount - layoutParams.getSpanIndex())) / spanCount * offset);
                outRect.bottom = (int) (((float) offset * (spanCount + 1) / spanCount) - outRect.top);
            }
        }
    }



}
