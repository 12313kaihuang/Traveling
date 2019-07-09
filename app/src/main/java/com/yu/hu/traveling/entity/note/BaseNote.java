package com.yu.hu.traveling.entity.note;


import com.yu.hu.library.util.DateUtil;

import java.util.Date;

/**
 * Created by HY
 * 2018/12/2 15:29
 * 游记表   游记的基本信息
 */

@SuppressWarnings("unused")
public class BaseNote {

    /**
     * 游记
     */
    public static final int TAG_1 = 1;

    /**
     * 攻略
     */
    public static final int TAG_2 = 2;


    //    private Integer id;

    private Integer userId;

    private String title;

    private String content;

    private Integer tag = 1;

    private String createTime;


    public BaseNote() {
    }

    public BaseNote(Integer userId, String title, String content, Integer tag) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.createTime = DateUtil.toString(new Date());
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
