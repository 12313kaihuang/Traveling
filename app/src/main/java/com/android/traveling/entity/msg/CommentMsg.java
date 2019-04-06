package com.android.traveling.entity.msg;


import com.android.traveling.entity.comment.Comment;

import java.util.List;

/**
 * Created by HY
 * 2019/2/25 19:31
 *
 * 评论
 *
 */
@SuppressWarnings("unused")
public class CommentMsg extends Msg {

    private List<Comment> comments;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


}
