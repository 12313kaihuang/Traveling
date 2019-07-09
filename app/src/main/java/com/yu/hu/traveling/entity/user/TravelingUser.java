package com.yu.hu.traveling.entity.user;

import com.blankj.utilcode.util.SPUtils;
import com.yu.hu.traveling.db.Controller.UserDaoController;
import com.yu.hu.traveling.entity.Const;


/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.entity
 * 文件名：TravelingUser
 * 创建者：HY
 * 创建时间：2019/6/25 11:03
 * 描述：  对{@link User}的一些封装
 */
public class TravelingUser {

    /**
     * 获取当前登录的用户对象
     *
     * @return {@link User} ,没有登录则返回{@code null}
     */
    public static User getCurrentUser() {
        long userId = SPUtils.getInstance().getLong(Const.SP_KEY_CURRENT_USER);
        if (userId == -1) {
            return null;
        }
        return UserDaoController.getInstance().findById(userId);
    }

}
