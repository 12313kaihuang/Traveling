package com.android.traveling.developer.ting.li.entity;

public class FriendsNews {
    private String userName;//用户名
    private String userImg;//用户头像
    private String newsContent;//结伴内容
    //private String newsTimeImg;//时间图标
    private String newsTimeText;//结伴时间
    private String newsSiteText;//结伴地点
    //private String newsSiteImg;//地点图标
    private String browseNum;//浏览数
    private String timeofPublish;//发布时间
    //private String newsDiscussImg;//评论图标
    private String newsDiscuss;//评论数

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public String getNewsTimeText() {
        return newsTimeText;
    }

    public void setNewsTimeText(String newsTimeText) {
        this.newsTimeText = newsTimeText;
    }

    public String getNewsSiteText() {
        return newsSiteText;
    }

    public void setNewsSiteText(String newsSiteText) {
        this.newsSiteText = newsSiteText;
    }

    public String getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(String browseNum) {
        this.browseNum = browseNum;
    }

    public String getTimeofPublish() {
        return timeofPublish;
    }

    public void setTimeofPublish(String timeofPublish) {
        this.timeofPublish = timeofPublish;
    }

    public String getNewsDiscuss() {
        return newsDiscuss;
    }

    public void setNewsDiscuss(String newsDiscuss) {
        this.newsDiscuss = newsDiscuss;
    }
}
