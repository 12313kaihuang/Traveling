package com.yu.hu.traveling.rx;

import com.yu.hu.library.util.RxUtil;
import com.yu.hu.traveling.fragment.ChatListFragment;

import rx.Observable;
import rx.functions.Func1;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.rx
 * 文件名：ViewInitObservable
 * 创建者：HY
 * 创建时间：2019/7/5 14:33
 * 描述：  界面初始化的时候用这个异步初始化可以更快的跳转过去，优化用户体验
 *
 * @see ChatListFragment#onLoadChatList()
 */
public class ViewInitObservable {

    public static <T> Observable<T> create(InitImpl<T> init) {
        return Observable.just("")
                .compose(RxUtil.defaultSchedulers())
                .flatMap((Func1<String, Observable<T>>) s -> init.onInit());
    }

    public interface InitImpl<T> {
        Observable<T> onInit();
    }
}
