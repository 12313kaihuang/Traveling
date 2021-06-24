package com.yu.hu.ninegridlayout;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.hu.ninegridlayout.adapter.GridEditItemAdapter;
import com.yu.hu.ninegridlayout.databinding.LayoutNineGridBinding;
import com.yu.hu.ninegridlayout.entity.GridItem;
import com.yu.hu.ninegridlayout.utils.PixUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hy
 * created on 2020/04/25 19:08
 * <p>
 * 发布页面展示已添加图片
 **/
public class EditGridView extends LinearLayout {

    private GridEditItemAdapter mAdapter;

    private final NineGridView.Config mConfig;

    public EditGridView(Context context) {
        this(context, null);
    }

    public void setOnItemClickListener(OnGridItemClickListener itemClickListener) {
        mAdapter.setItemClickListener(itemClickListener);
    }

    public void setAddIconDrawableResource(@DrawableRes int drawableResource) {
        mConfig.setDrawableResource(drawableResource);
    }

    public EditGridView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditGridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EditGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mConfig = new NineGridView.Config(16);
        mAdapter = new GridEditItemAdapter();
        ItemDecoration itemDecoration = new ItemDecoration(PixUtils.dp2px(mConfig.spanSpacing));
        LayoutNineGridBinding mDataBinding = LayoutNineGridBinding.inflate(LayoutInflater.from(context), this, true);

        //itemDecoration只设置一次就行了 不能放在setItems里  否则多次set后布局会错乱
        mDataBinding.recyclerView.addItemDecoration(itemDecoration);
        mDataBinding.recyclerView.setAdapter(mAdapter);
        mDataBinding.recyclerView.setNestedScrollingEnabled(false); //禁止滑动

        int spanCount = 3;
        GridLayoutManager manager = new GridLayoutManager(context, spanCount);
        mDataBinding.recyclerView.setLayoutManager(manager);
        mDataBinding.recyclerView.setItemAnimator(null);

        int screenWidth = PixUtils.getScreenWidth();
        int availableWidth = screenWidth - getPaddingStart() - getPaddingEnd();
        //间隔
        mConfig.itemSize = PixUtils.dp2px(80);
        mConfig.availableSize = availableWidth;

        mAdapter.setConfig(mConfig);
        addLastItem();
    }

    /**
     * 添加item
     */
    public void addItem(GridItem gridItem) {
        List<GridItem> currentList = mAdapter.getCurrentList();
        List<GridItem> newList = new ArrayList<>();
        if (currentList.size() > 0) {
            newList.addAll(currentList);
            newList.add(currentList.size() - 1, gridItem);
        } else {
            newList.add(gridItem);
        }
        mAdapter.submitList(newList);
    }

    //获取所有图片
    public List<GridItem> getItems() {
        List<GridItem> currentList = mAdapter.getCurrentList();
        if (currentList.size() > 0) {
            GridItem item = currentList.get(currentList.size() - 1);
            return item.type == GridItem.TYPE_ADD_IMG ? currentList.subList(0, currentList.size() - 1) : currentList;
        }
        return currentList;
    }

    //获取当前item个数
    public int getItemsCount() {
        List<GridItem> currentList = mAdapter.getCurrentList();
        if (currentList.size() > 0) {
            GridItem item = currentList.get(currentList.size() - 1);
            return item.type == GridItem.TYPE_ADD_IMG ? currentList.size() - 1 : currentList.size();
        }
        return 0;
    }

    public void addLastItem() {
        List<GridItem> list = new ArrayList<>();
        list.add(new GridItem(GridItem.TYPE_ADD_IMG));
        mAdapter.submitList(list);
    }

    static class ItemDecoration extends RecyclerView.ItemDecoration {
        private int offset;

        ItemDecoration(int offset) {
            this.offset = offset;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = offset;
            outRect.bottom = offset;
        }
    }
}
