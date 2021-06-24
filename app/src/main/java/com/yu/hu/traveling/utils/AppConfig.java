package com.yu.hu.traveling.utils;

import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.reflect.TypeToken;
import com.yu.hu.common.cache.CacheManager;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.common.utils.ResourceUtil;
import com.yu.hu.traveling.model.BottomBar;
import com.yu.hu.traveling.model.Destination;
import com.yu.hu.traveling.model.HomeTab;

import java.util.Collections;
import java.util.HashMap;


/**
 * @author Hy
 * created on 2020/04/15 10:36
 * <p>
 * 用于获取全局配置项，如Destination
 **/
public class AppConfig {

    private static final String TAG = "AppConfig";

    private static HashMap<String, Destination> sDestConfig;

    private static BottomBar sBottomBar;

    private static HomeTab sHomeTab;

    public static HashMap<String, Destination> getDestConfig() {
        if (sDestConfig == null) {
            sDestConfig = ResourceUtil.getFromAssets("destnation.json",
                    new TypeToken<HashMap<String, Destination>>() {
                    }.getType());

        }
        return sDestConfig;
    }

    //底部BottomNavigationBar配置信息
    public static BottomBar getBottomBarConfig() {
        if (sBottomBar == null) {
            BottomBar bar = CacheManager.get(BottomBar.CACHE_KEY, BottomBar.class);
            if (bar != null) {
                LogUtil.d(TAG, "getBottomBarConfig - 加载缓存配置信息 - " + GsonUtils.toJson(bar));
                sBottomBar = bar;
            } else {
                sBottomBar = ResourceUtil.getFromAssets("main_tabs_config.json", BottomBar.class);
                LogUtil.d(TAG, "getBottomBarConfig - 加载默认配置文件 - " + GsonUtils.toJson(sBottomBar));
            }
        }
        return sBottomBar;
    }

    //首页tab配置信息
    public static HomeTab getHomeTabConfig() {
        if (sHomeTab == null) {
            HomeTab tab = CacheManager.get(HomeTab.CACHE_KEY, HomeTab.class);
            if (tab != null) {
                LogUtil.d(TAG, "getHomeTabConfig - 加载缓存配置信息 - " + GsonUtils.toJson(tab));
                sHomeTab = tab;
            } else {
                sHomeTab = ResourceUtil.getFromAssets("home_tabs_config.json", HomeTab.class);
                LogUtil.d(TAG, "getHomeTabConfig - 加载默认配置文件 - " + GsonUtils.toJson(sBottomBar));
            }
            Collections.sort(sHomeTab.tabs, (o1, o2) -> o1.index < o2.index ? -1 : 0);
        }
        return sHomeTab;
    }

    /**
     * 根据pageUrl获取对应destination的id
     */
    public static int getDestItemId(String pageUrl) {
        Destination destination = getDestConfig().get(pageUrl);
        if (destination == null)
            return -1;
        return destination.id;
    }

    /**
     * @param pageUrl pageUrl
     * @return 对应底部导航栏的icon图标
     */
    public static int getIconRes(String pageUrl) {
        Destination destination = getDestConfig().get(pageUrl);
        if (destination == null || destination.iconRes <= 0) {
            String msg = pageUrl + (destination == null ? "- destination == null" : "- iconRes is error");
            throw new RuntimeException(msg);
        }
        return destination.iconRes;
    }
}
