package com.yu.hu.common.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.yu.hu.common.R;
import com.yu.hu.common.dialog.LoadingDialog;
import com.yu.hu.common.utils.LogUtil;


/**
 * Created by Hy on 2019/11/28 17:04
 * fragment基类
 * <p>
 * 方法：
 *
 * @see #getLayoutId() 必须重写
 * @see #onInitView(Bundle)
 * @see #onInitEvents(View, Bundle)
 * @see #getLoadingDialog() 可以重写 返回已自定义loadingDialog样式
 * @see #showLoadingDialog()  or {@link #showLoadingDialog(String)}
 * @see #createViewModel(Class) 创建ViewModel
 * <p>
 * 属性：
 * @see #mContext  == this
 * @see #mDataBinding mDataBinding
 * @see #mLoadingDialog LoadingDialog
 * @see #mLayoutInflater mLayoutInflater
 **/
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class BaseFragment<D extends ViewDataBinding> extends Fragment {

    protected Context mContext;
    protected FragmentActivity mActivity;
    protected D mDataBinding;
    protected View mRootView;
    protected LoadingDialog mLoadingDialog;
    protected LayoutInflater mLayoutInflater;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i(generateLogTag() + "- lifecycleMethods - onCreate");
    }

    @CallSuper
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        LogUtil.i(generateLogTag() + "- lifecycleMethods - onAttach");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.i(generateLogTag() + "- lifecycleMethods - onCreateView");
        mLayoutInflater = inflater;
        mDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        mDataBinding.setLifecycleOwner(this);
        mRootView = mDataBinding.getRoot();
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LogUtil.i(generateLogTag() + "- lifecycleMethods - onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    @CallSuper
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        LogUtil.i(generateLogTag() + "- lifecycleMethods - onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mActivity = requireActivity();
        beforeInitView();
        onInitView(savedInstanceState);
        onInitEvents(mRootView, savedInstanceState);
    }

    protected final void superActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * onInitView之前执行 一般可用于获取Arguments
     * 避免封装之后的Fragment执行onInitView时传过来的参数还没获取到
     */
    protected void beforeInitView() {

    }

    /**
     * view的一些初始化，不要有太耗时的操作否则会影响显示
     *
     * @see #onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    protected void onInitView(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 初始化事件
     *
     * @see #onViewCreated(View, Bundle)
     */
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @LayoutRes
    protected abstract int getLayoutId();

    @Nullable
    public final <T extends View> T findViewById(@IdRes int id) {
        return mRootView.findViewById(id);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.i(generateLogTag() + "- lifecycleMethods - onResume:");
    }

    @SuppressLint("DefaultLocale")
    @CallSuper
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.i(generateLogTag() + "- lifecycleMethods -  hide:" + hidden);
        if (!isAdded()) {
            LogUtil.d(generateLogTag() + "- fragment has not been attached yet");
            return;
        }
        //事件分发
        FragmentManager manager = getChildFragmentManager();
        Fragment fragment = manager.getPrimaryNavigationFragment();
        if (fragment != null) {
            fragment.onHiddenChanged(hidden);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.i(generateLogTag() + "- lifecycleMethods - onPause:");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i(generateLogTag() + "- lifecycleMethods - onDestroy");
    }

    protected String generateLogTag() {
        return getClass().getSimpleName();
    }

    /**
     * 创建ViewModel
     */
    protected <T extends ViewModel> T createViewModel(Class<T> modelClass) {
        return new ViewModelProvider(requireActivity()).get(modelClass);
    }

    /**
     * showLoadingDialog
     *
     * @see #mLoadingDialog
     */
    public void showLoadingDialog() {
        showLoadingDialog(null);
    }

    /**
     * showLoadingDialog
     * 指定content，注意指定过后之后显示的content都是设置的content
     *
     * @param content content
     */
    public void showLoadingDialog(String content) {
        if (mLoadingDialog == null) {
            mLoadingDialog = getLoadingDialog();
        }
        if (!TextUtils.isEmpty(content)) {
            mLoadingDialog.setContent(content);
        }
        if (mLoadingDialog.isAdded()) {
            mLoadingDialog.changeHint(content);
        } else {
            mLoadingDialog.show(getChildFragmentManager());
        }
    }

    /**
     * hideLoadingDialog
     */
    public void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isVisible()) {
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 可以自定义dialog样式
     *
     * @return LoadingDialog
     * @see LoadingDialog
     */
    protected LoadingDialog getLoadingDialog() {
        return LoadingDialog.newInstance()
                .setContent(R.string.loading)
                .setContentColorResource(R.color.colorPrimary)
                .setProgressBarColorResource(R.color.colorPrimary);
    }
}
