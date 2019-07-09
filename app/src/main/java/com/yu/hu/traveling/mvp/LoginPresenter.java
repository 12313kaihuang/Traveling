package com.yu.hu.traveling.mvp;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yu.hu.library.mvp.BasePresenter;
import com.yu.hu.library.util.LogUtil;
import com.yu.hu.library.util.RetrofitUtil;
import com.yu.hu.library.util.RxUtil;
import com.yu.hu.traveling.entity.Const;
import com.yu.hu.traveling.activity.LoginActivity;
import com.yu.hu.traveling.db.Controller.UserDaoController;
import com.yu.hu.traveling.entity.user.User;
import com.yu.hu.traveling.entity.exception.NullResponseException;
import com.yu.hu.traveling.entity.msg.LoginMsg;
import com.yu.hu.traveling.entity.msg.Msg;
import com.yu.hu.traveling.mvp.impl.ILoginPresence;
import com.yu.hu.traveling.retrofit.UserService;
import com.yu.hu.traveling.rx.GlobalObserver;
import com.yuntongxun.plugin.common.ClientUser;
import com.yuntongxun.plugin.common.SDKCoreHelper;


import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;


/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.mvp
 * 文件名：LoginPresenter
 * 创建者：HY
 * 创建时间：2019/6/24 11:12
 * 描述：  登录相关Presenter
 *
 * @see LoginActivity
 */
public class LoginPresenter extends BasePresenter<ILoginPresence> {

    //UserService
    private UserService mUserService;

    private LoginTransformer mLoginTransformer;

    public LoginPresenter(ILoginPresence impl) {
        super(impl);
        mUserService = RetrofitUtil.getInstance().getService(UserService.class);
        mLoginTransformer = new LoginTransformer();
    }

    /**
     * 发送验证码
     */
    public void sendVerifiedCode(String phoneNumber) {
        if (!RegexUtils.isMobileSimple(phoneNumber)) {
            LogUtil.d("请输入正确的手机号码");
            return;
        }

        mUserService.sendVerifiedCode(phoneNumber)
                .compose(RxUtil.defaultSchedulers())
                .subscribe(new GlobalObserver<Msg>() {
                    @Override
                    public void onNext(Msg msg) {
                        if (msg.isStatusCorrect()) {
                            //倒计时
                            mInterface.onVerifiedCodeSended();
                            ToastUtils.showShort("验证码发送成功，请注意查收！");
                        } else {
                            ToastUtils.showShort("errInfo=" + msg.getInfo());
                        }
                    }
                });
    }

    /**
     * 手机号密码登录
     */
    public Subscription loginByPhone(String phoneNumber, String password) {
        if (!RegexUtils.isMobileSimple(phoneNumber)) {
            LogUtil.d("请输入正确的手机号码");
            return null;
        }

        LogUtil.d("----  手机密码登录   ----");
        return login(mUserService.loginByPass(phoneNumber, password));
    }

    /**
     * 手机验证码登录
     */
    public Subscription loginByCode(String phoneNumber, String verificationCode) {
        if (!RegexUtils.isMobileSimple(phoneNumber)) {
            LogUtil.d("请输入正确的手机号码");
            return null;
        }

        LogUtil.d("----  手机验证码登录   ----");
        return login(mUserService.loginByCode(phoneNumber, verificationCode));
    }

    /**
     * 邮箱密码登录
     */
    public Subscription loginByEmail(String email, String password) {
        if (!RegexUtils.isEmail(email)) {
            LogUtil.d("请输入正确的邮箱号");
            return null;
        }

        LogUtil.d("----  邮箱密码登录   ----");
        return login(mUserService.loginByEmail(email, password));
    }

    //登录返回的结果统一处理
    private Subscription login(Observable<LoginMsg> observable) {
        return observable.compose(RxUtil.defaultSchedulers())
                .flatMap(mLoginTransformer)
                .subscribe(mLoginObserver);
    }

    //拆解LoginMsg并转换成User
    private class LoginTransformer implements Func1<LoginMsg, Observable<User>> {

        @Override
        public Observable<User> call(LoginMsg loginMsg) {
            if (!loginMsg.isStatusCorrect()) {
                throw new LoginException(loginMsg.getInfo());
            }
            if (loginMsg.getUser() == null) {
                throw new NullResponseException("user == null");
            }
            return Observable.just(loginMsg.getUser());
        }
    }

    //LoginSubscriber
    private GlobalObserver<User> mLoginObserver = new GlobalObserver<User>() {
        @Override
        public void onNext(User user) {
            //容联登录操作
            ClientUser.UserBuilder builder = new ClientUser.UserBuilder(String.valueOf(user.getId()), user.getNickName());
            builder.setAppKey(Const.APP_ID_RongLY);
            builder.setAppToken(Const.APP_TOKEN_RongLY);
            SDKCoreHelper.login(builder.build());

            //存入本地缓存
            UserDaoController.getInstance().insertOrReplace(user);
            SPUtils.getInstance().put(Const.SP_KEY_CURRENT_USER, user.getId());

            mInterface.onLoginSuccess(user);
        }

        @Override
        public void _onError(Throwable e) {
            mInterface.onLoginFailure(e.getMessage());
        }
    };

    /**
     * 自定义异常,用于区分
     *
     * @see GlobalObserver
     */
    public class LoginException extends RuntimeException {

        LoginException(String message) {
            super(message);
        }

    }
}
