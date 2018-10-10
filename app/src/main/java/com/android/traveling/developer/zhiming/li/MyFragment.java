package com.android.traveling.developer.zhiming.li;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.zhiming.li.ui.GuideActivity;
import com.android.traveling.developer.zhiming.li.ui.LoginActivity;
import com.android.traveling.fragment.BaseFragment;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.UtilTools;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li
 * 文件名：MyFragment
 * 创建者：HY
 * 创建时间：2018/9/22 13:59
 * 描述：  我的
 */

public class MyFragment extends BaseFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        LogUtil.d("MyFragment  onCreateView");
        initView(view);
        return view;
    }

    //初始化view
    private void initView(View view) {
        setDefaultFont(view);
        TextView tv_toGuide = view.findViewById(R.id.tv_toGuide);
        tv_toGuide.setOnClickListener(v -> startActivity(new Intent(getActivity(), GuideActivity.class)));

        TextView tv_to_login = view.findViewById(R.id.tv_to_login);
        tv_to_login.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        ImageView my_sort = view.findViewById(R.id.my_sort);
        ImageView my_share = view.findViewById(R.id.my_share);
        CircleImageView my_user_bg = view.findViewById(R.id.my_user_bg);
        TextView my_to_vip = view.findViewById(R.id.my_to_vip);
        TextView my_edit_userData = view.findViewById(R.id.my_edit_userData);
        my_sort.setOnClickListener(this);
        my_share.setOnClickListener(this);
        my_user_bg.setOnClickListener(this);
        my_to_vip.setOnClickListener(this);
        my_edit_userData.setOnClickListener(this);
    }

    //设置字体
    private void setDefaultFont(View view) {
        TextView my_focus = view.findViewById(R.id.my_focus);
        TextView my_focus_num = view.findViewById(R.id.my_focus_num);
        TextView my_fans = view.findViewById(R.id.my_fans);
        TextView my_fans_num = view.findViewById(R.id.my_fans_num);
        TextView my_collections = view.findViewById(R.id.my_collections);
        TextView my_collections_num = view.findViewById(R.id.my_collections_num);
        TextView my_edit_userData = view.findViewById(R.id.my_edit_userData);

        UtilTools.setDefaultFontType(getContext(), my_focus);
        UtilTools.setDefaultFontType(getContext(), my_focus_num);
        UtilTools.setDefaultFontType(getContext(), my_fans);
        UtilTools.setDefaultFontType(getContext(), my_fans_num);
        UtilTools.setDefaultFontType(getContext(), my_collections);
        UtilTools.setDefaultFontType(getContext(), my_collections_num);
        UtilTools.setDefaultFontType(getContext(), my_edit_userData);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d("MyFragment  onDestroyView");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_sort:
                UtilTools.toast(getContext(), "点击了更多");
                break;
            case R.id.my_share:
                UtilTools.toast(getContext(), "点击了分享");
                break;
            case R.id.my_user_bg:
                UtilTools.toast(getContext(), "点击了头像");
                break;
            case R.id.my_to_vip:
                UtilTools.toast(getContext(), "点击了成为黑卡会员");
                break;
            case R.id.my_edit_userData:
                UtilTools.toast(getContext(), "点击了编辑个人资料");
                break;
        }
    }
}
