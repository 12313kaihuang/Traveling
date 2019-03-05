package com.android.traveling.entity.msg;


import com.android.traveling.entity.comment.Reply;

import java.util.List;

/**
 * Created by HY
 * 2019/3/5 10:15
 *
 * 结伴信息的评论
 *
 */
@SuppressWarnings("unused")
public class CompanionReplyMsg extends Msg {
    private List<Reply> replies;

    public CompanionReplyMsg() {

    }

    private CompanionReplyMsg(int status, String info) {
        super(status, info);
    }

    public static CompanionReplyMsg errMsg(String info){
        return new CompanionReplyMsg(ERROR_STATUS, info);
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }
}
