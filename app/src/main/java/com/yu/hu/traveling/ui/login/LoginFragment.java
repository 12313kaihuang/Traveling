package com.yu.hu.traveling.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;
import com.yu.hu.libnavannotation.FragmentDestination;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.FragmentLoginBinding;
import com.yu.hu.traveling.fragment.BackInterceptFragment;
import com.yu.hu.traveling.model.User;

/**
 * @author Hy
 * created on 2020/04/15 22:36
 **/
@FragmentDestination(pageUrl = "login")
public class LoginFragment extends BackInterceptFragment<FragmentLoginBinding>
        implements LoginViewModel.LoginCallback {

    public static final String PAGE_URL = "login";
    private static final String TAG = "LoginFragment";

    private LoginViewModel mViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void onInitView(@Nullable Bundle savedInstanceState) {
        mViewModel = createViewModel(LoginViewModel.class);
        mViewModel.setLoginCallback(this);
        mDataBinding.loginBtn.setOnClickListener(v -> {
            showLoadingDialog(getString(R.string.logining));
            mViewModel.login(requireActivity());
        });
        mDataBinding.back.setOnClickListener(v -> handleOnBackPressed());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //加上这句 否则会报错 No permission to write APN settings
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mViewModel.getLoginListener());
        }
    }

    @Override
    public void handleOnBackPressed() {
        ((MainActivity) mActivity).popBackStack();
    }

    @Override
    public void onLoginSuccess(User user) {
        hideLoadingDialog();
        handleOnBackPressed();
    }

    @Override
    public void onLoginCancel() {
        hideLoadingDialog();
        ToastUtils.showShort("登录取消");
    }

    @Override
    public void onLoginError(String errorMsg) {
        Log.e(TAG, "登录失败 - " + errorMsg);
    }
}
