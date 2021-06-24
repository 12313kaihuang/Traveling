package com.yu.hu.traveling.network;

import com.yu.hu.libnetwork.ApiResponse;
import com.yu.hu.traveling.model.BottomBar;
import com.yu.hu.traveling.model.HomeTab;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author Hy
 * created on 2020/04/28 18:57
 * <p>
 * 配置信息拉取
 **/
public interface ConfigService {

    @GET("config/bottomTabConfig")
    Call<ApiResponse<BottomBar>> getBottomBarConfig();

    @GET("config/homeTabConfig")
    Call<ApiResponse<HomeTab>> getHomeTabConfig();
}
