package com.yu.hu.traveling.network;

import com.yu.hu.libnetwork.ApiResponse;
import com.yu.hu.traveling.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author Hy
 * created on 2020/04/29 11:26
 **/
public interface UserService {

    @FormUrlEncoded
    @POST("user/login")
    Call<ApiResponse<User>> login(@Field("user") String user);

    @FormUrlEncoded
    @POST("user/getUserInfo")
    Call<ApiResponse<User>> getUserInfo(@Field("userId") int userId);

    @GET("user/getUserLikeCount")
    Call<ApiResponse<Integer>> getUserLikeCount(@Query("userId") int userId);

    @GET("user/getBaseUserInfo")
    Call<ApiResponse<User>> getBaseUserInfo(@Query("userId") int userId);

    @FormUrlEncoded
    @POST("user/focus")
    Call<ApiResponse<Boolean>> focus(@Field("focusId") int focusId, @Field("followerId") int followerId);
}
