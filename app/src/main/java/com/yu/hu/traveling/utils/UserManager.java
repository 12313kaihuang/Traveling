package com.yu.hu.traveling.utils;

import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.blankj.utilcode.util.Utils;
import com.yu.hu.common.cache.CacheManager;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.common.utils.NavOptionsBuilder;
import com.yu.hu.libnetwork.ApiResponse;
import com.yu.hu.libnetwork.ApiService;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.TravelingApplication;
import com.yu.hu.traveling.model.User;
import com.yu.hu.traveling.network.ApiResponseCallback;
import com.yu.hu.traveling.network.UserService;
import com.yu.hu.traveling.ui.login.LoginFragment;

import org.jetbrains.annotations.NotNull;

import cn.leancloud.chatkit.utils.ChatKitUtils;
import retrofit2.Call;


/**
 * @author Hy
 * created on 2020/04/16 11:02
 * <p>
 * User管理类
 **/
@SuppressWarnings("unused")
public class UserManager {

    private static volatile UserManager sManager;
    private MutableLiveData<User> userLiveData;
    private MutableLiveData<User> chatUserLiveData = new MutableLiveData<>();
    private User mUser;

    public static UserManager get() {
        if (sManager == null) {
            synchronized (UserManager.class) {
                if (sManager == null) {
                    sManager = new UserManager();
                }
            }
        }
        return sManager;
    }

    //private UserDao userDao;

    private UserManager() {
        User cache = CacheManager.get(User.CACHE_KEY, User.class);
        if (cache != null && cache.expires_time > System.currentTimeMillis()) {
            mUser = cache;
        }
    }

    /**
     * 存储
     */
    public void save(User user) {
        mUser = user;
        CacheManager.save(User.CACHE_KEY, user);
        if (getUserLiveData().hasObservers()) {
            getUserLiveData().postValue(user);
        }
    }

    public LiveData<User> refresh() {
        if (!isLogin()) {
            return login((FragmentActivity) ((TravelingApplication) Utils.getApp()).getTopActivity());
        }
        MutableLiveData<User> liveData = new MutableLiveData<>();
        ApiService.create(UserService.class)
                .getUserInfo(getUserId())
                .enqueue(new ApiResponseCallback<User>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<User>> call, @NotNull User response) {
                        mUser = response;
                        liveData.postValue(response);
                        CacheManager.save(User.CACHE_KEY, response);
                    }
                });
        return liveData;
    }


    /**
     * 获取当前User对象
     */
    @Nullable
    public User getUser() {
        return isLogin() ? mUser : null;
    }

    public int getUserId() {
        User user = getUser();
        if (user != null) {
            LogUtil.d("userid = " + user.id);
        }
        return user == null ? -1 : user.id;
    }

    public void addObserver(LifecycleOwner owner, Observer<? super User> observer) {
        getUserLiveData().observe(owner, observer);
    }

    /**
     * 登录
     */
    public MutableLiveData<User> login(FragmentActivity activity) {
        ((MainActivity) activity).navigate(AppConfig.getDestItemId(LoginFragment.PAGE_URL), NavOptionsBuilder.defaultSlideAnim());
        return getUserLiveData();
    }

    /**
     * 退出登录
     * <p>
     * bugfix:  liveData默认情况下是支持黏性事件的，即之前已经发送了一条消息，当有新的observer注册进来的时候，也会把先前的消息发送给他，
     * <p>
     * 就造成了{@linkplain com.yu.hu.traveling.ui.MainFragment#onNavigationItemSelected(MenuItem)}  }死循环
     * <p>
     * 那有两种解决方法
     * 1.我们在退出登录的时候，把livedata置为空，或者将其内的数据置为null
     * 2.利用我们改造的stickyLiveData来发送这个登录成功的事件
     * <p>
     * 我们选择第一种,把livedata置为空
     */
    public void logout() {
        ChatKitUtils.logout();  //leancloud退出登录
        CacheManager.delete(User.CACHE_KEY);
        userLiveData.postValue(null);
        userLiveData = null;
        mUser = null;
    }

    @SuppressWarnings("WeakerAccess")
    public MutableLiveData<User> getUserLiveData() {
        if (userLiveData == null) {
            userLiveData = new MutableLiveData<>();
        }
        return userLiveData;
    }

    /**
     * 是否已登录
     *
     * @return {@code true}表示已登录 ；{@code false}表示未登录
     */
    public boolean isLogin() {
        return mUser != null && mUser.expires_time > System.currentTimeMillis();
    }

    public static boolean isSelf(int userId) {
        int currentUserId = get().getUserId();
        return get().isLogin() && currentUserId == userId;
    }
}
