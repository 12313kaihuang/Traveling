package com.yu.hu.library.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yu.hu.library.activity.BaseActivity;
import com.yu.hu.library.impl.SubscriptionImpl;
import com.yu.hu.library.mvp.BasePresenter;
import com.yu.hu.library.mvp.Presence;
import com.yu.hu.library.util.AppUtil;
import com.yu.hu.library.util.LogUtil;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 文件名：BaseFragment
 * 创建者：HY
 * 创建时间：2019/4/20 10:13
 * 描述：  Fragment基类
 * <p>
 * 必须重写{@link #getLayoutId()}方法
 * <p>
 * 新学到的方法了解一下：
 * {@link #onHiddenChanged(boolean)}
 *
 * @see #getLayoutId() 设置布局文件
 * @see #onPrepare(Bundle)
 * @see #addSubscription(Subscription)
 * @see #getCurrentFragment()  获取当前Fragment
 * @see #getCurrentActivity()  获取当前Activity
 * @see #getCurrentView()      获取当前View
 */
@SuppressWarnings("unused")
public abstract class BaseFragment<I extends Presence, P extends BasePresenter<I>>
        extends Fragment
        implements FragmentImpl, SubscriptionImpl {

    protected View mView;

    //rxSubscription
    private CompositeSubscription mCompositeSubscription;

    protected P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(AppUtil.getLogTag(getClass()) + "--onCreate");
        mCompositeSubscription = new CompositeSubscription();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), container, false);
        prepare(savedInstanceState);
        LogUtil.d(AppUtil.getLogTag(getClass()) + "--onCreateView");
        return mView;
    }

    /**
     * 绑定Presenter
     */
    protected P attachPresenter() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
        LogUtil.d(AppUtil.getLogTag(getClass()) + " -- onDestroy");
    }

    private void prepare(@Nullable Bundle savedInstanceState) {
        //绑定ButterKnife  可直接使用@BindView等注解
        ButterKnife.bind(this, mView);
        init(savedInstanceState);
        onPrepare(savedInstanceState);
    }

    /**
     * 自己的进行的初始化操作（给子类但非最终实现类）
     */
    @SuppressWarnings("unused")
    protected void init(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 准备工作
     * <p>
     * 暴露给具体实现类的
     * 如设置默认值，初始化参数等
     * 放在onCreateView方法中执行的
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    protected void onPrepare(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 布局文件Id
     */
    @SuppressWarnings("WeakerAccess")
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 添加Subscription
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    @Override
    public final void addSubscription(Subscription s) {
        mCompositeSubscription.add(s);
    }

    /**
     * 在布局文件中添加一个子Fragment
     */
    protected final void setChildFragment(@IdRes int containerViewId, @NonNull Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(containerViewId, fragment)
                //替换commit为commitAllowingStateLoss
                //onSaveInstanceState后不能再addfragment了，把Commit改成这个可以解决原因暂未搞懂
                //一般情况下不会出现这个问题 特殊情况如ChatListFragment就每次都需要新建Fragment
                .commitAllowingStateLoss();
    }

    /**
     * 删除子Fragment
     */
    protected final void removeChildFragment(@NonNull Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();
    }

    /**
     * 新学到的方法 *_*
     * 这样就不需要在Activity中手动调用上onResume，onPause方法了
     *
     * @param hidden true -> 不可见  ，false -> 可见
     */
    @Override
    public void onHiddenChanged(boolean hidden) {

    }

    /**
     * 获取当前Activity
     * <p>
     * 要求Activity继承自BaseActivity
     */
    @SuppressWarnings({"unchecked", "WeakerAccess"})
    @Override
    public final <T extends BaseActivity> T getCurrentActivity() {
        return (T) getActivity();
    }

    /**
     * 获取当前Fragment
     * <p>
     * 要求Fragment继承自BaseFragment
     */
    @SuppressWarnings({"unchecked", "WeakerAccess"})
    @Override
    public final <T extends BaseFragment> T getCurrentFragment() {
        return (T) this;
    }

    @Override
    public final  <V extends View> V findViewById(@IdRes int idRes) {
        return mView.findViewById(idRes);
    }

    /**
     * 获取当前View布局
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    protected final View getCurrentView() {
        return mView;
    }
}
