package com.yu.hu.traveling.utils;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.GsonUtils;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.libnetwork.ApiResponse;
import com.yu.hu.libnetwork.ApiService;
import com.yu.hu.traveling.model.User;
import com.yu.hu.traveling.network.ApiResponseCallback;
import com.yu.hu.traveling.network.UserService;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCChatProfileProvider;
import cn.leancloud.chatkit.LCChatProfilesCallBack;
import cn.leancloud.chatkit.cache.LCIMProfileCache;
import retrofit2.Call;

/**
 * @author Hy
 * created on 2020/04/26 16:55
 * <p>
 * LeanClound IM 用户头像、昵称提供的工具类
 **/
public class TravelingUserProvider implements LCChatProfileProvider {

    private static volatile TravelingUserProvider sInstance;

    public static TravelingUserProvider getInstance() {
        if (sInstance == null) {
            synchronized (TravelingUserProvider.class) {
                if (sInstance == null) {
                    sInstance = new TravelingUserProvider();
                }
            }
        }
        return sInstance;
    }

    /**
     * 添加用户信息
     *
     * @param user user
     */
    public static void addUser(@Nullable User user) {
        if (user == null) {
            return;
        }
        for (LCChatKitUser lcChatKitUser : partUsers) {
            //有就更新
            if (lcChatKitUser.getUserId().equals(String.valueOf(user.id))) {
                refreshCacheUser(user);
                return;
            }
        }
        //没有才添加
        refreshCacheUser(user);
        partUsers.add(new LCChatKitUser(String.valueOf(user.id), user.name, user.avatar));
    }

    /**
     * 更新缓存中的用户信息
     */
    @SuppressWarnings("WeakerAccess")
    public static void refreshCacheUser(User user) {
        LCChatKitUser lcChatKitUser = new LCChatKitUser(String.valueOf(user.id), user.name, user.avatar);
        LCIMProfileCache.getInstance().cacheUser(lcChatKitUser);
    }

    private TravelingUserProvider() {
    }

    private static List<LCChatKitUser> partUsers = new ArrayList<>();

    // 此数据均为模拟数据，仅供参考
    static {
        //partUsers.add(new LCChatKitUser("Tom", "Tom", "http://www.avatarsdb.com/avatars/tom_and_jerry2.jpg"));
        //partUsers.add(new LCChatKitUser("Jerry", "Jerry", "http://www.avatarsdb.com/avatars/jerry.jpg"));
        //partUsers.add(new LCChatKitUser("Harry", "Harry", "http://www.avatarsdb.com/avatars/young_harry.jpg"));
        //partUsers.add(new LCChatKitUser("William", "William", "http://www.avatarsdb.com/avatars/william_shakespeare.jpg"));
        //partUsers.add(new LCChatKitUser("Bob", "Bob", "http://www.avatarsdb.com/avatars/bath_bob.jpg"));
    }

    @Override
    public void fetchProfiles(List<String> userIdList, LCChatProfilesCallBack profilesCallBack) {
        LogUtil.d("fetchProfiles - ids - " + GsonUtils.toJson(userIdList));
        for (int i = 0; i < userIdList.size(); i++) {
            String userId = userIdList.get(i);
            boolean isContains = false;
            for (int j = 0; j < partUsers.size(); j++) {
                if (userId.equals(partUsers.get(j).getUserId())) {
                    isContains = true;
                    break;
                }
            }
            if (!isContains) {
                getUserInfo(userId);
            }
        }
        profilesCallBack.done(partUsers, null);
    }

    @Override
    public List<LCChatKitUser> getAllUsers() {
        return partUsers;
    }

    private void getUserInfo(String sId) {
        int userId;
        if (TextUtils.isEmpty(sId)) {
            return;
        }
        try {
            userId = Integer.parseInt(sId);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        ApiService.create(UserService.class).getBaseUserInfo(userId)
                .enqueue(new ApiResponseCallback<User>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<User>> call, @NotNull User response) {
                        addUser(response);
                    }
                });
    }
}
