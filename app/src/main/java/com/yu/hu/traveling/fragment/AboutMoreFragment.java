package com.yu.hu.traveling.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yu.hu.library.fragment.BaseFragment;
import com.yu.hu.library.util.LogUtil;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.activity.LoginActivity;
import com.yu.hu.traveling.entity.Const;
import com.yu.hu.traveling.entity.user.TravelingUser;
import com.yu.hu.traveling.entity.user.User;
import com.yu.hu.traveling.entity.bus.BusMessage;
import com.yuntongxun.plugin.common.SDKCoreHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.Nullable;
import butterknife.OnClick;

import static com.yu.hu.traveling.entity.bus.BusMessage.MESSAGE_TYPE_LOGIN;
import static com.yu.hu.traveling.entity.bus.BusMessage.MESSAGE_TYPE_LOGOUT;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.fragment
 * 文件名：AboutMoreFragment
 * 创建者：HY
 * 创建时间：2019/7/5 13:21
 * 描述：  更多
 */
public class AboutMoreFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about_more;
    }

    @Override
    protected void onPrepare(@Nullable Bundle savedInstanceState) {
        //注册
        EventBus.getDefault().register(this);

        initView();
    }

    //登录或退出登录
    @OnClick(R.id.about_logout)
    void loginOrLoginout() {
        User currentUser = TravelingUser.getCurrentUser();
        if (currentUser != null) {
            //退出登录
            SDKCoreHelper.logout();
            SPUtils.getInstance().remove(Const.SP_KEY_CURRENT_USER);
            EventBus.getDefault().post(new BusMessage(MESSAGE_TYPE_LOGOUT));
        } else {
            //登录
            ActivityUtils.startActivity(LoginActivity.class);
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAcceptMessage(BusMessage message) {
        if (message.messageType == MESSAGE_TYPE_LOGIN) {
            //登录
            LogUtil.d("MyTestFragment -- onAcceptLoginMessage");
            initView();
            //mTv.setText("当前用户：" + message.user.getNickName());
        } else if (message.messageType == MESSAGE_TYPE_LOGOUT) {
            //mTv.setText("未登录");
            initView();
            LogUtil.d("MyTestFragment -- onAcceptLogoutMessage");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消注册
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        TextView mAreaTv = mView.findViewById(R.id.about_area);
        TextView mLoginOrOutTv = mView.findViewById(R.id.about_logout);

        User currentUser = TravelingUser.getCurrentUser();

        if (currentUser == null) {
            //未登录状态
            //about_area.setText(getString(R.string.about_area));
            mLoginOrOutTv.setCompoundDrawablesWithIntrinsicBounds(getResources().
                            getDrawable(R.drawable.ic_about_login, null), null,
                    null, null);
            mLoginOrOutTv.setText(getResources().getString(R.string.about_login));
        } else {
            //登录状态
            mAreaTv.setText(getString(R.string.about_live_area, currentUser.getArea()));

            mLoginOrOutTv.setText(getString(R.string.about_logout));
            mLoginOrOutTv.setCompoundDrawablesWithIntrinsicBounds(getResources().
                            getDrawable(R.drawable.ic_logout, null), null,
                    null, null);
        }

    }


}
