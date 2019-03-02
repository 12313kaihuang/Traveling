package com.android.traveling.entity.note;

import com.android.traveling.entity.msg.BaseCommentMsg;
import com.android.traveling.entity.msg.CommentMsg;
import com.android.traveling.entity.msg.Msg;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.entity.note
 * 文件名：CommentService
 * 创建者：HY
 * 创建时间：2019/2/25 20:43
 * 描述：  评论 retrofit接口
 */

public interface CommentService {

    /**
     * 获取文章所有评论
     * @param noteId 文章id
     * @return 所有评论
     */
    @GET("getComments")
    Call<CommentMsg> getComments(@Query("noteId")int noteId);

    /**
     * 添加评论
     * @param paramsMap 将BaseComment封装成键值对传递
     * @return BaseCommentMsg
     */
    @FormUrlEncoded
    @POST("addComment")
    Call<BaseCommentMsg> addComment(@FieldMap Map<String,String> paramsMap);

    /**
     * 删除评论
     * @param id 评论id
     * @return Msg
     */
    @GET("deleteComment")
    Call<Msg> deleteComment(@Query("id") int id);

    /**
     * 删除回复
     * @param id 评论id
     * @return Msg
     */
    @GET("deleteReply")
    Call<Msg> deleteReply(@Query("id")int id);
}
