package com.yu.hu.traveling.network;

import androidx.lifecycle.LiveData;

import com.yu.hu.traveling.model.BingImg;

import retrofit2.http.GET;

/**
 * @author Hy
 * created on 2020/04/14 14:25
 * <p>
 * 启动页请求
 **/
public interface SplashService {

    @GET("HPImageArchive.aspx?format=js&idx=0&n=1")
    LiveData<BingImg> getBingImgLiveData();
}
