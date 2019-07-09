package com.yu.hu.traveling.retrofit;

import com.yu.hu.traveling.entity.msg.DetailUserInfoMsg;
import com.yu.hu.traveling.entity.msg.LoginMsg;
import com.yu.hu.traveling.entity.msg.Msg;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.retrofit
 * 文件名：UserService
 * 创建者：HY
 * 创建时间：2019/6/24 11:39
 * 描述：  用户相关接口
 */
public interface UserService {

    /**
     * 发送验证码
     *
     * @param phoneNumber 手机号
     * @return Msg
     */
    @GET("sendPhoneVerifiedCode")
    Observable<Msg> sendVerifiedCode(@Query("phoneNumber") String phoneNumber);

    /**
     * 手机号密码登录
     *
     * @param phoneNumber 手机号
     * @param password    密码
     * @return LoginMsg
     */
    @POST("loginByPass")
    Observable<LoginMsg> loginByPass(@Query("phoneNumber") String phoneNumber, @Query("password") String password);

    /**
     * 手机验证码登录
     *
     * @param phoneNumber      手机号
     * @param verificationCode 验证码
     * @return LoginMsg
     */
    @POST("loginByCode")
    Observable<LoginMsg> loginByCode(@Query("phoneNumber") String phoneNumber, @Query("verificationCode") String verificationCode);

    /**
     * 邮箱密码登录
     *
     * @param email    邮箱
     * @param password 密码
     * @return LoginMsg
     */
    @POST("loginByEmail")
    Observable<LoginMsg> loginByEmail(@Query("email") String email, @Query("password") String password);


    /**
     * 查询User
     *
     * @param id userId
     * @return LoginMsg
     */
    @GET("findUser")
    Observable<LoginMsg> findUser(@Query("id") Long id);

    /**
     * 获取用户详细信息
     * 用于“我的界面”
     *
     * @param userId userId
     * @return DetailUserInfoMsg
     */
    @GET("getDetailUserInfo")
    Observable<DetailUserInfoMsg> getDetailUserInfo(@Query("userId") int userId);


    /**
     * 获取用户详细信息
     * 用于“个人主页”界面，
     * 可查询出{@code fromId}这个人是否关注了{@code userId}这个人
     *
     * @param userId userId
     * @param fromId fromId
     * @return DetailUserInfoMsg
     */
    @GET("getDetailUserInfo")
    Observable<DetailUserInfoMsg> getDetailUserInfo(@Query("userId") int userId, @Query("fromId") int fromId);
}
