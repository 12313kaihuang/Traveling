package com.yu.hu.traveling.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.blankj.utilcode.util.TimeUtils;
import com.yu.hu.common.cache.CacheManager;
import com.yu.hu.traveling.BR;

import java.util.Date;

/**
 * @author Hy
 * created on 2020/04/15 17:49
 * <p>
 * bing图片信息简化版
 **/
public class ImgModel extends BaseObservable {

    public static final String CACHE_KEY = "bing_img_model";

    private String url;

    private String saveTime;

    @SuppressWarnings("unused")
    @Bindable
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        notifyPropertyChanged(BR.url);
    }

    //判定缓存的图片是否过期 即是否是当天的图片
    public boolean needUpdate() {
        return !TimeUtils.isToday(saveTime);
    }

    //缓存
    public void save() {
        saveTime = TimeUtils.date2String(new Date());
        CacheManager.save(CACHE_KEY, this);
    }
}
