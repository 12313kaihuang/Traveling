package com.yu.hu.traveling.mvp.impl;

import com.yu.hu.library.mvp.Presence;
import com.yu.hu.traveling.entity.comment.Comment;

import java.util.List;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.mvp.impl
 * 文件名：NoteDetailPrensence
 * 创建者：HY
 * 创建时间：2019/7/2 17:54
 * 描述：  游记详细内容
 *
 * @see com.yu.hu.traveling.activity.NoteDetailActivity
 */
public interface NoteDetailPrensence extends Presence {

    /**
     * 加载到了评论
     *
     * @param comments 所有评论
     */
    void onLoadComments(List<Comment> comments);

    /**
     * 评论加载失败
     */
    void onLoadError(Throwable e);
}
