package com.android.traveling.developer.zhiming.li.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.zhiming.li.ui.NewsSettingActivity;
import com.android.traveling.developer.zhiming.li.ui.AccountBindActivity;
import com.android.traveling.developer.zhiming.li.ui.ConfigActivity;
import com.android.traveling.developer.zhiming.li.ui.LoginActivity;
import com.android.traveling.developer.zhiming.li.ui.UserEditActivity;
import com.android.traveling.entity.user.TravelingUser;
import com.android.traveling.entity.user.User;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.StaticClass;
import com.android.traveling.util.UtilTools;
import com.android.traveling.widget.dialog.ToLogoutDialog;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

import cn.leancloud.chatkit.LCChatKit;


/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.fragment
 * 文件名：AboutMoreFragment
 * 创建者：HY
 * 创建时间：2018/10/12 18:25
 * 描述：  关于更多侧滑菜单
 */

public class AboutMoreFragment extends Fragment implements View.OnClickListener {

    private TextView about_area;
    private TextView about_logout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_more, container, false);

        IntentFilter filter = new IntentFilter();
        filter.addAction(StaticClass.BROADCAST_LOGIN);
        if (getActivity() != null) {
            getActivity().registerReceiver(loginReceiver, filter);
        }


        initView(view);
        initData();
        return view;
    }

    private void initData() {
        User currentUser = TravelingUser.getCurrentUser();
        LogUtil.d(currentUser==null?"aboutMore current=null":"aboutMore current="+currentUser);
        if (currentUser != null) {
            about_area.setText(getString(R.string.about_live_area,currentUser.getArea()));
            about_logout.setCompoundDrawablesWithIntrinsicBounds( getResources().
                            getDrawable(R.drawable.ic_logout,null),null,
                    null,null);
            about_logout.setText(getResources().getString(R.string.about_logout));
        }else {
            about_area.setText(getString(R.string.about_area));
            about_logout.setCompoundDrawablesWithIntrinsicBounds( getResources().
                            getDrawable(R.drawable.ic_about_login,null),null,
                    null,null);
            about_logout.setText(getResources().getString(R.string.about_login));
        }
    }

    private void initView(View view) {

        about_area = view.findViewById(R.id.about_area);
        about_logout = view.findViewById(R.id.about_logout);
        TextView about_more = view.findViewById(R.id.about_more);

        TextView about_edit = view.findViewById(R.id.about_edit);
        TextView about_bind = view.findViewById(R.id.about_bind);
        TextView about_privacy = view.findViewById(R.id.about_privacy);
        TextView clear_cache = view.findViewById(R.id.clear_cache);
        TextView message_setting = view.findViewById(R.id.message_setting);
        about_edit.setOnClickListener(this);
        about_logout.setOnClickListener(this);
        about_bind.setOnClickListener(this);
        about_privacy.setOnClickListener(this);
        about_more.setOnClickListener(this);
        clear_cache.setOnClickListener(this);
        message_setting.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_edit:
                if (TravelingUser.getCurrentUser() != null) {
                    startActivity(new Intent(getActivity(), UserEditActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.about_bind:
                if (TravelingUser.getCurrentUser() != null) {
                    startActivity(new Intent(getActivity(), AccountBindActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.about_privacy:
                if (TravelingUser.getCurrentUser() != null) {
                    UtilTools.toast(getContext(), "隐私设置");
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.about_logout:

                if (TravelingUser.getCurrentUser() != null) {

                    new ToLogoutDialog(getContext(), () -> {
                        TravelingUser.logout();
                        Logout();
                    }).show();

                } else {
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }
                break;
            case R.id.about_more:
                startActivity(new Intent(getActivity(), ConfigActivity.class));
                break;
            case R.id.clear_cache:
                UtilTools.toast(getContext(), "清除缓存");
                break;
            case R.id.message_setting:
                startActivity(new Intent(getActivity(), NewsSettingActivity.class));
                break;
        }
    }

    //退出登录操作
    private void Logout() {
        initData();
        try {
            LCChatKit.getInstance().close(new AVIMClientCallback() {
                @Override
                public void done(AVIMClient avimClient, AVIMException e) {
                    if (null != e) {
                        e.printStackTrace();
                        LogUtil.d("===================LeanCloud退出登录失败===================");
                    }
                    LogUtil.d("===================LeanCloud退出登录===================");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("LeanCloud退出登录异常");
        }

        Intent intent = new Intent();
        intent.setAction(StaticClass.BROADCAST_LOGOUT);
        //noinspection ConstantConditions
        getActivity().sendBroadcast(intent);
        UtilTools.toast(getContext(), "已退出当前用户");
    }

    private BroadcastReceiver loginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            initData();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() != null) {
            getActivity().unregisterReceiver(loginReceiver);
        }
        LogUtil.d("AboutMoreFragment onDestroy");
    }
}
