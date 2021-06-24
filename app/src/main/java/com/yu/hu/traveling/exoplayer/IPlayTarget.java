package com.yu.hu.traveling.exoplayer;

import android.view.ViewGroup;

/**
 * @author Hy
 * created on 2020/04/27 9:13
 * <p>
 */
@SuppressWarnings("unused")
public interface IPlayTarget {

    ViewGroup getOwner();

    //活跃状态 视频可播放
    void onActive();

    //非活跃状态，暂停它
    void inActive();


    boolean isPlaying();
}
