package com.android.traveling.entity.service;

import com.android.traveling.entity.msg.CompanionMsg;
import com.android.traveling.entity.msg.CompanionReplyMsg;
import com.android.traveling.entity.msg.Msg;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.entity.companion
 * 文件名：CompanionService
 * 创建者：HY
 * 创建时间：2019/3/5 8:08
 * 描述：  结伴 retrofit接口
 */

public interface CompanionService {

    /**
     * 获取到最新的10篇结伴信息
     *
     * @return 最新的10篇结伴信息
     */
    @GET("newsCompanion")
    Call<CompanionMsg> getNewsCompanion();

    /**
     * 加载更多的结伴信息
     *
     * @param lastId 当前所显示的最旧的一结伴信息的id
     * @return 10条结伴信息
     */
    @GET("loadMoreCompanions")
    Call<CompanionMsg> loadMoreCompanions(@Query("lastId") int lastId);

    /**
     * 增加浏览量
     *
     * @param companionId 结伴消息id
     * @return Msg
     */
    @POST("addBrowseNum")
    Call<Msg> addBrowseNum(@Query("companionId") int companionId);

    /**
     * 通过结伴信息id来查找与之相关的所有评论
     *
     * @param companionId 结伴信息id
     * @return Msg
     */
    @GET("getCompanionComments")
    Call<CompanionReplyMsg> getCompanionComments(@Query("companionId") int companionId);
}
