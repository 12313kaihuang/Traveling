package com.yu.hu.traveling.model;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.GsonUtils;

import java.util.List;

/**
 * @author Hy
 * created on 2020/04/15 11:12
 * <p>
 * 对应底部导航栏
 **/
@SuppressWarnings("unused")
public class BottomBar {

    public static final String CACHE_KEY = "com.yu.hu.traveling.model.BottomBar";

    /**
     * activeColor : #333333
     * inActiveColor : #666666
     * selectTab : 0
     * tabs : [{"size":24,"enable":true,"index":0,"pageUrl":"main/tabs/home","title":"首页"},{"size":24,"enable":true,"index":1,"pageUrl":"main/tabs/sofa","title":"沙发"},{"size":40,"enable":true,"index":2,"tintColor":"#ff678f","pageUrl":"main/tabs/publish","title":""},{"size":24,"enable":true,"index":3,"pageUrl":"main/tabs/find","title":"发现"},{"size":24,"enable":true,"index":4,"pageUrl":"main/tabs/my","title":"我的"}]
     */

    public String activeColor;
    public String inActiveColor;
    public int selectTab;  //默认选中项
    public List<TabsBean> tabs;

    public static class TabsBean {
        /**
         * size : 24
         * enable : true
         * index : 0
         * pageUrl : main/tabs/home
         * title : 首页
         * tintColor : #ff678f
         */

        public int size;
        public boolean enable;
        public int index;
        public String pageUrl;
        public String title;
        public String tintColor;  //单独着色 不受上面的全局颜色配置影响
    }

    @NonNull
    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
