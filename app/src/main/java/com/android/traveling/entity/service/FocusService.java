package com.android.traveling.entity.service;

import com.android.traveling.entity.msg.Msg;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.entity.service
 * 文件名：FocusService
 * 创建者：HY
 * 创建时间：2019/3/9 21:03
 * 描述：  +关注/取消关注 retrofit接口
 */

public interface FocusService {

    /**
     * 添加关注
     *
     * @param fromId fromId
     * @param toId   toId
     * @return Msg
     */
    @POST("addFocus")
    Call<Msg> addFocus(@Query("fromId") int fromId, @Query("toId") int toId);

    /**
     * 取消关注
     *
     * @param fromId fromId
     * @param toId   toId
     * @return Msg
     */
    @POST("cancelFocus")
    Call<Msg> cancelFocus(@Query("fromId") int fromId, @Query("toId") int toId);

}
