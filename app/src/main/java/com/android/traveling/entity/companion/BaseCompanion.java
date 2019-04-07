package com.android.traveling.entity.companion;

import java.util.Date;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.entity.companion
 * 文件名：BaseCompanion
 * 创建者：HY
 * 创建时间：2019/4/7 8:23
 * 描述：  结伴信息 基类
 */

@SuppressWarnings("unused")
public class BaseCompanion {

    private Integer id;

    private Integer userId;

    private String title;

    private String content;

    private Date createTime;

    private Date startTime;

    private Date endTime;

    private String target;

    private Integer views;

    public BaseCompanion() {
    }

    public BaseCompanion(Integer userId, String title, String content, Date startTime, Date endTime, String target) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createTime = new Date();
        this.startTime = startTime;
        this.endTime = endTime;
        this.target = target;
        this.views = 0;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }
}
