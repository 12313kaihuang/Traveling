package com.yu.hu.traveling.model;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.GsonUtils;

import java.util.List;

/**
 * @author Hy
 * created on 2020/04/15 18:27
 * <p>
 * 首页tab配置信息
 **/
@SuppressWarnings("unused")
public class HomeTab {

    public static final String CACHE_KEY = "com.yu.hu.traveling.model.HomeTab";

    /**
     * activeSize : 16
     * normalSize : 14
     * activeColor : #f8e97c
     * normalColor : #666666
     * select : 1
     * tabGravity : 0
     * tabs : [{"title":"关注","index":0,"tag":"focus","enable":true},{"title":"推荐","index":1,"tag":"recommend","enable":true},{"title":"游记","index":1,"tag":"note","enable":true},{"title":"攻略","index":1,"tag":"strategy","enable":true}]
     */

    public int activeSize;
    public int normalSize;
    public String activeColor;
    public String normalColor;
    public int select;
    public int tabGravity;
    public List<Tab> tabs;

    public static class Tab {
        /**
         * title : 关注
         * index : 0
         * tag : focus
         * enable : true
         */

        public String title;
        public int index;
        public String tag;
        public String activeColor;  //这里可以单独设置某个tab的颜色 但是一定要同时设置activeColor和normalColor
        public String normalColor;
        public boolean enable;
    }

    /*,
    {
      "title": "抗击肺炎",
      "index": 1,
      "normalColor": "#666666",
      "activeColor": "#666666",
      "tag": "strategy",
      "enable": true
    }*/

    @NonNull
    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
