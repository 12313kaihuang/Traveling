package com.android.traveling.developer.zhiming.li;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.zhiming.li.ui.GuideActivity;
import com.android.traveling.fragment.BaseFragment;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.UtilTools;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li
 * 文件名：MyFragment
 * 创建者：HY
 * 创建时间：2018/9/22 13:59
 * 描述：  我的
 */

public class MyFragment extends BaseFragment {

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

        UtilTools.setDefaultFontType(getContext(),my_focus);
        UtilTools.setDefaultFontType(getContext(),my_focus_num);
        UtilTools.setDefaultFontType(getContext(),my_fans);
        UtilTools.setDefaultFontType(getContext(),my_fans_num);
        UtilTools.setDefaultFontType(getContext(),my_collections);
        UtilTools.setDefaultFontType(getContext(),my_collections_num);
        UtilTools.setDefaultFontType(getContext(),my_edit_userData);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d("MyFragment  onDestroyView");
    }
}
