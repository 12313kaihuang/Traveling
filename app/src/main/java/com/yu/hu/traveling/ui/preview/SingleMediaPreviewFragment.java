package com.yu.hu.traveling.ui.preview;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.yu.hu.common.fragment.BaseFragment;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.ninegridlayout.entity.GridItem;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.FragmentSimpleMediaPreviewBinding;

/**
 * @author Hy
 * created on 2020/04/27 11:16
 * <p>
 * 单张图片/视频的预览
 * @see MediaPreviewFragment
 **/
public class SingleMediaPreviewFragment extends BaseFragment<FragmentSimpleMediaPreviewBinding> {

    private static final String KEY_ITEM = "key_item";

    private GridItem gridItem;
    private boolean isVideo;

    public static SingleMediaPreviewFragment newInstance(GridItem item) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_ITEM, item);
        SingleMediaPreviewFragment fragment = new SingleMediaPreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void beforeInitView() {
        super.beforeInitView();
        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }
        gridItem = arguments.getParcelable(KEY_ITEM);
    }

    @Override
    protected void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        if (gridItem == null) {
            return;
        }

        isVideo = !TextUtils.isEmpty(gridItem.url);
        mDataBinding.setIsVideo(isVideo);
        if (isVideo) {
            mDataBinding.mediaView.bindData(gridItem.width, gridItem.height, gridItem.cover, gridItem.url);
        } else {
            Glide.with(requireContext()).load(gridItem.cover).into(mDataBinding.photoView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d(" SingleMediaPreviewFragment - onResume");
        if (isVideo) {
            mDataBinding.mediaView.onActive();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d(" SingleMediaPreviewFragment - onPause");
        if (isVideo) {
            mDataBinding.mediaView.inActive();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(" SingleMediaPreviewFragment - onDestroy");
        if (isVideo) {
            mDataBinding.mediaView.release();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_simple_media_preview;
    }
}
