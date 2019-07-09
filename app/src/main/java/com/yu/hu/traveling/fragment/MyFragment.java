package com.yu.hu.traveling.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.yu.hu.library.fragment.BaseFragment;
import com.yu.hu.library.util.LogUtil;
import com.yu.hu.library.util.Utils;
import com.yu.hu.library.widget.SimpleActionBar;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.adapter.NotesAdapter;
import com.yu.hu.traveling.entity.Const;
import com.yu.hu.traveling.entity.user.TravelingUser;
import com.yu.hu.traveling.entity.user.User;
import com.yu.hu.traveling.mvp.MyPresenter;
import com.yu.hu.traveling.mvp.impl.MyPrensence;
import com.yu.hu.traveling.rx.GlobalObserver;
import com.yu.hu.traveling.rx.ViewInitObservable;
import com.yu.hu.traveling.util.GlideUtil;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.fragment
 * 文件名：MyFragment
 * 创建者：HY
 * 创建时间：2019/6/22 10:44
 * 描述：  测试用
 */
public class MyFragment extends BaseFragment<MyPrensence, MyPresenter>
        implements MyPrensence {

    @BindView(R.id.iv_user_bg)
    ImageView mUserBg;  //背景图片

    @BindView(R.id.my_user_bg)
    CircleImageView mUserImg; //头像

    @BindView(R.id.simple_action_bar)
    SimpleActionBar mActionBar;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected MyPresenter attachPresenter() {
        return new MyPresenter(this);
    }

    @Override
    protected void onPrepare(@Nullable Bundle savedInstanceState) {

        mActionBar.changeAlpha(0);
        //异步进行初始化操作
        ViewInitObservable.create(() -> Observable.just("")).subscribe(new GlobalObserver<String>() {
            @Override
            public void onNext(String s) {
                initView();
                setDefaultFont();
            }
        });

    }

    //初始化view
    private void initView() {
        RecyclerView recyclerView = mView.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new NotesAdapter(getContext()));

        SimpleActionBar actionBar = mView.findViewById(R.id.simple_action_bar);
        User currentUser = TravelingUser.getCurrentUser();
        if (currentUser != null) {
            LogUtil.d("currentUser.getBackgroundImg() = " + currentUser.getBackgroundImg());
            GlideUtil.load(getContext(), currentUser.getBackgroundImg()).into(mUserBg);
            GlideUtil.loadWithoutAnimate(getContext(), currentUser.getImgUrl()).into(mUserImg);
            actionBar.setTitle(currentUser.getNickName());
        }

        DrawerLayout drawerLayout = mView.findViewById(R.id.drawer_layout);
        actionBar.setOnBtnClickListener(new SimpleActionBar.OnBtnClickListener() {
            @Override
            public void onLeftBtnClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }

            @Override
            public void onRightBtnClick(View v) {
                ToastUtils.showShort("分享功能暂未开启");
            }
        });
    }

    //设置默认字体
    @SuppressWarnings("ConstantConditions")
    private void setDefaultFont() {
        TextView my_focus = mView.findViewById(R.id.my_focus);
        TextView my_focus_num = mView.findViewById(R.id.my_focus_num);
        TextView my_fans = mView.findViewById(R.id.my_fans);
        TextView my_fans_num = mView.findViewById(R.id.my_fans_num);
        TextView my_collections = mView.findViewById(R.id.my_collections);
        TextView my_collections_num = mView.findViewById(R.id.my_collections_num);

        Utils.setFont(getContext(), my_focus, Const.DEFAULT_FONT_NORMAL);
        Utils.setFont(getContext(), my_focus_num, Const.DEFAULT_FONT_NORMAL);
        Utils.setFont(getContext(), my_fans, Const.DEFAULT_FONT_NORMAL);
        Utils.setFont(getContext(), my_fans_num, Const.DEFAULT_FONT_NORMAL);
        Utils.setFont(getContext(), my_collections, Const.DEFAULT_FONT_NORMAL);
        Utils.setFont(getContext(), my_collections_num, Const.DEFAULT_FONT_NORMAL);
    }
}
