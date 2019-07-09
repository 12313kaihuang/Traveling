package com.yu.hu.library.activity;

import android.os.Bundle;
import android.view.View;

import com.yu.hu.library.impl.SubscriptionImpl;
import com.yu.hu.library.mvp.BasePresenter;
import com.yu.hu.library.mvp.Presence;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


/**
 * 文件名：BaseActivity
 * 创建者：HY
 * 创建时间：2019/4/20 9:09
 * 描述：  Activity基类
 * 布局比较简单的，
 * 不需要嵌套fragment的Activity继承此类
 * <p>
 * 必须重写{@link #getLayoutId()}方法
 *
 * @param <I> Presenter对应的IView接口  {@link Presence}
 * @param <P> 对应的Presenter {@link BasePresenter}
 * @see #$(int) 简化findViewById
 * @see #getLayoutId() 设置布局文件
 * @see #onPrepare(Bundle)
 * @see #addSubscription(Subscription)
 */
public abstract class BaseActivity<I extends Presence, P extends BasePresenter<I>>
        extends AppCompatActivity
        implements SubscriptionImpl {

    //rxSubscription
    private CompositeSubscription mCompositeSubscription;

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        prepare(savedInstanceState);
    }

    /**
     * prepare
     */
    private void prepare(@Nullable Bundle savedInstanceState) {
        //绑定ButterKnife  可直接使用@BindView等注解
        ButterKnife.bind(this);

        mCompositeSubscription = new CompositeSubscription();
        mPresenter = attachPresenter();

        init(savedInstanceState);
        onPrepare(savedInstanceState);
    }

    /**
     * 提供给子类用于一些初始化操作
     * 如 {@link BaseNavigationActivity}
     * 具体使用时就用{@link #onPrepare(Bundle)}，
     * 这样实现类就不用调用{@code super.onPrepare()}了
     */
    @SuppressWarnings("unused")
    protected void init(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 准备工作，提供给具体实现类的
     * <p>
     * 如设置默认值，初始化参数等
     * 放在onCreate方法中执行的
     */
    @SuppressWarnings("unused")
    protected void onPrepare(@Nullable Bundle savedInstanceState) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
    }

    /**
     * 设置布局文件
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 绑定Presenter
     */
    protected P attachPresenter() {
        return null;
    }

    /**
     * 添加Subscription
     */
    @SuppressWarnings("unused")
    @Override
    public final void addSubscription(Subscription s) {
        mCompositeSubscription.add(s);
    }

    /**
     * 简化findViewById
     */
    @SuppressWarnings("unused")
    protected <T extends View> T $(@IdRes int id) {
        return findViewById(id);
    }

}
