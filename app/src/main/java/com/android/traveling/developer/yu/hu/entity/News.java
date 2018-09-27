package com.android.traveling.developer.yu.hu.entity;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.entity
 * 文件名：News
 * 创建者：HY
 * 创建时间：2018/9/27 15:09
 * 描述：  游记攻略等信息的实体类
 */

public class News {

    //标题
    private String title;

    private String source;

    //图片url
    private String imgUrl;

    //游记地址
    private String newsUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }
}
