package com.android.traveling.entity.msg;


import com.android.traveling.entity.note.BaseComment;

/**
 * Created by HY
 * 2019/2/28 23:43
 */
@SuppressWarnings("unused")
public class BaseCommentMsg extends Msg {
    private BaseComment baseComment;

    public BaseCommentMsg() {
    }

    private BaseCommentMsg(int status, String info, BaseComment baseComment) {
        super(status, info);
        this.baseComment = baseComment;
    }

    public BaseCommentMsg(BaseComment baseComment) {
        super(correctStatus,"");
        if (baseComment == null) {
           setStatus(errorStatus);
           setInfo("baseComment == null");
        }
        this.baseComment = baseComment;
    }

    public static BaseCommentMsg errorMsg(String info) {
        return new BaseCommentMsg(Msg.errorStatus, info, null);
    }

    public BaseComment getBaseComment() {
        return baseComment;
    }

    public void setBaseComment(BaseComment baseComment) {
        this.baseComment = baseComment;
    }
}
