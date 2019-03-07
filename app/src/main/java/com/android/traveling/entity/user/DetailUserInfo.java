package com.android.traveling.entity.user;


import com.android.traveling.entity.note.Note;
import com.android.traveling.util.StaticClass;

import java.util.Date;
import java.util.List;

/**
 * Created by HY
 * 2019/3/7 18:13
 * <p>
 * 用户详细信息 包括所有的文章
 */
@SuppressWarnings("unused")
public class DetailUserInfo {

    private Integer id;

    private String nickName;    //昵称

    private String img; //头像

    private String backgroundImg; //背景

    private String signature;

    private String gender;  //性别

    private Date birthday;

    private String area;

    private Integer level;

    private Integer fansNum;

    private Integer focusNum;

    private Integer beLikeNum;  //点赞与收藏

    private List<Note> notes;

    public DetailUserInfo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImg() {
        return StaticClass.IMG_URL + img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getBackgroundImg() {
        return StaticClass.IMG_URL + backgroundImg;
    }

    public void setBackgroundImg(String backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getFansNum() {
        return fansNum;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }

    public Integer getFocusNum() {
        return focusNum;
    }

    public void setFocusNum(Integer focusNum) {
        this.focusNum = focusNum;
    }

    public Integer getBeLikeNum() {
        return beLikeNum;
    }

    public void setBeLikeNum(Integer beLikeNum) {
        this.beLikeNum = beLikeNum;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
