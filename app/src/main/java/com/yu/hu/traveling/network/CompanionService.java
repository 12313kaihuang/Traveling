package com.yu.hu.traveling.network;

import com.yu.hu.libnetwork.ApiResponse;
import com.yu.hu.traveling.model.Comment;
import com.yu.hu.traveling.model.Companion;
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
 * created on 2020/05/05 12:21
 **/
public interface CompanionService {

    @FormUrlEncoded
    @POST("companion/insert")
    Call<ApiResponse<Companion>> insert(@Field("title") String title, @Field("content") String content,
                                        @Field("target") String target, @Field("targetTime") long targetTime, @Field("userId") int userId);

    @FormUrlEncoded
    @POST("companion/delete")
    Call<ApiResponse<Boolean>> delete(@Field("companionId") int companionId);

    /**
     * @param startId   起始搜索的noteId
     * @param pageCount 当前页数量
     * @param userId    userId，没有登录就传{@code 0}
     */
    @GET("companion/getCompanions")
    Call<ApiResponse<List<Companion>>> getCompanions(@Query("tag") String tag, @Query("startId") int startId
            , @Query("pageCount") int pageCount, @Query("userId") int userId);

    @GET("companion/getCompanions")
    Call<ApiResponse<List<Companion>>> getTargetCompanions(@Query("tag") String tag, @Query("startId") int startId
            , @Query("pageCount") int pageCount, @Query("userId") int userId, @Query("requestUserId") int requestUserId);

    //获取评论
    @GET("companion/getComments")
    Call<ApiResponse<List<Comment>>> getComments(@Query("companionId") int companionId, @Query("startId") int startId,
                                                 @Query("pageCount") int pageCount, @Query("userId") int userId);

    @FormUrlEncoded
    @POST("interaction/toggleCompanionLike")
    Call<ApiResponse<Ugc>> toggleLike(@Field("itemId") int companionId, @Field("userId") int userId);

    @FormUrlEncoded
    @POST("interaction/toggleCompanionCollect")
    Call<ApiResponse<Ugc>> toggleCollect(@Field("itemId") int companionId, @Field("userId") int userId);
}
