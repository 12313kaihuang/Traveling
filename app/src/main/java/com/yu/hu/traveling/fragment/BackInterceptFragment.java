package com.yu.hu.traveling.fragment;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.yu.hu.common.fragment.BaseFragment;
import com.yu.hu.common.utils.LogUtil;

/**
 * @author Hy
 * created on 2020/04/15 16:39
 * <p>
 * 简化back事件的拦截
 **/
@SuppressWarnings("unused")
public abstract class BackInterceptFragment<D extends ViewDataBinding> extends BaseFragment<D> {

    private OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(hasInterceptBackPressed()) {
        @Override
        public void handleOnBackPressed() {
            LogUtil.d(generateLogTag() + " - handleOnBackPressed - hasIntercept" + hasInterceptBackPressed());
            BackInterceptFragment.this.handleOnBackPressed();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //放在这 是因为跳转到文章详情页时  切换到其他应用，再次返回时 会触发所有Fragment的onResume方法
        requireActivity().getOnBackPressedDispatcher().addCallback(this, backPressedCallback);
    }

    //因为本App中所有的Fragment切换方式为hide/show
    //所以每次先隐藏时需要监听/移除监听
    @CallSuper
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            //隐藏时取消掉监听
            backPressedCallback.remove();
        } else {
            requireActivity().getOnBackPressedDispatcher().addCallback(this, backPressedCallback);
        }
    }

    //是否消费掉此事件  默认为true  即不向下传递
    @SuppressWarnings("WeakerAccess")
    protected boolean hasInterceptBackPressed() {
        return true;
    }

    //back事件处理
    public abstract void handleOnBackPressed();

}
