package com.yu.hu.traveling.ui.login;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.libnetwork.ApiResponse;
import com.yu.hu.libnetwork.ApiService;
import com.yu.hu.traveling.model.User;
import com.yu.hu.traveling.network.ApiResponseCallback;
import com.yu.hu.traveling.network.UserService;
import com.yu.hu.traveling.utils.Statics;
import com.yu.hu.traveling.utils.TravelingUserProvider;
import com.yu.hu.traveling.utils.UserManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import cn.leancloud.chatkit.utils.ChatKitUtils;
import retrofit2.Call;

/**
 * @author Hy
 * created on 2020/04/15 22:58
 **/
@SuppressWarnings("WeakerAccess")
public class LoginViewModel extends AndroidViewModel {

    private static final String TAG = "LoginViewModel";

    private Context mContext;
    private Tencent tencent;
    private IUiListener loginListener;
    private LoginCallback loginCallback;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        tencent = Tencent.createInstance(Statics.QQ_APP_ID, mContext);
        loginListener = createLoginListener();
    }

    public void setLoginCallback(LoginCallback loginCallback) {
        this.loginCallback = loginCallback;
    }

    public IUiListener getLoginListener() {
        return loginListener;
    }

    public void login(Activity activity) {
        tencent.login(activity, "all", loginListener);
    }

    //根据token获取用户信息
    private void getUserInfo(QQToken qqToken, long expires_time, String openid) {
        UserInfo userInfo = new UserInfo(mContext, qqToken);
        userInfo.getUserInfo(createUserInfoListener(expires_time, openid));
    }

    //创建QQ授权页面需要的回调listener
    private IUiListener createLoginListener() {
        return new IUiListener() {

            @Override
            public void onComplete(Object o) {

                JSONObject response = (JSONObject) o;
                try {
                    String openid = response.getString("openid");
                    String access_token = response.getString("access_token");
                    String expires_in = response.getString("expires_in");
                    long expires_time = response.getLong("expires_time");
                    tencent.setOpenId(openid);
                    tencent.setAccessToken(access_token, expires_in);
                    QQToken qqToken = tencent.getQQToken();
                    getUserInfo(qqToken, expires_time, openid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
                if (loginCallback != null) {
                    loginCallback.onLoginError(uiError.toString());
                }
            }

            @Override
            public void onCancel() {
                if (loginCallback != null) {
                    loginCallback.onLoginCancel();
                }
            }
        };
    }

    private IUiListener createUserInfoListener(long expires_time, String openid) {
        return new IUiListener() {
            @Override
            public void onComplete(Object o) {
                JSONObject response = (JSONObject) o;
                try {
                    String nickname = response.getString("nickname");
                    String avatar = response.getString("figureurl_qq_2");

                    if (loginCallback != null) {
                        User user = new User();
                        user.name = nickname;
                        user.avatar = avatar;
                        user.qqOpenId = openid;
                        user.expires_time = expires_time;
                        LogUtil.d("qq response = " + response.toString());
                        doLogin(user);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
                if (loginCallback != null) {
                    loginCallback.onLoginError(uiError.toString());
                }
            }

            @Override
            public void onCancel() {
                if (loginCallback != null) {
                    loginCallback.onLoginCancel();
                }
            }
        };
    }

    //服务器登录请求
    private void doLogin(User user) {
        ApiService.create(UserService.class)
                .login(GsonUtils.toJson(user))
                .enqueue(new ApiResponseCallback<User>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<User>> call, @NotNull User response) {
                        loginCharKit(response);
                        UserManager.get().save(response);
                        ToastUtils.showShort("登录成功");
                        loginCallback.onLoginSuccess(response);
                    }

                    @Override
                    public void onFailure2(Call<ApiResponse<User>> call, String errorMsg) {
                        ToastUtils.showShort("登录失败");
                        loginCallback.onLoginError(errorMsg);
                    }
                });
    }

    //leanClound登录
    public void loginCharKit(User user) {
        TravelingUserProvider.addUser(user);
        ChatKitUtils.login(String.valueOf(user.id), new ChatKitUtils.LoginCallback() {
            @Override
            public void onLoginSuccess() {
                Log.i(TAG, "leanClound登录成功");
            }

            @Override
            public void onLoginFailure() {
                Log.w(TAG, "leanClound登录失败");
            }
        });
    }

    public interface LoginCallback {
        void onLoginSuccess(User user);

        void onLoginCancel();

        void onLoginError(String errorMsg);
    }
}
