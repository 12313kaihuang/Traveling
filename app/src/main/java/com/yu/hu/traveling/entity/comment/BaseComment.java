package com.yu.hu.traveling.entity.comment;

import com.yu.hu.library.util.DateUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by HY
 * 2019/1/20 15:14
 * <p>
 * 评论回复总表
 */
@SuppressWarnings("unused")
public class BaseComment {

    private Integer id;

    //标记位  0为评论  1为评论的回复  2为回复的回复
    private Integer flag;

    private Integer noteId;

    private Integer commentId;  //评论id

    private Integer toUserId;   //回复某个人的id

    private Integer userId;

    private String content;

    private Date commentTime;

    public BaseComment() {
    }

    //评论或评论的评论

    public BaseComment(Integer flag, Integer noteId, Integer commentId, Integer userId, String content) {
        this.flag = flag;
        this.noteId = noteId;
        this.commentId = commentId;
        this.userId = userId;
        this.content = content;
        this.commentTime = new Date();
    }


    public BaseComment(Integer noteId, Integer userId, String content) {
        this.noteId = noteId;
        this.userId = userId;
        this.content = content;
        this.commentTime = new Date();
    }

    //评论的回复
    public BaseComment(int flagComment, int noteId, int commentId, Reply reply, int userId, String content) {
        this.flag = flagComment;
        this.noteId = noteId;
        this.commentId = commentId;
        this.toUserId = reply.getUserId();
        this.userId = userId;
        this.content = content;
        this.commentTime = new Date();
    }
    //评论的回复
    public BaseComment(int flagComment, int noteId, Reply reply, int userId, String content) {
        this.flag = flagComment;
        this.noteId = noteId;
        this.toUserId = reply.getUserId();
        this.userId = userId;
        this.content = content;
        this.commentTime = new Date();
    }

    /**
     * 将所有属性转换为键值对
     *
     * @return Map
     */
    private Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        if (id != null) {
            map.put("id", String.valueOf(id));
        }
        if (flag != null) {
            map.put("flag", String.valueOf(flag));
        }
        if (noteId != null) {
            map.put("noteId", String.valueOf(noteId));
        }
        if (commentId != null) {
            map.put("commentId", String.valueOf(commentId));
        }
        if (toUserId != null) {
            map.put("toUserId", String.valueOf(toUserId));
        }
        if (userId != null) {
            map.put("userId", String.valueOf(userId));
        }
        if (content != null) {
            map.put("content", content);
        }
        if (commentTime != null) {
            map.put("commentTime", DateUtil.toString(commentTime));
        }
        return map;
    }

    //添加评论回调接口
    public interface AddCommentListener {
        void onSuccess(BaseComment baseComment);

        void onFailure(String reason);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public void setCommentId() {
        this.commentId = this.id;
    }

    Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "BaseComment{" +
                "id=" + id +
                ", flag=" + flag +
                ", noteId=" + noteId +
                ", commentId=" + commentId +
                ", toUserId=" + toUserId +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", commentTime=" + commentTime +
                '}';
    }
}
