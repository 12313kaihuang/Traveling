package com.yu.hu.traveling.ui.home;

import android.app.Application;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;
import com.yu.hu.common.fragment.DevelopingFragment;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.traveling.model.HomeTab;
import com.yu.hu.traveling.ui.extension.FightVirusFragment;
import com.yu.hu.traveling.utils.AppConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hy
 * created on 2020/04/15 19:02
 **/
public class HomeViewModel extends AndroidViewModel {

    private Context mContext;
    @SuppressWarnings("WeakerAccess")
    public HomeTab tabConfig;
    public List<HomeTab.Tab> tabs;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        getTabConfig();
    }

    //加载Tab配置项
    private void getTabConfig() {
        tabs = new ArrayList<>();
        tabConfig = AppConfig.getHomeTabConfig();
        for (HomeTab.Tab tab : tabConfig.tabs) {
            if (tab.enable) {
                tabs.add(tab);
            }
        }
    }

    //创建FragmentStateAdapter
    //viewPager2默认只有一种类型的Adapter。FragmentStateAdapter
    //并且在页面切换的时候 不会调用子Fragment的setUserVisibleHint ，取而代之的是onPause(),onResume()、
    FragmentStateAdapter createAdapter(FragmentManager manager, Lifecycle lifecycle) {
        return new FragmentStateAdapter(manager, lifecycle) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                HomeTab.Tab tab = tabs.get(position);
                LogUtil.d("createFragment tag = " + tab.tag);
                if (null == tab.tag) {
                    return DevelopingFragment.newInstance("");
                }
                switch (tab.tag) {
                    case FightVirusFragment.EXTENSION_TAG_VIRUS:
                        return FightVirusFragment.newInstance();
                    case NoteViewModel.TAG_FOCUS:
                        return FocusedNoteListFragment.newInstance();
                    case NoteViewModel.TAG_NEW:
                    case NoteViewModel.TAG_NOTE:
                    case NoteViewModel.TAG_STRATEGY:
                    case NoteViewModel.TAG_COLLECT:
                    case NoteViewModel.TAG_HISTORY:
                    case NoteViewModel.TAG_SELF_NOTE:
                    case NoteViewModel.TAG_SELF_STRATEGY:
                        return NoteListFragment.newInstance(tab.tag);
                    default:
                        return DevelopingFragment.newInstance(tab.tag);
                }
            }

            @Override
            public int getItemCount() {
                return tabs.size();
            }
        };
    }

    /**
     * 生成TabConfigurationStrategy
     */
    TabLayoutMediator.TabConfigurationStrategy createTabConfigurationStrategy() {
        return (tab, position) -> {
            HomeTab.Tab itemConfig = tabs.get(position);
            TextView tabView = new TextView(mContext);

            int[][] states = new int[2][];
            states[0] = new int[]{android.R.attr.state_selected};
            states[1] = new int[]{};

            int[] colors;
            if (TextUtils.isEmpty(itemConfig.activeColor)) {
                LogUtil.i("activeColor - " + tabConfig.activeColor);
                colors = new int[]{Color.parseColor(tabConfig.activeColor), Color.parseColor(tabConfig.normalColor)};
            } else {
                colors = new int[]{Color.parseColor(itemConfig.activeColor), Color.parseColor(itemConfig.normalColor)};
            }
            ColorStateList colorStateList = new ColorStateList(states, colors);
            //tabView.setBackgroundColor(Color.parseColor("#f8e97c"));
            tabView.setGravity(Gravity.CENTER);
            tabView.setText(itemConfig.title);
            tabView.setTextColor(colorStateList);
            tabView.setTextSize(tabConfig.normalSize);

            tab.setCustomView(tabView);
        };
    }
}
