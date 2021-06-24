package com.yu.hu.ninegridlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.yu.hu.ninegridlayout.adapter.GridItemAdapter;
import com.yu.hu.ninegridlayout.databinding.LayoutNineGridBinding;
import com.yu.hu.ninegridlayout.entity.GridItem;
import com.yu.hu.ninegridlayout.utils.GridItemDecoration;
import com.yu.hu.ninegridlayout.utils.PixUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author Hy
 * created on 2020/04/17 20:52
 * <p>
 * 实现九宫格展示图片/视频
 * <p>
 * 1、当只有1张图时，可以自己定制图片宽高，也可以使用默认九宫格的宽高；
 * 2、当只有4张图时，以2*2的方式显示
 * 3. 其他情况 3*3显示，如果超过9张图，则在最后一张图片上面带一个加号
 * @see #setItems(List)  构建{@link GridItem}数据集并传入即可
 **/
public class NineGridView extends LinearLayout {

    public static final int DEFAULT_MAX_COUNT = 9;

    private static final int DEFAULT_SPAN_SPACING = 8; //8dp

    private Context mContext;
    private GridItemAdapter mAdapter;
    private LayoutNineGridBinding mDataBinding;

    private final Config mConfig;
    private GridItemDecoration itemDecoration;
    private List<GridItem> saveItems;

    public void setOnItemClickListener(OnGridItemClickListener itemClickListener) {
        mAdapter.setItemClickListener(itemClickListener);
    }

    public NineGridView(Context context) {
        this(context, null);
    }

    public NineGridView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineGridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mConfig = new Config();
        mAdapter = new GridItemAdapter();
        itemDecoration = new GridItemDecoration(PixUtils.dp2px(mConfig.spanSpacing));
        mDataBinding = LayoutNineGridBinding.inflate(LayoutInflater.from(context), this, true);

        //itemDecoration只设置一次就行了 不能放在setItems里  否则多次set后布局会错乱
        mDataBinding.recyclerView.addItemDecoration(itemDecoration);
        mDataBinding.recyclerView.setAdapter(mAdapter);
        mDataBinding.recyclerView.setNestedScrollingEnabled(false); //禁止滑动
    }

    /**
     * 设置item对象
     */
    public void setItems(@NonNull List<GridItem> items) {
        if (items.size() == 0) {
            setVisibility(GONE);
            return;
        }
        if (hasLoaded(items)) {
            setVisibility(VISIBLE);
            Log.i("NineGridView", "items has Set");
            return;
        }

        mConfig.reset(items);
        this.saveItems = items;
        if (getVisibility() != VISIBLE) setVisibility(VISIBLE);

        int screenWidth = PixUtils.getScreenWidth();
        int availableWidth = screenWidth - getPaddingStart() - getPaddingEnd();
        //间隔
        int spacingSize = PixUtils.dp2px(mConfig.spanSpacing);
        int spanCount = items.size() == 1 ? 1 : (items.size() == 4 ? 2 : 3);
        if (spanCount == 1) {
            mConfig.availableSize = mConfig.itemSize = availableWidth - spacingSize * 2;
        } else {
            mConfig.itemSize = (availableWidth - spacingSize * (spanCount + 1)) / spanCount;
            mConfig.availableSize = availableWidth;
        }
        Log.d("NineGridView", "setItems: screenWidth = " + screenWidth
                + " - availableWidth = " + availableWidth
                + " - spacingSize = " + spacingSize
                + " - itemSize  =" + mConfig.itemSize);

        GridLayoutManager manager = new GridLayoutManager(mContext, spanCount);
        mDataBinding.recyclerView.setLayoutManager(manager);
        mDataBinding.recyclerView.setItemAnimator(null);
        // mDataBinding.recyclerView.addItemDecoration(new GridItemDecoration(spacingSize));

        mAdapter.setConfig(mConfig);
        mAdapter.submitList(items);
        invalidate();
        requestLayout();
    }

    /**
     * 判断是否真的需要重新加载，
     * 为优化界面刷新时调用setItems方法导致页面刷新问题
     *
     * @param items items
     */
    private boolean hasLoaded(@NonNull List<GridItem> items) {
        if (saveItems == null || saveItems.size() != items.size()) {
            return false;
        }
        for (int i = 0; i < saveItems.size(); i++) {
            if (!Objects.equals(saveItems.get(i), items.get(i))) return false;
        }
        return true;
    }

    /**
     * 设置item间隔
     */
    @SuppressWarnings("unused")
    public NineGridView setSpanSpacing(int dpValue) {
        mConfig.spanSpacing = dpValue;
        itemDecoration.changeOffset(dpValue);
        return this;
    }

    @SuppressWarnings("unused")
    public Config getConfig() {
        return mConfig;
    }

    //存储配置信息
    public static class Config {
        public int maxCount = DEFAULT_MAX_COUNT;
        public int spanSpacing = DEFAULT_SPAN_SPACING;  //dp单位
        public int availableSize; //除去屏幕padding和spacing后剩下的大小
        public int itemSize;
        public int itemCount;

        //当只有一张图片或视频时用于记录其宽高
        public int width;
        public int height;

        //添加按钮的资源id
        @DrawableRes
        public int drawableResource;

        @SuppressWarnings("WeakerAccess")
        public Config() {
        }

        @SuppressWarnings("WeakerAccess")
        public Config(int spanSpacing) {
            this.spanSpacing = spanSpacing;
        }

        void reset(@NonNull List<GridItem> items) {
            this.maxCount = DEFAULT_MAX_COUNT;
            this.availableSize = 0;
            this.itemSize = 0;
            this.itemCount = 0;
            this.width = 0;
            this.height = 0;
            setItems(items);
        }

        void setItems(@NonNull List<GridItem> items) {
            itemCount = items.size();
            if (items.size() == 0) {
                return;
            }
            GridItem item = items.get(0);
            if (item != null) {
                width = item.width;
                height = item.height;
            }
        }

        void setDrawableResource(int drawableResource) {
            this.drawableResource = drawableResource;
        }
    }
}
