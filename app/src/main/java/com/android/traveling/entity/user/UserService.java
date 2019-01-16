package com.android.traveling.entity.user;

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
     * 手机号密码登录
     *
     * @param phoneNumber 手机号
     * @param password    密码
     * @return LoginMsg
     */
    @POST("loginByPass")
    Call<LoginMsg> loginByPass(@Query("phoneNumber") String phoneNumber, @Query("password") String password);

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
     * @param userId userId
     * @param email 邮箱
     * @return Msg
     */
    @GET("bindEmail")
    Call<LoginMsg> bindEmail(@Query("userId") int userId, @Query("email") String email);

    /**
     * * 绑定邮箱和密码
     * @param userId userId
     * @param email 邮箱
     * @param password 密码
     * @return Msg
     */
    @GET("bindEmailAndPass")
    Call<LoginMsg> bindEmailAndPass(@Query("userId") int userId, @Query("email") String email,@Query("password")String password);
}
