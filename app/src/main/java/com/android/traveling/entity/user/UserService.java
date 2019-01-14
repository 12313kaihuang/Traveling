package com.android.traveling.entity.user;

import com.android.traveling.entity.msg.LoginMsg;

import retrofit2.Call;
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

    @POST("loginByPass")
    Call<LoginMsg> loginByPass(@Query("phoneNumber") String phoneNumber, @Query("password") String password);

}
