package com.yu.hu.traveling.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.yu.hu.library.activity.BaseActivity;
import com.yu.hu.library.util.Utils;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.entity.Const;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.ui
 * 文件名：GuideActivity
 * 创建者：HY
 * 创建时间：2018/9/26 19:49
 * 描述：  引导页（第一次运行程序出现）
 */

public class GuideActivity extends BaseActivity implements View.OnClickListener {

    //小圆点
    @BindView(R.id.guide_point1)
    ImageView point1;

    @BindView(R.id.guide_point2)
    ImageView point2;

    @BindView(R.id.guide_point3)
    ImageView point3;

    //跳过按钮
    @BindView(R.id.guide_back)
    ImageView guide_back;

    //容器
    private List<View> viewList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void onPrepare(@Nullable Bundle savedInstanceState) {
        initViewPager();

        //跳过点击事件
        guide_back.setOnClickListener(this);

        //设置原点默认图片
        setPointImage(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guide_start:
            case R.id.guide_back:
                ActivityUtils.startActivity(MainActivity.class);
                finish();
                break;
            default:
                break;
        }
    }

    //初始化viewPager
    private void initViewPager() {
        ViewPager guide_viewPager = findViewById(R.id.guide_viewPager);

        View view1 = View.inflate(this, R.layout.item_guide_one, null);
        View view2 = View.inflate(this, R.layout.item_guide_two, null);
        View view3 = View.inflate(this, R.layout.item_guide_three, null);

        //设置字体
        setDefaultFont(view1, view2, view3);

        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);

        //设置适配器
        guide_viewPager.setAdapter(new GuideAdaptor());

        //滑动监听事件
        guide_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPointImage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //进入主页按钮
        Button guide_start = view3.findViewById(R.id.guide_start);
        guide_start.setOnClickListener(this);
    }

    //设置默认字体
    private void setDefaultFont(View view1, View view2, View view3) {
        TextView tv_pager_1 = view1.findViewById(R.id.tv_pager_1);
        TextView tv_pager_2 = view2.findViewById(R.id.tv_pager_2);
        TextView tv_pager_3 = view3.findViewById(R.id.tv_pager_3);

        Utils.setFont(this, tv_pager_1, Const.DEFAULT_FONT_GUIDE);
        Utils.setFont(this, tv_pager_2, Const.DEFAULT_FONT_GUIDE);
        Utils.setFont(this, tv_pager_3, Const.DEFAULT_FONT_GUIDE);
    }

    //设置小圆点选中效果
    private void setPointImage(int selectedPosition) {
        point1.setBackgroundResource(R.drawable.point_off);
        point2.setBackgroundResource(R.drawable.point_off);
        point3.setBackgroundResource(R.drawable.point_off);
        switch (selectedPosition) {
            case 0:
                point1.setBackgroundResource(R.drawable.point_on);
                guide_back.setVisibility(View.VISIBLE);
                break;
            case 1:
                point2.setBackgroundResource(R.drawable.point_on);
                guide_back.setVisibility(View.VISIBLE);
                break;
            case 2:
                point3.setBackgroundResource(R.drawable.point_on);
                guide_back.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    class GuideAdaptor extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(viewList.get(position));
            //super.destroyItem(container, position, object);
        }
    }
}
