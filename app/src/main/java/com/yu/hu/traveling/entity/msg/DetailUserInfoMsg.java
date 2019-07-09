package com.yu.hu.traveling.entity.msg;


import com.yu.hu.traveling.entity.user.DetailUserInfo;

/**
 * Created by HY
 * 2019/3/7 18:27
 */
@SuppressWarnings("unused")
public class DetailUserInfoMsg extends Msg {

    private DetailUserInfo detailUserInfo;

    private boolean focus;

    public DetailUserInfoMsg(DetailUserInfo detailUserInfo, boolean isFocus) {
        super(CORRECT_STATUS,"");
        this.detailUserInfo = detailUserInfo;
        this.focus = isFocus;
    }

    public DetailUserInfo getDetailUserInfo() {
        return detailUserInfo;
    }

    public void setDetailUserInfo(DetailUserInfo detailUserInfo) {
        this.detailUserInfo = detailUserInfo;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }
}
