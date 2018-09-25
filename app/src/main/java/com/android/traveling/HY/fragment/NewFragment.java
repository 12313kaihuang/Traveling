package com.android.traveling.HY.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.traveling.R;
import com.android.traveling.util.LogUtil;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.fragment
 * 文件名：MyFragment
 * 创建者：HY
 * 创建时间：2018/9/22 13:59
 * 描述：  最新
 */

public class NewFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("NewFragment onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("NewFragment onCreate");
        return inflater.inflate(R.layout.fragment_new, container, false);
    }


}
