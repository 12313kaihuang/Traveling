package com.android.traveling.entity.note;


import android.support.annotation.NonNull;

import com.android.traveling.entity.msg.Msg;
import com.android.traveling.entity.user.User;
import com.android.traveling.util.DateUtil;
import com.android.traveling.util.StaticClass;
import com.android.traveling.util.UtilTools;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by HY
 * 2019/2/25 16:32
 * 回复
 */
@SuppressWarnings("unused")
public class Reply implements Serializable{

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

    public Reply() {
    }

    public Reply(User user, String toName, BaseComment baseComment) {
        this.id = baseComment.getId();
        this.flag = baseComment.getFlag();
        if (flag == FLAG_REPLY) {
            this.toId = baseComment.getToUserId();
            this.toName = toName;
        }
        this.userId = baseComment.getUserId();
        this.nickName = user.getNickName();
        this.userImg = user.getDirectImg();
        this.content = baseComment.getContent();
        this.commentTime = DateUtil.transform(baseComment.getCommentTime());
    }

    /**
     * retrofit请求 删除评论
     *
     * @param id 评论id
     */
    public static void deleteReply(int id, DeleteCommentListener listener) {
        Retrofit retrofit = UtilTools.getRetrofit();
        CommentService commentService = retrofit.create(CommentService.class);
        Call<Msg> msgCall = commentService.deleteReply(id);
        msgCall.enqueue(new Callback<Msg>() {
            @Override
            public void onResponse(@NonNull Call<Msg> call, @NonNull Response<Msg> response) {
                Msg msg = response.body();
                if (msg != null) {
                    if (msg.getStatus() == Msg.correctStatus) {
                        listener.onSuccess();
                        return;
                    }
                    listener.onFailure(msg.getInfo());
                } else {
                    listener.onFailure("msg == null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Msg> call, @NonNull Throwable t) {
                t.printStackTrace();
                listener.onFailure(t.getMessage());
            }
        });
    }

    public interface DeleteCommentListener {
        void onSuccess();

        void onFailure(String reason);
    }

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
