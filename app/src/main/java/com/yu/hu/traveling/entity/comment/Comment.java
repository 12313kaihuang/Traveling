package com.yu.hu.traveling.entity.comment;


import com.yu.hu.library.util.DateUtil;
import com.yu.hu.traveling.entity.Const;
import com.yu.hu.traveling.entity.user.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HY
 * 2019/2/25 16:26
 * 评论
 */
@SuppressWarnings("unused")
public class Comment implements Serializable {

    public static final int FLAG_COMMENT = 0;   //评论

    private Integer id;   //评论id

    private Integer userId;

    private String nickName;

    private String userImg;

    private String content;

    private String commentTime;

    private List<Reply> replies;

    public Comment() {
    }

    public Comment(User user, BaseComment baseComment) {
        this.id = baseComment.getId();
        this.userId = baseComment.getUserId();
        this.nickName = user.getNickName();
        this.userImg = user.getImgUrl();
        this.content = baseComment.getContent();
        this.commentTime = DateUtil.toString(baseComment.getCommentTime());
        this.replies = new ArrayList<>();
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        if (userImg.contains(Const.URL)) {
            return userImg;
        }
        return Const.IMG_URL + userImg;
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

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    //添加回复
    public void addReply(User currentUser, BaseComment baseComment) {
        if (replies == null) {
            replies = new ArrayList<>();
        }
        Reply reply = new Reply(currentUser, nickName, baseComment);
        replies.add(reply);
    }

}
