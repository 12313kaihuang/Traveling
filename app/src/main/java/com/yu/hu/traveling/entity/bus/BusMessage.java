package com.yu.hu.traveling.entity.bus;

import com.yu.hu.traveling.entity.user.User;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.entity.bus
 * 文件名：BusMessage
 * 创建者：HY
 * 创建时间：2019/6/24 16:33
 * 描述：  EventBus发送广播所带的massage
 */
public class BusMessage {

    public static final int MESSAGE_TYPE_LOGIN = 0;
    public static final int MESSAGE_TYPE_LOGOUT = 1;

    public User user;

    public int messageType;

    public BusMessage(User user, int messageType) {
        this.user = user;
        this.messageType = messageType;
    }

    public BusMessage(int messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "BusMessage{" +
                "user=" + user +
                ", messageType=" + messageType +
                '}';
    }
}
