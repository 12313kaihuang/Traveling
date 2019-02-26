package com.android.traveling.entity.user;


import android.support.annotation.NonNull;

import com.android.traveling.entity.msg.LoginMsg;
import com.android.traveling.entity.msg.Msg;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.StaticClass;
import com.android.traveling.util.UtilTools;

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

    /**
     * 获取当前登录的角色信息
     *
     * @return 当前登录的User
     */
    public static User getCurrentUser() {
        List<User> users = LitePal.findAll(User.class);
        if (users.size() != 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    /**
     * 是否已登录
     * @return 是否已登录
     */
    public static boolean hasLogin(){
        return getCurrentUser() != null;
    }

    /**
     * 从远程数据库更新本地User
     *
     * @param userCallback 回调接口
     */
    public static void refresh(UserCallback userCallback) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            userCallback.onFiled("currentUser == null");
            return;
        }
        //创建Retrofit对象  注意url后面有一个'/'。
        Retrofit retrofit = UtilTools.getRetrofit();
        // 获取UserService对象
        UserService userService = retrofit.create(UserService.class);
        Call<LoginMsg> call = userService.findUser(currentUser.getUserId());//谨记是userId
        call.enqueue(new Callback<LoginMsg>() {
            @Override
            public void onResponse(@NonNull Call<LoginMsg> call, @NonNull Response<LoginMsg> response) {
                LogUtil.d("response=" + response.toString());
                LoginMsg msg = response.body();
                if (msg == null) {
                    userCallback.onFiled("LoginMsg == null");
                } else {
                    if (msg.getStatus() == LoginMsg.errorStatus) {
                        userCallback.onFiled(msg.getInfo());
                    } else {
                        //更新成功
                        getCurrentUser().refresh(msg.getUser());  //更新currentUser
                        userCallback.onSuccess(msg.getUser());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginMsg> call, @NonNull Throwable t) {
                userCallback.onFiled("onFailure t=" + t.getMessage());
            }
        });
    }

    /**
     * 退出登录
     */
    public static void logout() {
        LitePal.deleteAll(User.class);   //清除数据
    }

    /**
     * 手机验证码登录
     * @param phoneNumber 手机号码
     * @param verificationCode 验证码
     * @param userCallback 回调接口
     */
    public static void loginByCode(String phoneNumber, String verificationCode, UserCallback userCallback) {
        //创建Retrofit对象  注意url后面有一个'/'。
        Retrofit retrofit = UtilTools.getRetrofit();
        // 获取UserService对象
        UserService userService = retrofit.create(UserService.class);
        Call<LoginMsg> call = userService.loginByCode(phoneNumber, verificationCode);
        call.enqueue(new Callback<LoginMsg>() {
            @Override
            public void onResponse(@NonNull Call<LoginMsg> call, @NonNull Response<LoginMsg> response) {
                LoginMsg loginMsg = response.body();
                if (loginMsg == null) {
                    userCallback.onFiled("loginMsg == null");
                } else {
                    if (loginMsg.getStatus() == Msg.errorStatus) {
                        userCallback.onFiled(loginMsg.getInfo());
                    } else {
                        LitePal.deleteAll(User.class);   //清除数据
                        User user = loginMsg.getUser();
                        user.setUserId(user.getId());
                        user.save();                    //存入user
                        userCallback.onSuccess(user);  //登录成功
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginMsg> call, @NonNull Throwable t) {
                userCallback.onFiled("onFailure t=" + t.getMessage());
            }
        });
    }

    /**
     * 手机密码登录
     *
     * @param phoneNumber 手机号码
     * @param password    密码
     * @param callback    回调接口
     */
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


    /**
     * 邮箱登录
     * @param email 邮箱
     * @param password 密码
     * @param userCallback 回调接口
     */
    public static void loginByEmail(String email, String password, UserCallback userCallback) {
        //创建Retrofit对象  注意url后面有一个'/'。
        Retrofit retrofit = UtilTools.getRetrofit();
        // 获取UserService对象
        UserService userService = retrofit.create(UserService.class);
        Call<LoginMsg> call = userService.loginByEmail(email, password);
        //异步请求
        call.enqueue(new Callback<LoginMsg>() {
            @Override
            public void onResponse(@NonNull Call<LoginMsg> call, @NonNull Response<LoginMsg> response) {
                LoginMsg loginMsg = response.body();
                if (loginMsg == null) {
                    userCallback.onFiled("loginMsg == null");
                } else {
                    if (loginMsg.getStatus() == Msg.errorStatus) {
                        userCallback.onFiled(loginMsg.getInfo());
                    } else {
                        LitePal.deleteAll(User.class);   //清除数据
                        User user = loginMsg.getUser();
                        user.setUserId(user.getId());
                        user.save();                    //存入user
                        userCallback.onSuccess(user);  //登录成功
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginMsg> call, @NonNull Throwable t) {
                userCallback.onFiled("onFailure t=" + t.getMessage());
            }
        });
    }
}
