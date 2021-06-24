package com.yu.hu.traveling.exoplayer;

import android.annotation.SuppressLint;
import android.app.Application;
import android.view.LayoutInflater;

import com.blankj.utilcode.util.Utils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.yu.hu.traveling.R;

/**
 * @author Hy
 * created on 2020/04/26 22:56
 * <p>
 * 视频播放器
 **/
public class MediaPlayer {
    //播放器对象
    public SimpleExoPlayer exoPlayer;

    //用于展示视频画面
    public PlayerView playerView;

    //视频画面播放器
    public PlayerControlView playerControlView;

    @SuppressLint("InflateParams")
    public MediaPlayer() {
        Application context = Utils.getApp();

        //创建exoplayer播放器实例
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context,
                //视频每一这的画面如何渲染,实现默认的实现类
                new DefaultRenderersFactory(context),
                //视频的音视频轨道如何加载,使用默认的轨道选择器
                new DefaultTrackSelector(),
                //视频缓存控制逻辑,使用默认的即可
                new DefaultLoadControl());

        //加载咱们布局层级优化之后的能够展示视频画面的View
        playerView = (PlayerView) LayoutInflater.from(context).inflate(R.layout.layout_exo_player_view, null);
        //加载咱们布局层级优化之后的视频播放控制器
        playerControlView = (PlayerControlView) LayoutInflater.from(context).inflate(R.layout.layout_exo_player_controller_view, null);

        //别忘记 把播放器实例 和 playerView，controlView相关联
        //如此视频画面才能正常显示,播放进度条才能自动更新
        playerView.setPlayer(exoPlayer);
        playerControlView.setPlayer(exoPlayer);
    }

    /**
     * 释放资源
     */
    public void release() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }

        if (playerView != null) {
            playerView.setPlayer(null);
            playerView = null;
        }

        if (playerControlView != null) {
            playerControlView.setPlayer(null);
            playerControlView.setVisibilityListener(null);
            playerControlView = null;
        }
    }
}
