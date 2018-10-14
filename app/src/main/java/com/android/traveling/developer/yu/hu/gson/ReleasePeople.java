package com.android.traveling.developer.yu.hu.gson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.gson
 * 文件名：ReleasePeople
 * 创建者：HY
 * 创建时间：2018/10/13 10:45
 * 描述：  游记发布人
 */

@SuppressWarnings("unused")
public class ReleasePeople implements Serializable{

    //头像资源
    @SerializedName("headportrait")
    private String imgUrl;

    //昵称
    @SerializedName("nickname")
    private String nickName;

    //级别
    private String level;

    //是否已关注
    @SerializedName("isfocus")
    private boolean isFocus;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgurl) {
        this.imgUrl = imgurl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public boolean isFocus() {
        return isFocus;
    }

    public void setFocus(boolean focus) {
        isFocus = focus;
    }
}
