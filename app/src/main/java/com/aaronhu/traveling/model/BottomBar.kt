package com.aaronhu.traveling.model

import com.aaronhu.base.ext.toJson

/**
 * huyu create
 * 2025/3/31 16:56
 */
class BottomBar(
    /**
     * activeColor : #333333
     * inActiveColor : #666666
     * selectTab : 0
     * tabs : [{"size":24,"enable":true,"index":0,"pageUrl":"main/tabs/home","title":"首页"},{"size":24,"enable":true,"index":1,"pageUrl":"main/tabs/sofa","title":"沙发"},{"size":40,"enable":true,"index":2,"tintColor":"#ff678f","pageUrl":"main/tabs/publish","title":""},{"size":24,"enable":true,"index":3,"pageUrl":"main/tabs/find","title":"发现"},{"size":24,"enable":true,"index":4,"pageUrl":"main/tabs/my","title":"我的"}]
     */
    val activeColor: String? = null,
    val inActiveColor: String? = null,
    val selectTab: Int = 0, //默认选中项
    val tabs: List<TabsBean>? = null
) {

    /**
     * size : 24
     * enable : true
     * index : 0
     * pageUrl : main/tabs/home
     * title : 首页
     * tintColor : #ff678f
     */
    data class TabsBean(
        val size: Int = 0,
        val enable: Boolean = false,
        val index: Int = 0,
        val pageUrl: String? = null,
        val title: String? = null,
        val tintColor: String? = null //单独着色 不受上面的全局颜色配置影响
    )

    override fun toString(): String = toJson()

    companion object {
        const val CACHE_KEY: String = "com.yu.hu.traveling.model.BottomBar"
    }
}
