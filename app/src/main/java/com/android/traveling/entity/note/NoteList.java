package com.android.traveling.entity.note;

import java.util.Date;

/**
 * Created by HY
 * 2019/1/20 16:20
 * <p>
 * View视图
 * 首页所需一篇游记的全部信息
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class NoteList {

    private Integer id;

    private Integer userId;

    private String title;

    private String content;

    private Integer tag = 1;

    private String imgs;

    private Date createTime;

    private Integer likeNum;

    private Integer commentNum;

    private String headportrait;

    //昵称
    private String nickName;

    //级别
    private String level;


    public NoteList() {
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

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public String getImgUrl() {
        return headportrait;
    }

    public void setImgUrl(String imgUrl) {
        this.headportrait = imgUrl;
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
