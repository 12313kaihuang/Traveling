package com.yu.hu.traveling.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.blankj.utilcode.util.BarUtils;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.common.view.CircleImageView;
import com.yu.hu.ninegridlayout.utils.PixUtils;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.LayoutMediaViewBinding;
import com.yu.hu.traveling.exoplayer.ExoPlayerManager;
import com.yu.hu.traveling.exoplayer.IPlayTarget;
import com.yu.hu.traveling.exoplayer.MediaPlayer;

/**
 * @author Hy
 * created on 2020/04/26 21:29
 * <p>
 * 视频 预览View
 **/
public class MediaView extends FrameLayout
        implements IPlayTarget, PlayerControlView.VisibilityListener, Player.EventListener {

    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    private LayoutMediaViewBinding mDataBinding;

    private int playState = -1; //用于记录是否已经缓存出视频了

    private int widthPx;
    private int heightPx;
    protected String mVideoUrl;  //需要播放的视频url
    private String mediaSourceUrl;  //用于存储mediaSource创建时的url  重新创建时如果已经创建过就不需要再创建了

    public MediaView(@NonNull Context context) {
        this(context, null);
    }

    public MediaView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @SuppressLint("InflateParams")
    public MediaView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mDataBinding = LayoutMediaViewBinding.inflate(LayoutInflater.from(context), this, true);
        setBackgroundColor(Color.parseColor("#000000"));
        mediaPlayer = new MediaPlayer();

        mDataBinding.playBtn.setOnClickListener(v -> {
            if (isPlaying()) {
                inActive();
            } else {
                onActive();
            }
        });
    }

    public void bindData(int widthPx, int heightPx, String coverUrl, String videoUrl) {
        this.widthPx = widthPx;
        this.heightPx = heightPx;
        mVideoUrl = videoUrl;
        mDataBinding.cover.setImageUrl(coverUrl);
        LogUtil.d("MediaView width = " + widthPx + ", height = " + heightPx);
        //如果该视频的宽度小于高度,则高斯模糊背景图显示出来
        if (widthPx < heightPx) {
            CircleImageView.setBlurImageUrl(mDataBinding.blurBackground, coverUrl, 10);
            mDataBinding.blurBackground.setVisibility(VISIBLE);
        } else {
            mDataBinding.blurBackground.setVisibility(INVISIBLE);
            //宽大于高则等比缩放
            fitSize(widthPx, heightPx);
        }
    }

    @SuppressWarnings("unused")
    @BindingAdapter("media_video_url")
    public static void setMediaVideoUrl(@NonNull MediaView mediaView, String media_video_url) {
        if (TextUtils.isEmpty(media_video_url)) {
            return;
        }
        mediaView.setVideoUrl(media_video_url);
    }

    public void setVideoUrl(String videoUrl) {
        this.mVideoUrl = videoUrl;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //点击该区域时 我们诸主动让视频控制器显示出来
        mediaPlayer.playerControlView.show();
        return true;
    }

    @Override
    public ViewGroup getOwner() {
        return this;
    }

    //视频播放,或恢复播放
    @Override
    public void onActive() {
        if (TextUtils.isEmpty(mVideoUrl)) {
            return;
        }
        PlayerView playerView = mediaPlayer.playerView;
        PlayerControlView controlView = mediaPlayer.playerControlView;
        SimpleExoPlayer exoPlayer = mediaPlayer.exoPlayer;

        if (playerView == null) {
            return;
        }

        ViewParent parent = playerView.getParent();
        if (parent != this) {
            //把展示视频画面的View添加到ItemView的容器上
            if (parent != null) {
                ((ViewGroup) parent).removeView(playerView);
                //还应该暂停掉列表上正在播放的那个
                ((MediaView) parent).inActive();
            }

            ViewGroup.LayoutParams coverParams = mDataBinding.cover.getLayoutParams();
            this.addView(playerView, 1, coverParams);
        }

        ViewParent ctrlParent = controlView.getParent();
        if (ctrlParent != this) {
            //把视频控制器 添加到ItemView的容器上
            if (ctrlParent != null) {
                ((ViewGroup) ctrlParent).removeView(controlView);
            }
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.BOTTOM;
            this.addView(controlView, params);
        }

        //如果是同一个视频资源,则不需要从重新创建mediaSource。
        //但需要onPlayerStateChanged 否则不会触发onPlayerStateChanged()
        if (mVideoUrl.equals(mediaSourceUrl)) {
            onPlayerStateChanged(true, Player.STATE_READY);
        } else {
            MediaSource mediaSource = ExoPlayerManager.createMediaSource(mVideoUrl);
            exoPlayer.prepare(mediaSource);
            //循环播放
            exoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
            mediaSourceUrl = mVideoUrl;
        }

        controlView.show();
        controlView.setVisibilityListener(this);
        exoPlayer.addListener(this);
        exoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void inActive() {
        //暂停视频的播放并让封面图和 开始播放按钮 显示出来
        if (mediaPlayer.exoPlayer == null || mediaPlayer.playerControlView == null)
            return;
        mediaPlayer.exoPlayer.setPlayWhenReady(false);
        mediaPlayer.playerControlView.setVisibilityListener(null);
        mediaPlayer.exoPlayer.removeListener(this);
        if (playState == -1) {
            mDataBinding.cover.setVisibility(VISIBLE);
        }
        mDataBinding.playBtn.setVisibility(VISIBLE);
        mDataBinding.playBtn.setImageResource(R.drawable.icon_video_play);
    }

    public void release() {
        mediaPlayer.release();
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    //监听ControllerView的显隐藏
    @Override
    public void onVisibilityChange(int visibility) {
        //播放按钮跟controllerView同步显示/异常
        mDataBinding.playBtn.setVisibility(visibility);
        mDataBinding.playBtn.setImageResource(isPlaying() ? R.drawable.icon_video_pause : R.drawable.icon_video_play);
    }

    //监听视频播放的状态
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        SimpleExoPlayer exoPlayer = mediaPlayer.exoPlayer;
        if (playbackState == Player.STATE_READY && exoPlayer.getBufferedPosition() != 0 && playWhenReady) {
            playState = playbackState;
            mDataBinding.cover.setVisibility(GONE);
            mDataBinding.bufferView.setVisibility(GONE);
        } else if (playbackState == Player.STATE_BUFFERING) {
            mDataBinding.bufferView.setVisibility(VISIBLE);
        }
        isPlaying = playbackState == Player.STATE_READY && exoPlayer.getBufferedPosition() != 0 && playWhenReady;
        mDataBinding.playBtn.setImageResource(isPlaying() ? R.drawable.icon_video_pause : R.drawable.icon_video_play);
    }

    //如果显示在list中 用于做状态重置
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isPlaying = false;
        mDataBinding.bufferView.setVisibility(GONE);
        mDataBinding.cover.setVisibility(VISIBLE);
        mDataBinding.playBtn.setVisibility(VISIBLE);
        mDataBinding.playBtn.setImageResource(R.drawable.icon_video_play);
    }

    //重写此方法 每次设置LayoutParams时重新设置高度
    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        if (heightPx > widthPx) {
            int layoutHeight = params.height;

            ViewGroup.LayoutParams coverLayoutParams = mDataBinding.cover.getLayoutParams();
            coverLayoutParams.width = (int) (widthPx / (heightPx * 1.0f / layoutHeight));
            coverLayoutParams.height = layoutHeight;
            mDataBinding.cover.setLayoutParams(coverLayoutParams);

            if (mediaPlayer.playerView != null) {
                ViewGroup.LayoutParams layoutParams = mediaPlayer.playerView.getLayoutParams();
                if (layoutParams != null && layoutParams.width > 0 && layoutParams.height > 0) {
                    float scalex = coverLayoutParams.width * 1.0f / layoutParams.width;
                    float scaley = coverLayoutParams.height * 1.0f / layoutParams.height;

                    mediaPlayer.playerView.setScaleX(scalex);
                    mediaPlayer.playerView.setScaleY(scaley);
                }
            }
        }
        super.setLayoutParams(params);
    }

    private void fitSize(int widthPx, int heightPx) {
        //这里主要是做视频宽大与高,或者高大于宽时  视频的等比缩放
        int maxWidth = PixUtils.getScreenWidth();
        int maxHeight = PixUtils.getScreenHeight();

        int statusBarHeight = BarUtils.getStatusBarHeight();
        int navBarHeight = BarUtils.getNavBarHeight();
        maxHeight += statusBarHeight + navBarHeight;

        int layoutHeight = maxHeight;

        int coverWidth;
        int coverHeight;
        if (widthPx >= heightPx) {
            coverWidth = maxWidth;
            layoutHeight = coverHeight = (int) (heightPx / (widthPx * 1.0f / maxWidth));
        } else {
            coverHeight = maxHeight;
            coverWidth = (int) (widthPx / (heightPx * 1.0f / maxHeight));
        }

        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = maxWidth;
        params.height = layoutHeight;
        setLayoutParams(params);

        ViewGroup.LayoutParams blurParams = mDataBinding.blurBackground.getLayoutParams();
        blurParams.width = maxWidth;
        blurParams.height = layoutHeight;
        mDataBinding.blurBackground.setLayoutParams(blurParams);

        FrameLayout.LayoutParams coverParams = (LayoutParams) mDataBinding.cover.getLayoutParams();
        coverParams.width = coverWidth;
        coverParams.height = coverHeight;
        coverParams.gravity = Gravity.CENTER;
        mDataBinding.cover.setLayoutParams(coverParams);

        FrameLayout.LayoutParams playBtnParams = (LayoutParams) mDataBinding.playBtn.getLayoutParams();
        playBtnParams.gravity = Gravity.CENTER;
        mDataBinding.playBtn.setLayoutParams(playBtnParams);
    }

    public View getPlayController() {
        return mediaPlayer.playerControlView;
    }
}
