package com.yu.hu.traveling.ui.companion;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.yu.hu.common.fragment.BaseFragment;
import com.yu.hu.libnavannotation.FragmentDestination;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.FragmentCompanionBinding;

/**
 * @author Hy
 * created on 2020/05/05 14:19
 * <p>
 * 结伴页面，仅添加了一个title，实际数据展示在{@link CompanionListFragment}
 **/
@SuppressWarnings("unused")
@FragmentDestination(pageUrl = "main/tabs/companion", tabPage = true, iconRes = R.drawable.icon_tab_friends)
public class CompanionFragment extends BaseFragment<FragmentCompanionBinding> {

    @Override
    protected void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);

        //加入真正的聊天列表
        CompanionListFragment fragment = CompanionListFragment.newInstance(null);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_layout_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_companion;
    }
}
