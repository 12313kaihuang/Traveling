package com.android.traveling.developer.yu.hu.gson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.gson
 * 文件名：News
 * 创建者：HY
 * 创建时间：2018/10/13 10:49
 * 描述：  游记类
 */

@SuppressWarnings("unused")
public class News implements Serializable {

    private String id;

    private int flag;

    //发布人
    @SerializedName("releasepeople")
    private ReleasePeople releasePeople;

    private String title;

    private String content;

    private String time;

    @SerializedName("img")
    private List<Img> imgList;

    private int like;

    private int comments;

    public class Img implements Serializable {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ReleasePeople getReleasePeople() {
        return releasePeople;
    }

    public void setReleasePeople(ReleasePeople releasePeople) {
        this.releasePeople = releasePeople;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Img> getImgList() {
        return imgList;
    }

    public void setImgList(List<Img> imgList) {
        this.imgList = imgList;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

}
