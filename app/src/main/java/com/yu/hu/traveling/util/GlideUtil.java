package com.yu.hu.traveling.util;

import android.content.Context;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.yu.hu.traveling.R;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.util
 * 文件名：GlideUtil
 * 创建者：HY
 * 创建时间：2019/7/2 18:37
 * 描述：  GlideUtil
 */
public class GlideUtil {

    /**
     * eg: GlideUtil.load(mContext,url).into(imageVIew)
     */
    public static DrawableRequestBuilder<String> load(Context context, String url) {
        return Glide.with(context).load(url)
                .placeholder(R.drawable.img_bg_place_holder)
                .error(R.drawable.ic_error_img);
    }


    /**
     * 不使用默认动画，这样CircleImageView就能正确加载图片了
     */
    public static DrawableRequestBuilder<String> loadWithoutAnimate(Context context, String url) {
        return Glide.with(context).load(url)
                .centerCrop()
                .dontAnimate()//防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
                .placeholder(R.drawable.img_bg_place_holder)
                .error(R.drawable.ic_error_img);
    }
}
