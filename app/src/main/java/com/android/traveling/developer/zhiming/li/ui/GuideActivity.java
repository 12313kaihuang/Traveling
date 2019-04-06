package com.android.traveling.developer.zhiming.li.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.traveling.MainActivity;
import com.android.traveling.R;
import com.android.traveling.util.StaticClass;
import com.android.traveling.util.UtilTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.ui
 * 文件名：GuideActivity
 * 创建者：HY
 * 创建时间：2018/9/26 19:49
 * 描述：  引导页（第一次运行程序出现）
 */

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    //容器
    private List<View> viewList = new ArrayList<>();
    //小圆点
    private ImageView point1,point2,point3;
    //跳过按钮
    ImageView guide_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
    }

    //初始化View
    private void initView() {
        ViewPager guide_viewPager = findViewById(R.id.guide_viewPager);

        point1 = findViewById(R.id.guide_point1);
        point2 = findViewById(R.id.guide_point2);
        point3 = findViewById(R.id.guide_point3);

        View view1 = View.inflate(this, R.layout.guide_item_one, null);
        View view2 = View.inflate(this, R.layout.guide_item_two, null);
        View view3 = View.inflate(this, R.layout.guide_item_three, null);

        //设置字体
        TextView tv_pager_1 = view1.findViewById(R.id.tv_pager_1);
        TextView tv_pager_2 = view2.findViewById(R.id.tv_pager_2);
        TextView tv_pager_3 = view3.findViewById(R.id.tv_pager_3);
        UtilTools.setFont(this,tv_pager_1, StaticClass.FONT);
        UtilTools.setFont(this,tv_pager_2, StaticClass.FONT);
        UtilTools.setFont(this,tv_pager_3, StaticClass.FONT);

        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);

        //进入主页按钮
        Button guide_start = view3.findViewById(R.id.guide_start);
        guide_start.setOnClickListener(this);

        //跳过
        guide_back = findViewById(R.id.guide_back);
        guide_back.setOnClickListener(this);

        //设置原点默认图片
        setPointImage(0);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guide_start:
            case R.id.guide_back:
                startActivity(new Intent(this, MainActivity.class));
                finish();
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
