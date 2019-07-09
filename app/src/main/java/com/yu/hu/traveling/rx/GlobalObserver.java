package com.yu.hu.traveling.rx;

import com.blankj.utilcode.util.ToastUtils;
import com.yu.hu.library.util.LogUtil;
import com.yu.hu.traveling.db.Controller.UserDaoController;
import com.yu.hu.traveling.entity.exception.MsgTransformException;
import com.yu.hu.traveling.entity.exception.NullResponseException;
import com.yu.hu.traveling.mvp.LoginPresenter;

import java.net.ConnectException;

import rx.Observer;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.rx
 * 文件名：GlobalObserver
 * 创建者：HY
 * 创建时间：2019/6/24 11:58
 * 描述：  全局Subscriber
 * 统一处理onError方法，只重写onNext方法
 * <p>
 * 注意：
 * Observer和Subscriber的区别：
 * {@link rx.Subscriber} onCompleted方法之后将会自动取消订阅，所以二次请求时将无法执行
 */
public class GlobalObserver<T> implements Observer<T> {

    @Override
    public void onCompleted() {
        LogUtil.d("Subscriber onCompleted");
    }

    @Override
    public final void onError(Throwable e) {

        if (e instanceof ConnectException) {
            LogUtil.d("网络连接失败：" + e.getMessage());
            ToastUtils.showShort("网络连接失败，请检查您的网络");
        } else if (e instanceof LoginPresenter.LoginException) {
            //登录相关
            LogUtil.d("登录失败：" + e.getMessage());
            ToastUtils.showShort("登录失败：" + e.getMessage());
        } else if (e instanceof UserDaoController.UserCacheException) {
            LogUtil.d("缓存失败：" + e.getMessage());
        } else if (e instanceof MsgTransformException) {
            LogUtil.d("MsgTransformException:" + e.getMessage());
        } else if (e instanceof NullResponseException) {
            LogUtil.d("Msg has empty response：" + e.getMessage());
        } else {
            e.printStackTrace();
            ToastUtils.showShort("Rx HttpRequest error :" + e.getMessage());
        }
        _onError(e);
    }

    @Override
    public void onNext(T t) {

    }

    public void _onError(Throwable e) {

    }
}
