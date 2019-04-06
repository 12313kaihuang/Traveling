package com.android.traveling.entity.msg;


import com.android.traveling.entity.user.User;
import com.android.traveling.util.StaticClass;

/**
 * Created by HY
 * 2019/1/11 9:50
 */
@SuppressWarnings("unused")
public class LoginMsg extends Msg {

    private User user;

    public LoginMsg() {
    }

    public LoginMsg(User user) {
        super(Msg.CORRECT_STATUS, "登录成功！");
        this.user = user;
        user.setImg(StaticClass.IMG_URL + user.getImg());
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
