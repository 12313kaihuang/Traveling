package com.android.traveling.entity.service;

import com.android.traveling.entity.msg.DetailUserInfoMsg;
import com.android.traveling.entity.msg.LoginMsg;
import com.android.traveling.entity.msg.Msg;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.entity.user
 * 文件名：UserService
 * 创建者：HY
 * 创建时间：2019/1/13 17:43
 * 描述：  retrofit接口
 */

public interface UserService {

    /**
     * 发送验证码
     *
     * @param phoneNumber 手机号
     * @return Msg
     */
    @GET("sendVerifiedCode")
    Call<Msg> sendVerifiedCode(@Query("phoneNumber") String phoneNumber);

    /**
     * 手机验证码登录
     *
     * @param phoneNumber      手机号
     * @param verificationCode 验证码
     * @return LoginMsg
     */
    @POST("loginByCode")
    Call<LoginMsg> loginByCode(@Query("phoneNumber") String phoneNumber, @Query("verificationCode") String verificationCode);

    /**
     * 手机号密码登录
     *
     * @param phoneNumber 手机号
     * @param password    密码
     * @return LoginMsg
     */
    @POST("loginByPass")
    Call<LoginMsg> loginByPass(@Query("phoneNumber") String phoneNumber, @Query("password") String password);

    /**
     * 邮箱密码登录
     *
     * @param email    邮箱
     * @param password 密码
     * @return LoginMsg
     */
    @POST("loginByEmail")
    Call<LoginMsg> loginByEmail(@Query("email") String email, @Query("password") String password);


    /**
     * 更新用户
     *
     * @param id        userId
     * @param nickName  昵称
     * @param signature 签名
     * @param area      所在地
     * @param gender    性别
     * @return Msg
     */
    @POST("updateUser")
    Call<Msg> update(@Query("id") int id, @Query("nickName") String nickName,
                     @Query("signature") String signature, @Query("area") String area,
                     @Query("gender") String gender);

    /**
     * 查询User
     *
     * @param id userId
     * @return LoginMsg
     */
    @GET("findUser")
    Call<LoginMsg> findUser(@Query("id") int id);

    /**
     * 绑定邮箱
     *
     * @param userId userId
     * @param email  邮箱
     * @return Msg
     */
    @GET("bindEmail")
    Call<LoginMsg> bindEmail(@Query("userId") int userId, @Query("email") String email);

    /**
     * * 绑定邮箱和密码
     *
     * @param userId   userId
     * @param email    邮箱
     * @param password 密码
     * @return Msg
     */
    @GET("bindEmailAndPass")
    Call<LoginMsg> bindEmailAndPass(@Query("userId") int userId, @Query("email") String email, @Query("password") String password);


    /**
     * 获取用户详细信息
     *
     * @param userId userId
     * @return DetailUserInfoMsg
     */
    @GET("getDetailUserInfo")
    Call<DetailUserInfoMsg> getDetailUserInfo(@Query("userId") int userId);


    /**
     * 获取用户详细信息
     *
     * @param userId userId
     * @param fromId fromId
     * @return DetailUserInfoMsg
     */
    @GET("getDetailUserInfo")
    Call<DetailUserInfoMsg> getDetailUserInfo(@Query("userId") int userId, @Query("fromId") int fromId);

    /**
     * 设置新密码
     *
     * @param userId  userId
     * @param newPass newPass
     * @return LoginMsg
     */
    @POST("setPassword")
    Call<LoginMsg> setPassword(@Query("userId") int userId, @Query("newPass") String newPass);

    /**
     * 更改密码
     *
     * @param userId  userId
     * @param oldPass oldPass
     * @param newPass newPass
     * @return LoginMsg
     */
    @POST("changePassword")
    Call<LoginMsg> changePassword(@Query("userId") int userId, @Query("oldPass") String oldPass, @Query("newPass") String newPass);


}
