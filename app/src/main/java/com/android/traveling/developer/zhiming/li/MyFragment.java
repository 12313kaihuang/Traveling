package com.android.traveling.developer.zhiming.li;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.yu.hu.adaptor.NewsAdapter;
import com.android.traveling.developer.yu.hu.ui.NewsActivity;
import com.android.traveling.developer.zhiming.li.ui.UserEditActivity;
import com.android.traveling.entity.note.Note;
import com.android.traveling.entity.user.DetailUserInfo;
import com.android.traveling.entity.user.DetailUserInfoCallback;
import com.android.traveling.entity.user.TravelingUser;
import com.android.traveling.entity.user.User;
import com.android.traveling.fragment.BaseFragment;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.StaticClass;
import com.android.traveling.util.UtilTools;
import com.android.traveling.widget.MyActionBar2;
import com.squareup.picasso.Picasso;

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

    //    private static final String TAG = "MyFragment";

    private DrawerLayout my_rawerLayout;
    CircleImageView my_user_bg;
    ImageView iv_user_bg;
    private TextView my_user_status;
    private TextView focusNum;
    private TextView fansNum;
    private TextView collectionsNum;
    private TextView tv_no_notes;
    private ListView listView;
    private ScrollView scrollView;
    private MyActionBar2 myActionBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        LogUtil.d("MyFragment  onCreateView");

        registerReceiver();

        //        User currentUser = TravelingUser.getCurrentUser();
        //        if (currentUser != null) {
        //            LogUtil.d(TAG, currentUser.toString());
        //            UtilTools.toast(getContext(), currentUser.getImg());
        //        }

        initView(view);
        initData();
        return view;
    }

    protected void registerReceiver() {
        //注册receiver
        IntentFilter loginFilter = new IntentFilter();
        loginFilter.addAction(StaticClass.BROADCAST_LOGIN);

        IntentFilter logoutFilter = new IntentFilter();
        logoutFilter.addAction(StaticClass.BROADCAST_LOGOUT);
        if (getActivity() != null) {
            getActivity().registerReceiver(logoutReceiver, logoutFilter);
            getActivity().registerReceiver(loginReceiver, loginFilter);
        }

    }

    //初始化view
    private void initView(View view) {
        setDefaultFont(view);

        myActionBar = view.findViewById(R.id.my_action_bar);
        scrollView = view.findViewById(R.id.my_scroll_view);

        my_user_bg = view.findViewById(R.id.my_user_bg);
        my_user_status = view.findViewById(R.id.my_user_status);
        iv_user_bg = view.findViewById(R.id.iv_user_bg);
        listView = view.findViewById(R.id.recycler_view);

        //用户详细信息
        focusNum = view.findViewById(R.id.my_focus_num); //关注数
        fansNum = view.findViewById(R.id.my_fans_num); //粉丝数
        collectionsNum = view.findViewById(R.id.my_collections_num);  //获赞与收藏
        tv_no_notes = view.findViewById(R.id.tv_no_notes);  //没有发布过文章
        refreshUserDetail();


        //点击事件
        ImageView my_sort = view.findViewById(R.id.my_sort);
        ImageView my_share = view.findViewById(R.id.my_share);
        CircleImageView my_user_bg = view.findViewById(R.id.my_user_bg);
        TextView my_to_vip = view.findViewById(R.id.my_to_vip);
        my_sort.setOnClickListener(this);
        my_share.setOnClickListener(this);
        my_user_bg.setOnClickListener(this);
        my_to_vip.setOnClickListener(this);

        //点击事件
        myActionBar.setOnClickListener(new MyActionBar2.onClickListener() {
            @Override
            public void onRightClick(View v) {
                my_rawerLayout.openDrawer(Gravity.START);
            }

            @Override
            public void onLeftClick(View v) {
                UtilTools.toast(getContext(), "点击了分享");
            }
        });

        //scrollView滑动监听
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                int hideY = iv_user_bg.getHeight() - myActionBar.getHeight();
                float alpha = (float) scrollY / hideY;
                alpha = alpha > 1 ? 1.0f : alpha;
                //改变透明度
                myActionBar.changeAlpha(alpha);
            });
        }
    }

    private void refreshUserDetail() {
        User currentUser = TravelingUser.getCurrentUser();
        if (currentUser != null) {
            myActionBar.setTitle(currentUser.getNickName());
            currentUser.getDetailInfo(new DetailUserInfoCallback() {
                @Override
                public void onSuccess(DetailUserInfo detailUserInfo, boolean isFocus) {
                    focusNum.setText(String.valueOf(detailUserInfo.getFocusNum()));
                    fansNum.setText(String.valueOf(detailUserInfo.getFansNum()));
                    collectionsNum.setText(String.valueOf(detailUserInfo.getBeLikeNum()));
                    if (detailUserInfo.getNotes().size() == 0) {
                        tv_no_notes.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    } else {
                        tv_no_notes.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        //adapter
                        listView.setAdapter(new NewsAdapter(getActivity(), detailUserInfo.getNotes()));
                        //点击事件
                        listView.setOnItemClickListener((parent, view, position, id) -> {
                            Note note = detailUserInfo.getNotes().get(position);
                            Intent intent = new Intent(getActivity(), NewsActivity.class);
                            intent.putExtra(NewsActivity.POSITION, position);
                            intent.putExtra(NewsActivity.s_NOTE, note);
                            startActivityForResult(intent, 1);  //注意 跳转过去后setResult后需Finish()才会正确传回结果来！
                        });
                    }
                    scrollToTop();
                }

                @Override
                public void onFailure(String reason) {
                    UtilTools.toast(getContext(), "加载失败：" + reason);
                }
            });
        }
    }


    //初始化数据
    private void initData() {
        User currentUser = TravelingUser.getCurrentUser();

        LogUtil.d(currentUser == null ? "Myfargment current=null" : "Myfargment current=" + currentUser);
        if (currentUser != null) {
            LogUtil.d("MyFragment currentUser!=null");
            LogUtil.d("currentUser.getNickName()" + currentUser.getNickName());
            my_user_status.setText(currentUser.getNickName());
            Picasso.get().load(currentUser.getImg()).error(R.drawable.err_img_bg).fit().into(my_user_bg);
            Picasso.get().load(currentUser.getBackgroundImg()).error(R.drawable.err_img_bg).fit().into(iv_user_bg);
        } else {
            LogUtil.d("MyFragment currentUser=null");
            my_user_status.setText(getString(R.string.not_login));
            my_user_bg.setImageResource(R.drawable.user);
            iv_user_bg.setImageResource(R.drawable.user_bg);
            focusNum.setText("0");
            fansNum.setText("0");
            collectionsNum.setText("0");
            tv_no_notes.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

    }

    /**
     * scrollView 滚动到顶部
     */
    public void scrollToTop() {
        if (scrollView == null) {
            LogUtil.d("scrollView == null");
            return;
        }
        scrollView.scrollTo(0, 0);
        scrollView.smoothScrollTo(0, 0);
    }


    //设置字体
    private void setDefaultFont(View view) {
        TextView my_focus = view.findViewById(R.id.my_focus);
        TextView my_focus_num = view.findViewById(R.id.my_focus_num);
        TextView my_fans = view.findViewById(R.id.my_fans);
        TextView my_fans_num = view.findViewById(R.id.my_fans_num);
        TextView my_collections = view.findViewById(R.id.my_collections);
        TextView my_collections_num = view.findViewById(R.id.my_collections_num);
        my_rawerLayout = view.findViewById(R.id.my_rawerLayout);

        UtilTools.setDefaultFontType(getContext(), my_focus);
        UtilTools.setDefaultFontType(getContext(), my_focus_num);
        UtilTools.setDefaultFontType(getContext(), my_fans);
        UtilTools.setDefaultFontType(getContext(), my_fans_num);
        UtilTools.setDefaultFontType(getContext(), my_collections);
        UtilTools.setDefaultFontType(getContext(), my_collections_num);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d("MyFragment  onDestroyView");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_sort:  //更多
                my_rawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.my_share:
                UtilTools.toast(getContext(), "点击了分享");
                break;
            case R.id.my_user_bg: //点击了头像
                if (TravelingUser.getCurrentUser() != null) {
                    startActivity(new Intent(getActivity(), UserEditActivity.class));
                } else {
                    UtilTools.toast(getContext(), "未登录状态无法编辑个人信息");
                }
                break;
            case R.id.my_to_vip:
                UtilTools.toast(getContext(), "点击了成为黑卡会员");
                break;
        }
    }

    private BroadcastReceiver loginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshUserDetail();
        }
    };

    private BroadcastReceiver logoutReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            initData();
            my_rawerLayout.closeDrawer(Gravity.START);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d("MyFragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("MyFragment onResume");
        initData();
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d("MyFragment onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d("MyFragment onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("MyFragment onDestroy");
        //noinspection ConstantConditions
        getActivity().unregisterReceiver(logoutReceiver);
    }
}
