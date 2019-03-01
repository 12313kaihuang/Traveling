package com.android.traveling.entity.note;

import com.android.traveling.entity.msg.Msg;
import com.android.traveling.entity.msg.NoteMsg;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.entity.note
 * 文件名：NoteService
 * 创建者：HY
 * 创建时间：2019/1/22 11:32
 * 描述：  retrofit接口
 */

public interface NoteService {

    /**
     * 获取最新的游记文章
     *
     * @return 最新的篇文章 NoteMsg
     */
    @GET("news")
    Call<NoteMsg> getNewest();

    /**
     * 加载更多游记文章
     * @param lastId 当前所显示的最旧的一篇文章的id
     * @return 5篇更多文章 NoteMsg
     */
    @GET("loadMore")
    Call<NoteMsg> loadMore(@Query("lastId") int lastId);

    /**
     * 更新点赞数
     * @param noteId 文章id
     * @param likeList 点赞list
     * @return Msg
     */
    @POST("updateLikeNum")
    Call<Msg> updateLikeNum(@Query("noteId") int noteId, @Query("likeList") String likeList);
}
