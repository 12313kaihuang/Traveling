package com.yu.hu.common.fragment;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;

import com.yu.hu.common.R;
import com.yu.hu.common.databinding.FragmentDevelopBinding;
import com.yu.hu.common.utils.LogUtil;

/**
 * 开发中的fragment
 *
 * @see #getText() 重写此方法以设置所展示的文字
 */
public class DevelopingFragment extends BaseFragment<FragmentDevelopBinding> {

    private static final String KEY_TEXT = "key_text";

    public static DevelopingFragment newInstance(String text) {
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        DevelopingFragment fragment = new DevelopingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_develop;
    }

    @CallSuper
    @Override
    protected void onInitView(@Nullable Bundle savedInstanceState) {
        setText(getText());
        Bundle arguments = getArguments();
        if (arguments != null) {
            String text = arguments.getString(KEY_TEXT);
            setText(text == null ? getText() : text);
        }
    }

    //如果通过newInstance来创建的 则以传入的text为准  此方法可能会失效（当传入的text不为空时）
    protected String getText() {
        return getString(R.string.developing);
    }

    public void setText(String text) {
        mDataBinding.tvTitle.setText(text);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        String text = null;
        if (arguments != null) {
            text = arguments.getString(KEY_TEXT);
        }
        LogUtil.i(getClass().getSimpleName() + " onCreate -" + text);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.i(getClass().getSimpleName() + " hide:" + hidden);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i(getClass().getSimpleName() + " onDestroy");
    }
}
