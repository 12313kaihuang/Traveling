package com.yu.hu.common.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.yu.hu.common.R;
import com.yu.hu.common.dialog.LoadingDialog;
import com.yu.hu.common.utils.LogUtil;

/**
 * create by hy on 2019/11/27 22:52
 * <p>
 * 方法：
 *
 * @see D {@link #getLayoutId()}资源文件对应的DataBinding类型
 * @see #getLayoutId()  必须重写 资源id
 * @see #getLoadingDialog() 可以重写 返回已自定义loadingDialog样式
 * <p>
 * 属性：
 * @see #mContext  == this
 * @see #mDataBinding mDataBinding
 * @see #mViewModelProvider viewModelProvider
 * @see #mLoadingDialog mLoadingDialog
 */
@SuppressWarnings("unused")
public abstract class BaseActivity<D extends ViewDataBinding> extends AppCompatActivity {

    protected Context mContext;
    protected D mDataBinding;
    protected ViewModelProvider mViewModelProvider;
    protected LoadingDialog mLoadingDialog;

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        mDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        //要设置LifecycleOwner 否则ViewModel数据发生变化时不会收到通知（视图不会发生变化）
        mDataBinding.setLifecycleOwner(this);
        mViewModelProvider = new ViewModelProvider(this);
    }

    @LayoutRes
    protected abstract int getLayoutId();

    @CallSuper
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().getPrimaryNavigationFragment();
        if (fragment != null) {
            LogUtil.d("onActivityResult - " + fragment.getClass().getSimpleName());
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 获取ViewModel实例
     *
     * @param modelClass ViewModel.class
     * @param <VM>       ViewModel类型
     * @return ViewModel
     */
    public <VM extends ViewModel> VM getViewModel(Class<VM> modelClass) {
        return mViewModelProvider.get(modelClass);
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
        mLoadingDialog.show(getSupportFragmentManager());
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
