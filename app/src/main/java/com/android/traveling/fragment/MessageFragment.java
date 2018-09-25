package com.android.traveling.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.traveling.R;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.fragment
 * 文件名：MessageFragment
 * 创建者：HY
 * 创建时间：2018/9/22 13:59
 * 描述：  消息
 */

public class MessageFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }
}
