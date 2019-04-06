package com.android.traveling.entity.service;

import com.android.traveling.entity.msg.Msg;
import com.android.traveling.entity.msg.NoteMsg;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
     * 获取关注的人的最新的游记文章
     *
     * @return 最新的篇文章 NoteMsg
     */
    @GET("getFocusedNotes")
    Call<NoteMsg> getFocusedNewest(@Query("userId") int userId);

    /**
     * 加载更多游记文章
     *
     * @param lastId 当前所显示的最旧的一篇文章的id
     * @return 5篇更多文章 NoteMsg
     */
    @GET("loadMore")
    Call<NoteMsg> loadMore(@Query("lastId") int lastId);

    /**
     * 加载更多游记文章（关注的人的）
     *
     * @param lastId 当前所显示的最旧的一篇文章的id
     * @return 5篇更多文章 NoteMsg
     */
    @GET("getMoreFocusedNotes")
    Call<NoteMsg> loadMoreFocused(@Query("userId") int userId, @Query("lastId") int lastId);

    /**
     * 模糊查询最新的标题含有 content 的10篇文章
     *
     * @param content content
     * @return NoteMsg
     */
    @GET("searchNoteHazily")
    Call<NoteMsg> searchNoteHazily(@Query("content") String content);

    /**
     * 模糊查询更多游记文章
     *
     * @param lastId 当前所显示的最旧的一篇文章的id
     * @return 10篇更多文章 NoteMsg
     */
    @GET("searchMoreNoteHazily")
    Call<NoteMsg> searchMoreNoteHazily(@Query("content") String content, @Query("lastId") int lastId);

    /**
     * 更新点赞数
     *
     * @param noteId   文章id
     * @param likeList 点赞list
     * @return Msg
     */
    @POST("updateLikeNum")
    Call<Msg> updateLikeNum(@Query("noteId") int noteId, @Query("likeList") String likeList);


    /**
     * 发表游记/攻略
     *
     * @param part part
     * @return Msg
     */
    @Multipart
    @POST("createNote")
    Call<NoteMsg> createNote(@Query("userId") int userId, @Query("tag") int tag
            , @Query("title") String title, @Query("content") String content,
                             @Query("createTime") String createTime, @Part MultipartBody.Part part);

    /**
     * 发表没有图片的游记/攻略
     *
     * @return Call<NoteMsg>
     */
    @POST("createNoteWithoutPicture")
    Call<NoteMsg> createNoteWithoutPicture(@Query("userId") int userId, @Query("tag") int tag
            , @Query("title") String title, @Query("content") String content,
                                           @Query("createTime") String createTime);

}
