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

        initView(view);
        return view;
    }

    //初始化view
    private void initView(View view) {
        TextView tv_toGuide = view.findViewById(R.id.tv_toGuide);
        tv_toGuide.setOnClickListener(v -> startActivity(new Intent(getActivity(), GuideActivity.class)));
    }
}
