package com.android.traveling.entity.msg;


import com.android.traveling.entity.user.DetailUserInfo;

/**
 * Created by HY
 * 2019/3/7 18:27
 */
@SuppressWarnings("unused")
public class DetailUserInfoMsg extends Msg {

    private DetailUserInfo detailUserInfo;

    public DetailUserInfoMsg(DetailUserInfo detailUserInfo) {
        super(CORRECT_STATUS,"");
        this.detailUserInfo = detailUserInfo;
    }

    public DetailUserInfo getDetailUserInfo() {
        return detailUserInfo;
    }

    public void setDetailUserInfo(DetailUserInfo detailUserInfo) {
        this.detailUserInfo = detailUserInfo;
    }
}
