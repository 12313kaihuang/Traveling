package com.yu.hu.traveling.entity.msg;


import com.yu.hu.traveling.entity.user.User;

/**
 * Created by HY
 * 2019/1/11 9:50
 */
@SuppressWarnings("unused")
public class LoginMsg extends Msg {

    private User user;

    public LoginMsg() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "LoginMsg{" +
                "status=" + getStatus() +
                "user=" + user +
                '}';
    }
}
