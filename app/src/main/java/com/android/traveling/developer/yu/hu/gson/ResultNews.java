package com.android.traveling.developer.yu.hu.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.gson
 * 文件名：ResultNews
 * 创建者：HY
 * 创建时间：2018/10/13 10:57
 * 描述：  游记组
 */

@SuppressWarnings("unused")
public class ResultNews {

    private String status;

    @SerializedName("news")
    private List<News> newsList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }
}
