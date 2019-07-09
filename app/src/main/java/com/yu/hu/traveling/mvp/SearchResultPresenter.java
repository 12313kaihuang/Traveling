package com.yu.hu.traveling.mvp;

import com.yu.hu.library.mvp.BasePresenter;
import com.yu.hu.library.util.LogUtil;
import com.yu.hu.library.util.RetrofitUtil;
import com.yu.hu.library.util.RxUtil;
import com.yu.hu.traveling.entity.exception.MsgTransformException;
import com.yu.hu.traveling.entity.exception.NullResponseException;
import com.yu.hu.traveling.entity.msg.NoteMsg;
import com.yu.hu.traveling.entity.note.Note;
import com.yu.hu.traveling.mvp.impl.SearchResultPresence;
import com.yu.hu.traveling.retrofit.NoteService;
import com.yu.hu.traveling.rx.GlobalObserver;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.mvp
 * 文件名：SearchResultPresenter
 * 创建者：HY
 * 创建时间：2019/7/2 14:52
 * 描述：  SearchResultPresenter 游记相关
 */
public class SearchResultPresenter extends BasePresenter<SearchResultPresence> {

    private NoteService mNoteService;

    private NoteMsgTransformer mNoteMsgTransformer;

    public SearchResultPresenter(SearchResultPresence impl) {
        super(impl);
        mNoteService = RetrofitUtil.getInstance().getService(NoteService.class);
        mNoteMsgTransformer = new NoteMsgTransformer();
    }

    /**
     * 模糊查询最新的标题含有 content 的10篇文章
     */
    public void searchHazily(String content) {
        LogUtil.d("debug -- searchHazily");
        Subscription subscription = mNoteService.searchNoteHazily(content)
                .compose(RxUtil.defaultSchedulers())
                .flatMap(mNoteMsgTransformer)
                .subscribe(new GlobalObserver<List<Note>>(){
                    @Override
                    public void onNext(List<Note> noteList) {
                        mInterface.onSearchResult(noteList);
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 模糊查询更多游记文章
     */
    public void searchMoreHazily(String content, int lastId) {
        Subscription subscription = mNoteService.searchMoreNoteHazily(content, lastId)
                .compose(RxUtil.defaultSchedulers())
                .flatMap(mNoteMsgTransformer)
                .subscribe(new GlobalObserver<List<Note>>() {
                    @Override
                    public void onNext(List<Note> notes) {
                        mInterface.onSearchMoreResult(notes);
                    }
                });
        addSubscription(subscription);

    }

    //拆解LoginMsg并转换成User
    private class NoteMsgTransformer implements Func1<NoteMsg, Observable<List<Note>>> {

        @Override
        public Observable<List<Note>> call(NoteMsg noteMsg) {
            if (!noteMsg.isStatusCorrect()) {
                throw new MsgTransformException(noteMsg.getInfo());
            }
            if (noteMsg.getNotes() == null) {
                throw new NullResponseException("msg.getNotes() == null");
            }
            return Observable.just(noteMsg.getNotes());
        }
    }
}
