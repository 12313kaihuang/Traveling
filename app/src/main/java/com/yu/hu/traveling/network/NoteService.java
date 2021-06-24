package com.yu.hu.traveling.network;

import com.yu.hu.libnetwork.ApiResponse;
import com.yu.hu.traveling.model.Comment;
import com.yu.hu.traveling.model.Note;
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
 * created on 2020/04/29 22:27
 **/
public interface NoteService {

    @FormUrlEncoded
    @POST("note/insert")
    Call<ApiResponse<Note>> insert(@Field("title") String title, @Field("content") String content
            , @Field("type") int type, @Field("userId") int userId, @Field("imgs") String imgs);

    @FormUrlEncoded
    @POST("note/delete")
    Call<ApiResponse<Boolean>> delete(@Field("noteId") int noteId);

    /**
     * 根据{@param tag}搜索指定类型的游记/攻略
     *
     * @param tag       tag eg:{@code focus}
     * @param startId   起始搜索的noteId
     * @param pageCount 当前页数量
     * @param userId    userId，没有登录就传{@code 0}
     */
    @FormUrlEncoded
    @POST("note/getNotes")
    Call<ApiResponse<List<Note>>> getNotes(@Field("tag") String tag, @Field("startId") int startId
            , @Field("pageCount") int pageCount, @Field("userId") int userId);

    /**
     * @param requestUserId 当前登录的用户id
     */
    @FormUrlEncoded
    @POST("note/getNotes")
    Call<ApiResponse<List<Note>>> getTargetNotes(@Field("tag") String tag, @Field("startId") int startId
            , @Field("pageCount") int pageCount, @Field("userId") int userId, @Field("requestUserId") int requestUserId);

    //搜索
    @FormUrlEncoded
    @POST("note/searchNotes")
    Call<ApiResponse<List<Note>>> searchNotes(@Field("content") String content, @Field("startId") int startId
            , @Field("pageCount") int pageCount, @Field("userId") int userId);

    @GET("note/getComments")
    Call<ApiResponse<List<Comment>>> getComments(@Query("noteId") int noteId, @Query("startId") int startId,
                                                 @Query("pageCount") int pageCount, @Query("userId") int userId);

    @FormUrlEncoded
    @POST("interaction/toggleNoteLike")
    Call<ApiResponse<Ugc>> toggleNoteLike(@Field("itemId") int noteId, @Field("userId") int userId);

    @FormUrlEncoded
    @POST("interaction/toggleNoteDiss")
    Call<ApiResponse<Ugc>> toggleNoteDiss(@Field("itemId") int noteId, @Field("userId") int userId);

    @FormUrlEncoded
    @POST("interaction/toggleNoteCollect")
    Call<ApiResponse<Ugc>> toggleNoteCollect(@Field("noteId") int noteId, @Field("userId") int userId);

    @FormUrlEncoded
    @POST("interaction/share")
    Call<ApiResponse<Ugc>> share(@Field("noteId") int noteId);


}
