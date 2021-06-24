package com.yu.hu.ninegridlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.yu.hu.ninegridlayout.adapter.ImgItemAdapter;
import com.yu.hu.ninegridlayout.databinding.LayoutHorizonImgViewBinding;
import com.yu.hu.ninegridlayout.entity.GridItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hy
 * created on 2020/04/18 23:53
 * <p>
 * 水平ViewPager展示图片
 **/
@SuppressWarnings("unused")
public class HorizonImgView extends FrameLayout {

    private LayoutHorizonImgViewBinding mDataBinding;
    private List<GridItem> mImgList;
    private ImgItemAdapter mAdapter;

    public void setOnItemClickListener(OnGridItemClickListener itemClickListener) {
        mAdapter.setItemClickListener(itemClickListener);
    }

    public HorizonImgView(@NonNull Context context) {
        this(context, null);
    }

    public HorizonImgView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizonImgView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public HorizonImgView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mImgList = new ArrayList<>();
        mAdapter = new ImgItemAdapter();
        mDataBinding = LayoutHorizonImgViewBinding.inflate(LayoutInflater.from(context), this, true);
        mDataBinding.viewPager.setAdapter(mAdapter);
        mDataBinding.viewPager.setOffscreenPageLimit(3);
        mDataBinding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                mDataBinding.textView.setText(getResources().getString(R.string.img_count, (position + 1), mImgList.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    public void setImgItems(@NonNull List<GridItem> items) {
        mImgList = items;
        mAdapter.submitList(items);
        mDataBinding.viewPager.setOffscreenPageLimit(items.size());
        mDataBinding.textView.setText(getResources().getString(R.string.img_count, 1, items.size()));
    }


}
