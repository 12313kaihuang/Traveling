package com.yu.hu.traveling.ui.extension;

import android.os.Bundle;

import com.yu.hu.common.fragment.BaseFragment;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.FragmentFightVirusBinding;

/**
 * @author Hy
 * created on 2020/04/29 10:19
 * <p>
 * 抗击疫情
 **/
public class FightVirusFragment extends BaseFragment<FragmentFightVirusBinding> {

    public static final String EXTENSION_TAG_VIRUS = "extension_nCoV2019";

    public static FightVirusFragment newInstance() {
        Bundle args = new Bundle();
        FightVirusFragment fragment = new FightVirusFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fight_virus;
    }
}
