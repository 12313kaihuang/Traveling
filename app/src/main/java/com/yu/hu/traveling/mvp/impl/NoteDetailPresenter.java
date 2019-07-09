package com.yu.hu.traveling.mvp.impl;

import com.yu.hu.library.mvp.BasePresenter;
import com.yu.hu.library.util.RetrofitUtil;
import com.yu.hu.library.util.RxUtil;
import com.yu.hu.traveling.entity.comment.Comment;
import com.yu.hu.traveling.entity.exception.MsgTransformException;
import com.yu.hu.traveling.entity.exception.NullResponseException;
import com.yu.hu.traveling.entity.msg.CommentMsg;
import com.yu.hu.traveling.retrofit.CommentService;
import com.yu.hu.traveling.rx.GlobalObserver;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.mvp.impl
 * 文件名：NoteDetailPresenter
 * 创建者：HY
 * 创建时间：2019/7/2 17:56
 * 描述：  NoteDetailPresenter
 *
 * @see com.yu.hu.traveling.activity.NoteDetailActivity
 */
public class NoteDetailPresenter extends BasePresenter<NoteDetailPrensence> {

    private CommentService mCommentService;

    public NoteDetailPresenter(NoteDetailPrensence impl) {
        super(impl);
        mCommentService = RetrofitUtil.getInstance().getService(CommentService.class);
    }

    /**
     * 加载评论
     */
    public void loadComments(int noteId) {

        Subscription subscription = mCommentService.getComments(noteId)
                .compose(RxUtil.defaultSchedulers())
                .flatMap((Func1<CommentMsg, Observable<List<Comment>>>) commentMsg -> {
                    if (!commentMsg.isStatusCorrect()) {
                        throw new MsgTransformException(commentMsg.getInfo());
                    }
                    if (commentMsg.getComments() == null) {
                        throw new NullResponseException("commentMsg.getComments() == null");
                    }
                    return Observable.just(commentMsg.getComments());
                })
                .subscribe(new GlobalObserver<List<Comment>>() {
                    @Override
                    public void onNext(List<Comment> comments) {
                        mInterface.onLoadComments(comments);
                    }

                    @Override
                    public void _onError(Throwable e) {
                        mInterface.onLoadError(e);
                    }
                });
        addSubscription(subscription);
    }
}
