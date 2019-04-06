package com.android.traveling.entity.user;


/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.entity.user
 * 文件名：UserCallback
 * 创建者：HY
 * 创建时间：2019/1/15 18:08
 * 描述：   回调接口
 */

public interface UserCallback {

    /**
     * 刷新成功
     * @param user 刷新成功后的user
     */
    void onSuccess(User user);

    /**
     * 刷新失败
     * @param info 失败信息
     */
    void onFiled(String info);

}
