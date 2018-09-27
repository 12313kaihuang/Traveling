package com.android.traveling.developer.ting.li;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.traveling.R;
import com.android.traveling.fragment.BaseFragment;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.fragment
 * 文件名：FriendsFragment
 * 创建者：HY
 * 创建时间：2018/9/22 13:59
 * 描述：  结伴
 */

public class FriendsFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }
}
