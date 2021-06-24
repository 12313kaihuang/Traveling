package com.yu.hu.traveling.network;

import com.yu.hu.libnetwork.ApiResponse;
import com.yu.hu.traveling.model.Comment;
import com.yu.hu.traveling.model.Ugc;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author Hy
 * created on 2020/04/30 20:28
 **/
public interface CommentService {

    @FormUrlEncoded
    @POST("comment/insert")
    Call<ApiResponse<Comment>> addComment(@Field("itemId") int itemId, @Field("userId") int userId,
                                          @Field("commentType") int commentType, @Field("commentText") String commentText);

    @FormUrlEncoded
    @POST("comment/insert")
    Call<ApiResponse<Comment>> addReply(@Field("itemId") int itemId, @Field("userId") int userId, @Field("commentType") int commentType,
                                        @Field("commentText") String commentText, @Field("replyId") int replyId);

    @FormUrlEncoded
    @POST("comment/delete")
    Call<ApiResponse<Boolean>> deleteComment(@Field("id") int commentId);

    @GET("interaction/toggleCommentLike")
    Call<ApiResponse<Ugc>> toggleCommentLike(@Query("commentId") int commentId, @Query("userId") int userId);

    @GET("comment/getComments")
    Call<ApiResponse<List<Comment>>> getComments(@Query("type") int type, @Query("itemId") int itemId, @Query("startId") int startId,
                                                 @Query("pageCount") int pageCount, @Query("userId") int userId);
}
