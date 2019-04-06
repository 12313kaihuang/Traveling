package com.android.traveling.entity.user;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.entity.user
 * 文件名：DetailUserInfoCallback
 * 创建者：HY
 * 创建时间：2019/3/7 19:07
 * 描述：  获取用户详细信息 回调接口
 */

public interface DetailUserInfoCallback {

    void onSuccess(DetailUserInfo detailUserInfo,boolean isFocus);

    void onFailure(String reason);
}
