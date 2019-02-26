package com.android.traveling.entity.note;


import com.android.traveling.util.StaticClass;

import java.util.Date;

/**
 * Created by HY
 * 2019/2/25 16:32
 * 回复
 */
@SuppressWarnings("unused")
public class Reply {

    public static final int FLAG_COMMENT = 1;   //评论的评论
    public static final int FLAG_REPLY = 2;     //评论的回复

    private Integer id;   //评论id

    private Integer flag;

    private Integer userId;

    private String nickName;

    private String userImg;

    private String content;

    private Integer toId;

    private String toName;

    private String commentTime;

    public static int getFlagComment() {
        return FLAG_COMMENT;
    }

    public static int getFlagReply() {
        return FLAG_REPLY;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserImg() {
        return StaticClass.IMG_URL + userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }
}
