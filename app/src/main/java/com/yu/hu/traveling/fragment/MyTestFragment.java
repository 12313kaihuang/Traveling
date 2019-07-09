package com.yu.hu.traveling.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yu.hu.library.fragment.BaseFragment;
import com.yu.hu.library.util.LogUtil;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.activity.LoginActivity;
import com.yu.hu.traveling.db.Controller.UserDaoController;
import com.yu.hu.traveling.entity.Const;
import com.yu.hu.traveling.entity.user.TravelingUser;
import com.yu.hu.traveling.entity.user.User;
import com.yu.hu.traveling.entity.bus.BusMessage;
import com.yuntongxun.plugin.common.SDKCoreHelper;
import com.yuntongxun.plugin.im.manager.IMPluginManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

import static com.yu.hu.traveling.entity.bus.BusMessage.MESSAGE_TYPE_LOGIN;
import static com.yu.hu.traveling.entity.bus.BusMessage.MESSAGE_TYPE_LOGOUT;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.fragment
 * 文件名：MyTestFragment
 * 创建者：HY
 * 创建时间：2019/6/24 9:18
 * 描述：  我的 页面
 */
public class MyTestFragment extends BaseFragment {

    @BindView(R.id.et_userId)
    EditText mEtUserId;

    @BindView(R.id.btn_chat)
    Button mChatBtn;

    @BindView(R.id.btn_login)
    Button mLoginBtn;

    @BindView(R.id.btn_logout)
    Button mLogoutBtn;

    @BindView(R.id.tv_current_user)
    TextView mTv;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_test;
    }

    @Override
    protected void onPrepare(@Nullable Bundle savedInstanceState) {
        //注册
        EventBus.getDefault().register(this);

        User currentUser = TravelingUser.getCurrentUser();
        if (currentUser != null) {
            mTv.setText("当前用户：" + currentUser.getNickName() + " id=" + currentUser.getId());
        }
    }

    @OnClick(R.id.btn_chat)
    void toChat() {
        String sId = mEtUserId.getText().toString();
        if (!TextUtils.isEmpty(sId)) {
            UserDaoController.getInstance().findById(Long.valueOf(sId));
            //跳转到聊天界面
            IMPluginManager.getManager().startChatting(getContext(), sId);
        }
    }

    @OnClick(R.id.btn_login)
    void login() {
        ActivityUtils.startActivity(LoginActivity.class);
    }

    //退出登录
    @OnClick(R.id.btn_logout)
    void logout() {
        SDKCoreHelper.logout();
        SPUtils.getInstance().remove(Const.SP_KEY_CURRENT_USER);
        EventBus.getDefault().post(new BusMessage(MESSAGE_TYPE_LOGOUT));
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAcceptMessage(BusMessage message) {
        if (message.messageType == MESSAGE_TYPE_LOGIN) {
            //登录
            LogUtil.d("MyTestFragment -- onAcceptLoginMessage");
            mTv.setText("当前用户：" + message.user.getNickName());
        } else if (message.messageType == MESSAGE_TYPE_LOGOUT) {
            mTv.setText("未登录");
            LogUtil.d("MyTestFragment -- onAcceptLogoutMessage");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消注册
        EventBus.getDefault().unregister(this);
    }
}
