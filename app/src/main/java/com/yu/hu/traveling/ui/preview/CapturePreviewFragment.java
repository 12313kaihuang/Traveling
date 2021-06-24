package com.yu.hu.traveling.ui.preview;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.util.Util;
import com.yu.hu.common.fragment.BaseFragment;
import com.yu.hu.libnavannotation.FragmentDestination;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.FragmentPreviewBinding;
import com.yu.hu.traveling.ui.capture.CaptureViewModel;

import java.io.File;

/**
 * @author Hy
 * created on 2020/04/25 9:31
 * <p>
 * 本机 拍照/录制视频 后 预览
 **/
@FragmentDestination(pageUrl = "preview/capture")
public class CapturePreviewFragment extends BaseFragment<FragmentPreviewBinding> {

    public static final String PAGE_URL = "preview/capture";

    private SimpleExoPlayer player;
    private CaptureViewModel captureViewModel;

    @Override
    protected void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        captureViewModel = new ViewModelProvider(mActivity).get(CaptureViewModel.class);

        mDataBinding.setViewModel(captureViewModel);
        if (captureViewModel.isVideo()) {
            previewVideo(captureViewModel.getPreviewUrl());
        } else {
            previewImg(captureViewModel.getPreviewUrl());
        }
    }

    /**
     * 预览图片
     */
    private void previewImg(String previewUrl) {
        Glide.with(requireContext()).load(previewUrl).into(mDataBinding.photoView);
    }

    /**
     * 预览视频
     */
    private void previewVideo(String previewUrl) {
        player = ExoPlayerFactory.newSimpleInstance(mContext, new DefaultRenderersFactory(mContext), new DefaultTrackSelector(), new DefaultLoadControl());

        //判断previewUrl是网络资源还是本地资源
        Uri uri = null;
        File file = new File(previewUrl);
        if (file.exists()) {
            //本地资源
            DataSpec dataSpec = new DataSpec(Uri.fromFile(file));
            FileDataSource fileDataSource = new FileDataSource();
            try {
                fileDataSource.open(dataSpec);
                uri = fileDataSource.getUri();
            } catch (FileDataSource.FileDataSourceException e) {
                e.printStackTrace();
            }
        } else {
            //网络资源
            uri = Uri.parse(previewUrl);
        }

        ProgressiveMediaSource.Factory factory = new ProgressiveMediaSource.Factory(new DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, mActivity.getPackageName())));
        ProgressiveMediaSource mediaSource = factory.createMediaSource(uri);
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
        mDataBinding.playerView.setPlayer(player);
    }

    @Override
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onInitEvents(view, savedInstanceState);
        mDataBinding.finishBtn.setOnClickListener(v -> onFinishBtnClicked());
        mDataBinding.closeBtn.setOnClickListener(v -> ((MainActivity) mActivity).popBackStack());
    }

    private void onFinishBtnClicked() {
        captureViewModel.setFromPreview(true);
        ((MainActivity) mActivity).popBackStack();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (player != null) {
            player.setPlayWhenReady(!hidden);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.setPlayWhenReady(false);
            player.stop(true);
            player.release();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_preview;
    }
}
