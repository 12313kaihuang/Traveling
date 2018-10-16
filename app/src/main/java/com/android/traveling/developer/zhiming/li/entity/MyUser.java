package com.android.traveling.developer.zhiming.li.entity;

import android.text.TextUtils;

import com.android.traveling.util.StaticClass;
import com.google.gson.annotations.SerializedName;

import cn.bmob.v3.BmobUser;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.entity
 * 文件名：MyUser
 * 创建者：HY
 * 创建时间：2018/10/16 14:42
 * 描述：  扩展BmobUser
 */

@SuppressWarnings("unused")
public class MyUser extends BmobUser {

    @SerializedName("nickname")
    private String nickName;    //昵称

    private String gender;      //性别

    private String signature;   //签名

    private String liveArea;    //所在地

    public String getGender() {
        return gender == null ? StaticClass.GENDER_SECRET : gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLiveArea() {
        return liveArea;
    }

    public void setLiveArea(String liveArea) {
        this.liveArea = liveArea;
    }

    public String getSignature() {
        if (TextUtils.isEmpty(signature)) {
            return "";
        } else {
            return signature;
        }
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getNickName() {
        if (TextUtils.isEmpty(nickName)) {
            return "";
        }
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
