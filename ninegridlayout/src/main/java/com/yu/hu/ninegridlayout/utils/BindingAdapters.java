package com.yu.hu.ninegridlayout.utils;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.yu.hu.ninegridlayout.entity.SpacingConfig;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * @author Hy
 * created on 2020/04/17 21:35
 **/
@SuppressWarnings("unused")
public class BindingAdapters {


    @BindingAdapter("image_url")
    public static void setImageUrl(ImageView imageView, String image_url) {
        if (hasLoaded(imageView, image_url)) {
            return;
        }
        Glide.with(imageView).load(image_url)
                .into(imageView);
    }

    @BindingAdapter("image_url_auto")
    public static void setImageUrl2(final ImageView imageView, final String image_url_auto) {
        if (hasLoaded(imageView, image_url_auto)) {
            return;
        }
        Glide.with(imageView).load(image_url_auto)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        int height = resource.getIntrinsicHeight();
                        int width = resource.getIntrinsicWidth();
                        ViewUtils.setSize(imageView, image_url_auto, width, height, new SpacingConfig(), PixUtils.getScreenWidth(), PixUtils.getScreenWidth());
                        imageView.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        if (placeholder == null) {
                            return;
                        }
                        int height = placeholder.getIntrinsicHeight();
                        int width = placeholder.getIntrinsicWidth();
                        ViewUtils.setSize(imageView, image_url_auto, width, height, new SpacingConfig(), PixUtils.getScreenWidth(), PixUtils.getScreenWidth());
                        imageView.setImageDrawable(placeholder);
                    }
                });
    }

    @BindingAdapter("blur_url")
    public static void setBlurImgUrl(final ImageView imageView, String blur_url) {
        //blur暂时不记录了  个人首页这里会出问题
//        if (hasLoaded(imageView, blur_url)) {
//            return;
//        }
        Glide.with(imageView).load(blur_url)
                .transform(new BlurTransformation())
                .dontAnimate()
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        imageView.setBackground(placeholder);
                    }
                });
    }


    /**
     * 判断imageView是否真的需要执行网络加载
     *
     * @param imageView imageView
     * @param targetUrl 需要加载的url
     * @return 是否需要加载 {@code false}说明已经加载过这个url了
     */
    private static boolean hasLoaded(ImageView imageView, String targetUrl) {
        if (TextUtils.isEmpty(targetUrl)) {
            return true;
        }

        //通过tag存储已加载图片的url  如果相同就不重新加载了
        String loadedImgUrl = (String) imageView.getTag();
        if (TextUtils.equals(loadedImgUrl, targetUrl)) {
            return true;
        }
        imageView.setTag(targetUrl);
        return false;
    }
}
