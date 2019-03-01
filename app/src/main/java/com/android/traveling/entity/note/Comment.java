package com.android.traveling.entity.note;


import com.android.traveling.entity.user.User;
import com.android.traveling.util.DateUtil;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.StaticClass;

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
        this.userImg = user.getDirectImg();
        this.content = baseComment.getContent();
        this.commentTime = DateUtil.transform(baseComment.getCommentTime());
        this.replies = new ArrayList<>();
        LogUtil.d("Comment", "userImg" + userImg);
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

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    //添加回复
    public void addReply(User currentUser,BaseComment baseComment) {
        if (replies == null) {
            replies = new ArrayList<>();
        }
        Reply reply = new Reply(currentUser,nickName,baseComment);
        replies.add(reply);
    }
}
