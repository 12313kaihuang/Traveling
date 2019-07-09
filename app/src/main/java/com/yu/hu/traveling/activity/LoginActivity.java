package com.yu.hu.traveling.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yu.hu.library.activity.BaseActivity;
import com.yu.hu.library.util.LogUtil;
import com.yu.hu.library.util.Utils;
import com.yu.hu.library.widget.dialog.HLoadingDialog;
import com.yu.hu.library.widget.dialog.LifeCycleListener;
import com.yu.hu.traveling.entity.Const;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.entity.user.User;
import com.yu.hu.traveling.entity.bus.BusMessage;
import com.yu.hu.traveling.mvp.LoginPresenter;
import com.yu.hu.traveling.mvp.impl.ILoginPresence;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.Subscription;

import static com.yu.hu.traveling.entity.bus.BusMessage.MESSAGE_TYPE_LOGIN;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.activity
 * 文件名：LoginActivity
 * 创建者：HY
 * 创建时间：2019/6/24 9:36
 * 描述：  登录
 */
public class LoginActivity extends BaseActivity<ILoginPresence, LoginPresenter>
        implements ILoginPresence {

    @BindView(R.id.login_login)
    EditText username;

    @BindView(R.id.login_password)
    EditText password;

    @BindView(R.id.login_verified_code)
    EditText verified_code;

    @BindView(R.id.btn_login)
    Button btn_login;

    @BindView(R.id.btn_verified_code)
    Button btn_verified_code;

    @BindView(R.id.login_forget_pass)
    TextView login_forget_pass;

    @BindView(R.id.login_by_password)
    TextView login_by_password;

    @BindView(R.id.cut_line)
    TextView cut_line;

    @BindView(R.id.login_by_verified)
    TextView login_by_verified;

    @BindView(R.id.LL_verified)
    LinearLayout LL_verified;

    private VerifiedTimer verifiedTimer;
    //
    private HLoadingDialog landingDialog;

    //输入是否合法
    private boolean usernameValid = false;  //手机号
    private boolean emailValid = false;
    private boolean verifiedCodeValid = false;
    private boolean loginEnable = false;

    private InputContent inputContent = new InputContent();

    //登录方式
    private int loginMode = Const.LOGIN_BY_VERIFIED;
    private boolean passwordMode = true;  //true为手机账号登录 false为邮箱账号登录

    //登陆Subscription
    private Subscription mLoginSubscription;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginPresenter attachPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void onPrepare(@Nullable Bundle savedInstanceState) {
        //设置字体
        TextView tv_login = findViewById(R.id.tv_login);
        Utils.setFont(this, tv_login, Const.DEFAULT_FONT_SPLASH);

        verifiedTimer = new VerifiedTimer(60000, 1000);
    }

    /**
     * 登录成功
     */
    @Override
    public void onLoginSuccess(User user) {
        LogUtil.d("登陆成功 -- user=" + user);
        //发送广播
        EventBus.getDefault().post(new BusMessage(user, MESSAGE_TYPE_LOGIN));
        dismissDialog();
        LoginActivity.this.finish();
    }

    /**
     * 登录失败
     */
    @Override
    public void onLoginFailure(String reason) {
        dismissDialog();
    }

    /**
     * 发送验证码
     */
    @OnClick(R.id.btn_verified_code)
    public void sendVerifiedCode() {
        mPresenter.sendVerifiedCode(username.getText().toString());
    }

    @Override
    public void onVerifiedCodeSended() {
        verifiedTimer.start();
    }

    /**
     * 普通方式登录（邮箱，手机号）
     */
    @OnClick(R.id.btn_login)
    public void login() {
        showDialog();
        switch (loginMode) {
            case Const.LOGIN_BY_VERIFIED:
                //手机验证码登录
                mLoginSubscription = mPresenter.loginByCode(username.getText().toString(), verified_code.getText().toString());
                break;
            case Const.LOGIN_BY_PHONE:
                //手机号密码登录
                mLoginSubscription = mPresenter.loginByPhone(username.getText().toString(), password.getText().toString());
                break;
            case Const.LOGIN_BY_EMAIL:
                //邮箱账号登录
                mLoginSubscription = mPresenter.loginByEmail(username.getText().toString(), password.getText().toString());
                break;
        }
    }

    /**
     * 第三方登录
     */
    @OnClick({R.id.ic_qq, R.id.ic_wechat, R.id.ic_weibo})
    public void loginByThirdPart(View v) {
        switch (v.getId()) {
            case R.id.ic_qq:
                ToastUtils.showShort("qq登录");
                break;
            case R.id.ic_wechat:
                ToastUtils.showShort("微信登录");
                break;
            case R.id.ic_weibo:
                ToastUtils.showShort("微博登录");
                break;
        }
    }

    /**
     * 其他的按钮点击事件
     *
     * @param v v
     */
    @OnClick({R.id.login_forget_pass, R.id.login_by_password, R.id.login_by_verified
            , R.id.service_terms, R.id.back})
    public void otherService(View v) {
        switch (v.getId()) {
            case R.id.login_forget_pass:
                ToastUtils.showShort("忘记密码");
                break;
            case R.id.login_by_password:
                changePasswordMode();
                break;
            case R.id.login_by_verified:
                changeLoginMode();
                break;
            case R.id.service_terms:
                ToastUtils.showShort("跳转到服务条款页面");
                break;
            case R.id.back:
                //返回
                onBackPressed();
                break;
        }
    }

    //username
    @OnTextChanged(value = R.id.login_login, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onUsernameTextChanged(CharSequence s) {
        switch (loginMode) {
            case Const.LOGIN_BY_VERIFIED:
                if (s.length() == Const.PHONE_MAX_LENGTH) {
                    usernameValid = true;
                    if (verifiedCodeValid) {
                        setLoginEnabled(true);
                    }
                } else {
                    if (usernameValid) {
                        usernameValid = false;
                    }
                    if (loginEnable) {
                        setLoginEnabled(false);
                    }
                }
                break;
            case Const.LOGIN_BY_PHONE:
                if (s.length() == Const.PHONE_MAX_LENGTH) {
                    usernameValid = true;
                    if (password.getText().toString().length() >=
                            Const.PASSWORD_MIN_LENGTH) {
                        setLoginEnabled(true);
                    }
                } else {
                    if (usernameValid) {
                        usernameValid = false;
                    }

                    if (loginEnable) {
                        setLoginEnabled(false);
                    }
                }
                break;
            case Const.LOGIN_BY_EMAIL:
                if (RegexUtils.isEmail(s.toString())) {
                    emailValid = true;
                    if (password.getText().toString().length() >=
                            Const.PASSWORD_MIN_LENGTH) {
                        setLoginEnabled(true);
                    }
                } else {
                    if (emailValid) {
                        emailValid = false;
                    }

                    if (loginEnable) {
                        setLoginEnabled(false);
                    }
                }
                break;
        }
    }

    //password
    @OnTextChanged(value = R.id.login_password, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onPasswordTextChanged(CharSequence s) {
        if (s.toString().length() >= Const.PASSWORD_MIN_LENGTH) {
            if (loginMode == Const.LOGIN_BY_PHONE && usernameValid) {
                setLoginEnabled(true);
            }
            if (loginMode == Const.LOGIN_BY_EMAIL && emailValid) {
                setLoginEnabled(true);
            }
        } else {

            if (loginEnable) {
                setLoginEnabled(false);
            }

        }
    }

    //验证码输入框
    @OnTextChanged(value = R.id.login_verified_code, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onVerifiedCodeTextChanged(CharSequence s) {
        switch (loginMode) {
            case Const.LOGIN_BY_VERIFIED:
                if (s.length() == Const.VERIFIED_CODE_MAX_LENGTH) {
                    verifiedCodeValid = true;
                    if (usernameValid) {
                        setLoginEnabled(true);
                    }
                } else {
                    if (loginEnable) {
                        setLoginEnabled(false);
                    }

                    if (verifiedCodeValid) {
                        verifiedCodeValid = false;
                    }
                }
                break;
            case Const.LOGIN_BY_PHONE:
                break;
            case Const.LOGIN_BY_EMAIL:
                break;
        }
    }

    //切换登录方式时设置登录按钮是否可点击
    private void changeLoginEnabled(boolean isEnabled) {
        if (password.getText().toString().length() >= 5 && isEnabled) {
            setLoginEnabled(true);
        } else {
            setLoginEnabled(false);
        }
    }

    private void showDialog() {
        if (landingDialog == null) {
            landingDialog = new HLoadingDialog.Builder(this)
                    .setHint("正在登陆..")
                    .setIndeterminateDrawable(R.drawable.progress_bar)
                    .setLifeCycleListener(new LifeCycleListener() {
                        @Override
                        public void onCanceled(Dialog dialog) {
                            if (mLoginSubscription != null && !mLoginSubscription.isUnsubscribed()) {
                                LogUtil.d("取消了dialog -- 取消订阅，停止请求");
                                mLoginSubscription.unsubscribe();
                            }
                        }
                    })
                    .build();
        }
        landingDialog.show();
    }

    private void dismissDialog() {
        if (landingDialog != null && landingDialog.isShowing()) {
            landingDialog.dismiss();
        }
    }

    //切换登录方式
    private void changeLoginMode() {
        if (loginMode == Const.LOGIN_BY_VERIFIED) {

            LL_verified.setVisibility(View.INVISIBLE);
            password.setVisibility(View.VISIBLE);
            login_by_password.setVisibility(View.VISIBLE);
            login_forget_pass.setVisibility(View.VISIBLE);
            cut_line.setVisibility(View.VISIBLE);
            login_by_verified.setText(getString(R.string.login_by_verified));
            if (passwordMode) { //由验证码登录换为手机账号登录
                setInputType(Const.LOGIN_BY_PHONE);
                changeLoginEnabled(usernameValid);
            } else {            //由验证码登录换为邮箱账号登录
                setInputType(Const.LOGIN_BY_EMAIL);
                username.setHint(getString(R.string.input_email));
                username.setText(inputContent.email);
                changeLoginEnabled(emailValid);
            }


        } else {
            setInputType(Const.LOGIN_BY_VERIFIED);

            if (passwordMode) {
                inputContent.phoneNumber = username.getText().toString();

            } else {
                inputContent.email = username.getText().toString();
                username.setText(inputContent.phoneNumber);

            }
            LL_verified.setVisibility(View.VISIBLE);
            password.setVisibility(View.INVISIBLE);
            login_by_password.setVisibility(View.GONE);
            login_forget_pass.setVisibility(View.GONE);
            cut_line.setVisibility(View.GONE);
            username.setHint(getString(R.string.input_phone));
            login_by_verified.setText(getString(R.string.login_by_password));

            if (username.getText().toString().length() == Const.PHONE_MAX_LENGTH
                    && verified_code.getText().toString().length() == Const.VERIFIED_CODE_MAX_LENGTH) {
                setLoginEnabled(true);
            } else {
                setLoginEnabled(false);
            }
        }
    }

    /**
     * 设置登录按钮是否可点击
     *
     * @param isEnabled isEnabled
     */
    private void setLoginEnabled(boolean isEnabled) {
        loginEnable = isEnabled;
        btn_login.setEnabled(isEnabled);
        if (isEnabled) {
            btn_login.setBackgroundResource(R.drawable.blue_bg);
        } else {
            btn_login.setBackgroundResource(R.drawable.btn_gray_bg);
        }
    }

    //切换登录方式
    private void changePasswordMode() {
        switch (loginMode) {
            case Const.LOGIN_BY_EMAIL:
                setInputType(Const.LOGIN_BY_PHONE);
                passwordMode = true;
                login_by_password.setText(getString(R.string.email));
                username.setHint(getString(R.string.input_phone));

                inputContent.email = username.getText().toString();
                username.setText(inputContent.phoneNumber);
                changeLoginEnabled(usernameValid);
                break;
            case Const.LOGIN_BY_PHONE:
                passwordMode = false;
                setInputType(Const.LOGIN_BY_EMAIL);
                login_by_password.setText(getString(R.string.phone));
                username.setHint(getString(R.string.input_email));

                inputContent.phoneNumber = username.getText().toString();
                username.setText(inputContent.email);
                changeLoginEnabled(emailValid);
                break;

        }

    }

    //设置username的类型同时设置loginMode
    private void setInputType(int loginMode) {
        this.loginMode = loginMode;
        switch (loginMode) {
            case Const.LOGIN_BY_VERIFIED:
            case Const.LOGIN_BY_PHONE:
                username.setInputType(InputType.TYPE_CLASS_PHONE);
                username.setFilters(new InputFilter[]{
                        new InputFilter.LengthFilter(11)
                });
                break;
            case Const.LOGIN_BY_EMAIL:
                username.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                username.setFilters(new InputFilter[]{
                        new InputFilter.LengthFilter(50)
                });
                break;
        }

    }

    //获取验证码后读秒操作
    class VerifiedTimer extends CountDownTimer {

        private boolean hasSet = false;

        VerifiedTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (!hasSet) {
                btn_verified_code.setBackgroundResource(R.drawable.btn_verified_bg2);
                btn_verified_code.setEnabled(false);
            }
            btn_verified_code.setText(getString(R.string.verifiedMilliSecond,
                    millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            btn_verified_code.setBackgroundResource(R.drawable.btn_verified_bg);
            btn_verified_code.setText("重新发送");
            btn_verified_code.setEnabled(true);
        }
    }

    class InputContent {
        String phoneNumber = "";
        String email = "";
    }
}
