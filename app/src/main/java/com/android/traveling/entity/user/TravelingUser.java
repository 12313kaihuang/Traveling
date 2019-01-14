package com.android.traveling.entity.user;


import android.support.annotation.NonNull;

import com.android.traveling.entity.msg.LoginMsg;
import com.android.traveling.util.StaticClass;

import org.litepal.LitePal;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.entity.user
 * 文件名：TravelingUser
 * 创建者：HY
 * 创建时间：2019/1/13 15:56
 * 描述：  封装类User
 */

public class TravelingUser {

    public static User getCurrentUser() {
        //        User user = new User();
        //        user.setPhoneNumber("17714445");
        //        user.setUserId(4);
        //        user.save();
        //        System.out.println("00000userId="+user.getUserId());
        List<User> users = LitePal.findAll(User.class);
        //        LitePal.deleteAll(User.class);
        if (users.size() != 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    public static void logout() {
        LitePal.deleteAll(User.class);   //清除数据
    }

    public static void loginByPass(String phoneNumber, String password, Callback<LoginMsg> callback) {
        //创建Retrofit对象  注意url后面有一个'/'。
        Retrofit retrofit = new Retrofit.Builder().baseUrl(StaticClass.URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        // 获取UserService对象
        UserService userService = retrofit.create(UserService.class);
        Call<LoginMsg> userCall = userService.loginByPass(phoneNumber, password);
        //异步请求
        userCall.enqueue(new Callback<LoginMsg>() {
            @Override
            public void onResponse(@NonNull Call<LoginMsg> call, @NonNull Response<LoginMsg> response) {
                LoginMsg loginMsg = response.body();
                if (loginMsg != null && loginMsg.getUser() != null) {
                    LitePal.deleteAll(User.class);   //清除数据
                    User user = loginMsg.getUser();
                    user.setUserId(user.getId());
                    user.save();                    //存入user
                    System.out.println("登录成功");
                }
                System.out.println("currentUser=" + getCurrentUser());
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<LoginMsg> call, @NonNull Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

}
