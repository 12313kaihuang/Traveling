package com.android.traveling.entity.note;

import com.android.traveling.util.StaticClass;
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
public class ReleasePeople implements Serializable {

    private Integer userId;

    //头像资源
    @SerializedName("headportrait")
    private String imgUrl;

    //昵称
    @SerializedName("nickname")
    private String nickName;

    //级别
    private String level;


    public ReleasePeople() {

    }

    public ReleasePeople(NoteList noteList) {
        this.userId = noteList.getUserId();
        this.imgUrl = StaticClass.IMG_URL + noteList.getImgUrl();
        this.nickName = noteList.getNickName();
        this.level = noteList.getLevel();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

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

}
