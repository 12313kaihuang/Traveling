package com.android.traveling.developer.yu.hu;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.searchview.SearchView;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;
import android.widget.EditText;

import com.android.traveling.developer.yu.hu.fragment.FocusOnFragment;
import com.android.traveling.developer.yu.hu.fragment.NewFragment;
import com.android.traveling.developer.yu.hu.fragment.RecommendFragment;
import com.android.traveling.R;
import com.android.traveling.fragment.BaseFragment;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.UtilTools;
import com.android.traveling.util.XunfeiUtil;
import com.iflytek.cloud.SpeechError;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu
 * 文件名：HomeFragment
 * 创建者：HY
 * 创建时间：2018/9/22 13:59
 * 描述：  首页
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private static final int REQUEST_CODE_RECORD_AUDIO = 1;
    private static final String TAG = "HomeFragment";

    ViewPager viewPager;
    SearchView searchView;
    //Title
    private List<String> titles;
    //Fragment
    private List<Fragment> fragments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        LogUtil.d("HomeFragment onCreateView");
        initView(view);
        initData();
        addEvents();
        return view;

    }

    private void addEvents() {
        searchView.setImageButtonVoiceClickListener((input, voice, view1) -> {

            //申请录音权限
            //noinspection ConstantConditions
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                showSpeechDialog(input);
            } else {
                //权限申请
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getParentFragment() != null) {
                        getParentFragment().requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                                REQUEST_CODE_RECORD_AUDIO);
                    } else {
                        requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                                REQUEST_CODE_RECORD_AUDIO);
                    }

                }
            }

        });


    }

    //初始化数据
    private void initData() {
        titles = new ArrayList<>();
        titles.add(getString(R.string.fragment_new));
        titles.add(getString(R.string.fragment_recommend));
        titles.add(getString(R.string.fragment_focus_on));

        fragments = new ArrayList<>();
        fragments.add(new NewFragment());
        fragments.add(new RecommendFragment());
        fragments.add(new FocusOnFragment());
    }

    //初始化view
    private void initView(View view) {
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        //语音输入
        searchView = view.findViewById(R.id.search_view);


        //预加载  todo
        viewPager.setOffscreenPageLimit(2);

        //设置适配器
        //这里要使用getChildFragmentManager 否则HomeFragment再次创建时，子Fragment不会显示
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            //设置标题
            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        });

        //默认显示"推荐"
        viewPager.setCurrentItem(1);

        //绑定
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * 显示并进行语音输入
     *
     * @param input editText
     */
    private void showSpeechDialog(EditText input) {
        XunfeiUtil.showSpeechDialog(getActivity(), new XunfeiUtil.onRecognizerResult() {
            @Override
            public void onSuccess(String result) {
                input.setText(result);// 设置输入框的文本
                input.requestFocus(); //请求获取焦点
                input.setSelection(input.length());//把光标定位末尾
            }

            @Override
            public void onFaild(JSONException e) {
                UtilTools.toast(getContext(), "onFaild e=" + e);
            }

            @Override
            public void onError(SpeechError speechError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //            case R.id.home_search:
            //                UtilTools.toast(getContext(),"点击了搜索图标");
            //                break;
            default:
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        LogUtil.d(TAG, " onRequestPermissionsResult");
        if (requestCode == REQUEST_CODE_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //同意开启权限
                showSpeechDialog(searchView.getEt_input());
            } else {
                //不同意开启权限
                UtilTools.toast(getActivity(), "开启录制音频权限后才可进行语音输入！");
            }
        }
    }
}
