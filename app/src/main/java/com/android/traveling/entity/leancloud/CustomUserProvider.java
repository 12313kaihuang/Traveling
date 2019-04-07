package com.android.traveling.entity.leancloud;

import android.support.annotation.NonNull;

import com.android.traveling.entity.msg.LoginMsg;
import com.android.traveling.entity.service.UserService;
import com.android.traveling.entity.user.DetailUserInfo;
import com.android.traveling.entity.user.User;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.UtilTools;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCChatProfileProvider;
import cn.leancloud.chatkit.LCChatProfilesCallBack;
import cn.leancloud.chatkit.cache.LCIMProfileCache;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wli on 15/12/4.
 * 实现自定义用户体系
 */
public class CustomUserProvider implements LCChatProfileProvider {

    private static CustomUserProvider customUserProvider;

    public synchronized static CustomUserProvider getInstance() {
        if (null == customUserProvider) {
            customUserProvider = new CustomUserProvider();
        }
        return customUserProvider;
    }

    private CustomUserProvider() {
    }

    private static List<LCChatKitUser> partUsers = new ArrayList<>();

    private LCChatKitUser createDefaultUser(String userId) {
        return new LCChatKitUser(userId, "测试用户", "http://www.avatarsdb.com/avatars/tom_and_jerry2.jpg");
    }
    // 此数据均为 fake，仅供参考
    //    static {
    //        partUsers.add();
    //        partUsers.add(new LCChatKitUser("2", "独秀同学", "http://www.avatarsdb.com/avatars/jerry.jpg"));
    //        partUsers.add(new LCChatKitUser("3", "Harry", "http://www.avatarsdb.com/avatars/young_harry.jpg"));
    //        partUsers.add(new LCChatKitUser("4", "William", "http://www.avatarsdb.com/avatars/william_shakespeare.jpg"));
    //        partUsers.add(new LCChatKitUser("Bob", "Bob", "http://www.avatarsdb.com/avatars/bath_bob.jpg"));
    //    }

    @Override
    public void fetchProfiles(List<String> list, LCChatProfilesCallBack callBack) {
        LogUtil.d("fetchProfiles   list=" + new Gson().toJson(list));
        for (int i = 0; i < list.size(); i++) {
            String userId = list.get(i);
            boolean isContains = false;
            for (int j = 0; j < partUsers.size(); j++) {
                if (userId.equals(partUsers.get(j).getUserId())) {
                    isContains = true;
                    break;
                }
            }
            if (!isContains) {
                LogUtil.d("fetchProfiles userId=" + userId);
                getLCChatKitUser(Integer.parseInt(userId), new mCallBack() {
                    @Override
                    public void onSuccess(LCChatKitUser lcChatKitUser) {
                        LogUtil.d("查找" + userId + "成功，添加");
                        addUser(lcChatKitUser);
                    }

                    @Override
                    public void onFailure() {
                        LogUtil.d("查找" + userId + "失败，添加");
                        addUser(createDefaultUser(userId));
                    }
                });
            }
        }
        LogUtil.d("回调接口 size=" + partUsers.size());
        callBack.done(partUsers, null);
    }

    //添加User
    private synchronized void addUser(LCChatKitUser lcChatKitUser) {
        LogUtil.d("addUser size=" + partUsers.size() + ",userId=" + lcChatKitUser.getUserId());
        for (int i = 0; i < partUsers.size(); i++) {
            if (partUsers.get(i).getUserId().equals(lcChatKitUser.getUserId())) {
                partUsers.set(i, lcChatKitUser);
                break;
            }
        }
        partUsers.add(lcChatKitUser);
    }

    @Override
    public List<LCChatKitUser> getAllUsers() {
        return partUsers;
    }

    /**
     * 获得某个用户的信息
     *
     * @param userId userId
     */
    private static void getLCChatKitUser(int userId, mCallBack callBack) {
        UserService userService = UtilTools.getRetrofit().create(UserService.class);
        userService.findUser(userId).enqueue(new Callback<LoginMsg>() {
            @Override
            public void onResponse(@NonNull Call<LoginMsg> call, @NonNull Response<LoginMsg> response) {
                LoginMsg msg = response.body();
                if (msg != null && msg.isStatusCorrect()) {
                    User user = msg.getUser();
                    LogUtil.d("fetchProfiles user=" + user.toString());
                    callBack.onSuccess(
                            new LCChatKitUser(String.valueOf(user.getUserId()), user.getNickName(),
                                    user.getImg())
                    );
                } else {
                    LogUtil.d("CustomUserProvider  getLCChatKitUser", msg != null ? msg.getInfo() : "msg == null");
                    callBack.onFailure();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginMsg> call, @NonNull Throwable t) {
                LogUtil.d("CustomUserProvider  getLCChatKitUser", t.getMessage());
                callBack.onFailure();
            }
        });
    }

    public static void refreshCacheUser(DetailUserInfo detailUserInfo) {
        LCChatKitUser lcChatKitUser = new LCChatKitUser(String.valueOf(detailUserInfo.getId()), detailUserInfo.getNickName(),
                detailUserInfo.getImg());
        LCIMProfileCache.getInstance().cacheUser(lcChatKitUser);
        LogUtil.d("更新用户信息 " + String.valueOf(detailUserInfo.getId()) + detailUserInfo.getNickName() +
                detailUserInfo.getImg());

    }

    /**
     * 回调接口
     */
    interface mCallBack {
        void onSuccess(LCChatKitUser lcChatKitUser);

        void onFailure();
    }

    /**
     * 添加用户信息
     *
     * @param user user
     */
    public static void addUser(@NonNull User user) {
        for (LCChatKitUser lcChatKitUser : partUsers) {
            //有就更新
            if (lcChatKitUser.getUserId().equals(String.valueOf(user.getUserId()))) {
                refreshCacheUser(user);
                return;
            }
        }
        //没有才添加
        partUsers.add(new LCChatKitUser(String.valueOf(user.getUserId()), user.getNickName(), user.getImg()));
    }

    //更新缓存中的用户信息
    public static void refreshCacheUser(User user) {
        LCChatKitUser lcChatKitUser = new LCChatKitUser(String.valueOf(user.getUserId()),
                user.getNickName(), user.getImg());
        LCIMProfileCache.getInstance().cacheUser(lcChatKitUser);
    }
}
