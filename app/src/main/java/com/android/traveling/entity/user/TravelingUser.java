package com.android.traveling.entity.user;


import android.content.Context;
import android.support.annotation.NonNull;

import com.android.traveling.MainActivity;
import com.android.traveling.entity.leancloud.CustomUserProvider;
import com.android.traveling.entity.msg.DetailUserInfoMsg;
import com.android.traveling.entity.msg.LoginMsg;
import com.android.traveling.entity.msg.Msg;
import com.android.traveling.entity.service.UserService;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.UtilTools;
import com.android.traveling.widget.dialog.ToLoginDialog;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.google.gson.Gson;

import org.litepal.LitePal;

import java.util.List;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.cache.LCIMProfileCache;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
     *
     * @return 是否已登录
     */
    public static boolean hasLogin() {
        return getCurrentUser() != null;
    }

    /**
     * 判断是否已登录，如果没有则先登录
     *
     * @param context context
     * @return 否已登录
     */
    public static User checkLogin(Context context) {
        if (getCurrentUser() == null) {
            new ToLoginDialog(context, null).show();
            return null;
        }
        return getCurrentUser();
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
        call.enqueue(createCallback(userCallback));
    }

    /**
     * 生成登录回调接口
     *
     * @param userCallback userCallback
     * @return Callback<LoginMsg>
     */
    @NonNull
    private static Callback<LoginMsg> createCallback(UserCallback userCallback) {
        return new Callback<LoginMsg>() {
            @Override
            public void onResponse(@NonNull Call<LoginMsg> call, @NonNull Response<LoginMsg> response) {
                LogUtil.d("response=" + response.toString());
                LoginMsg msg = response.body();
                if (msg == null) {
                    userCallback.onFiled("LoginMsg == null");
                } else {
                    if (msg.getStatus() == LoginMsg.ERROR_STATUS) {
                        userCallback.onFiled(msg.getInfo());
                    } else {
                        LitePal.deleteAll(User.class);   //清除数据
                        User user = msg.getUser();
                        user.setUserId(user.getId());
                        user.save();                    //存入user
                        try {
                            LCChatKit.getInstance().open(String.valueOf(user.getUserId()), new AVIMClientCallback() {
                                @Override
                                public void done(AVIMClient avimClient, AVIMException e) {
                                    if (null == e) {
                                        CustomUserProvider.refreshCacheUser(user);
                                        userCallback.onSuccess(user);
                                        LogUtil.d("===============done: " + user.getUserId() + " 登录LeanCloud成功");
                                    } else {
                                        LogUtil.d("===============done: " + user.getUserId() + " 登录LeanCloud失败");
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtil.d("MainActivity  LeanCloud open异常");
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginMsg> call, @NonNull Throwable t) {
                t.printStackTrace();
                userCallback.onFiled("onFailure t=" + t.getMessage());
            }
        };
    }

    /**
     * 退出登录
     */
    public static void logout() {
        LitePal.deleteAll(User.class);   //清除数据
    }

    /**
     * 手机验证码登录
     *
     * @param phoneNumber      手机号码
     * @param verificationCode 验证码
     * @param userCallback     回调接口
     */
    public static void loginByCode(String phoneNumber, String verificationCode, UserCallback userCallback) {
        //创建Retrofit对象  注意url后面有一个'/'。
        Retrofit retrofit = UtilTools.getRetrofit();
        // 获取UserService对象
        UserService userService = retrofit.create(UserService.class);
        Call<LoginMsg> call = userService.loginByCode(phoneNumber, verificationCode);
        call.enqueue(createCallback(userCallback));
    }

    /**
     * 手机密码登录
     *
     * @param phoneNumber 手机号码
     * @param password    密码
     * @param callback    回调接口
     */
    public static void loginByPass(String phoneNumber, String password, UserCallback callback) {
        //创建Retrofit对象  注意url后面有一个'/'。
        //        Retrofit retrofit = new Retrofit.Builder().baseUrl(StaticClass.URL)
        //                .addConverterFactory(GsonConverterFactory.create()).build();
        // 获取UserService对象
        UserService userService = UtilTools.getRetrofit().create(UserService.class);
        Call<LoginMsg> userCall = userService.loginByPass(phoneNumber, password);
        //异步请求
        userCall.enqueue(createCallback(callback));
    }


    /**
     * 邮箱登录
     *
     * @param email        邮箱
     * @param password     密码
     * @param userCallback 回调接口
     */
    public static void loginByEmail(String email, String password, UserCallback userCallback) {
        //创建Retrofit对象  注意url后面有一个'/'。
        Retrofit retrofit = UtilTools.getRetrofit();
        // 获取UserService对象
        UserService userService = retrofit.create(UserService.class);
        Call<LoginMsg> call = userService.loginByEmail(email, password);
        //异步请求
        call.enqueue(createCallback(userCallback));
    }


    /**
     * 判断userId是否与当前登录的用户一致
     *
     * @param userId userId
     * @return 判断结果
     */
    public static boolean isCurrentUser(Integer userId) {
        User currentUser = getCurrentUser();
        return currentUser != null && userId.equals(currentUser.getUserId());
    }

    /**
     * 获取用户的详细信息
     *
     * @param userId userId
     */
    public static void getDetailUserInfo(int userId, DetailUserInfoCallback callback) {
        UserService service = UtilTools.getRetrofit().create(UserService.class);
        Call<DetailUserInfoMsg> call;
        User currentUser = TravelingUser.getCurrentUser();
        if (currentUser != null) {
            call = service.getDetailUserInfo(userId, currentUser.getUserId());
            LogUtil.d("getDetailUserInfo toId=" + userId + " fromId=" + currentUser.getUserId());
        } else {
            call = service.getDetailUserInfo(userId);
            LogUtil.d("getDetailUserInfo toId = " + userId + " fromId=null");
        }
        call.enqueue(new Callback<DetailUserInfoMsg>() {
            @Override
            public void onResponse(@NonNull Call<DetailUserInfoMsg> call, @NonNull Response<DetailUserInfoMsg> response) {
                DetailUserInfoMsg msg = response.body();
                LogUtil.d("============DetailUserInfoMsg=" + (new Gson().toJson(msg)));
                if (msg == null) {
                    callback.onFailure("msg == null");
                    return;
                }
                if (msg.getStatus() == Msg.ERROR_STATUS) {
                    callback.onFailure(msg.getInfo());
                }
                callback.onSuccess(msg.getDetailUserInfo(), msg.isFocus());
            }

            @Override
            public void onFailure(@NonNull Call<DetailUserInfoMsg> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });

    }


}
