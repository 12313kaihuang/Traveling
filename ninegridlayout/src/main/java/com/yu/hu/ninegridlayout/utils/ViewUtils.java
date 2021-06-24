package com.yu.hu.ninegridlayout.utils;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.yu.hu.ninegridlayout.entity.GridItem;
import com.yu.hu.ninegridlayout.entity.SpacingConfig;

/**
 * @author Hy
 * created on 2020/04/19 10:06
 **/
@SuppressWarnings("unused")
public class ViewUtils {


    public static void setSize(ImageView imageView, GridItem item) {
        setSize(imageView, item.cover, item.width, item.height, new SpacingConfig(), PixUtils.getScreenWidth(), PixUtils.getScreenWidth());
    }

    /**
     * 根据图片/视频宽高自适应设置大小
     *
     * @param width     width
     * @param height    height
     * @param spacing   SpacingConfig item间距  dp
     * @param maxWidth  maxWidth
     * @param maxHeight maxHeight
     */
    @SuppressWarnings("WeakerAccess")
    public static void setSize(View view, String url, int width, int height, SpacingConfig spacing, int maxWidth, int maxHeight) {
        if (width == 0 || height == 0) {
            setSizeAutomatic(view, url, spacing, maxWidth, maxHeight);
        }

        int finalWidth, finalHeight;
        if (width > height) {
            //宽大于高 则宽为屏幕宽度  高等比缩放
            finalWidth = maxWidth;
            finalHeight = (int) (height / (width * 1.0f / finalWidth));
        } else {
            //高大于宽  高为屏幕宽度 宽等比缩放
            finalHeight = maxHeight;
            finalWidth = (int) (width / (height * 1.0f / finalHeight));
        }

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = finalWidth;
        params.height = finalHeight;
        if (params instanceof GridLayoutManager.LayoutParams) {
            //height > width ? PixUtils.dp2px(spacing) : 0
            ((GridLayoutManager.LayoutParams) params).leftMargin = PixUtils.dp2px(spacing.left);
            ((GridLayoutManager.LayoutParams) params).topMargin = PixUtils.dp2px(spacing.top);
            ((GridLayoutManager.LayoutParams) params).rightMargin = PixUtils.dp2px(spacing.right);
            ((GridLayoutManager.LayoutParams) params).bottomMargin = PixUtils.dp2px(spacing.bottom);
        }
        view.setLayoutParams(params);
    }

    //自己获取图片大小并设置
    @SuppressWarnings("WeakerAccess")
    public static void setSizeAutomatic(final View view, final String url, final SpacingConfig spacing, final int maxWidth, final int maxHeight) {
        Glide.with(view.getContext()).load(url).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                int height = resource.getIntrinsicHeight();
                int width = resource.getIntrinsicWidth();
                setSize(view, url, width, height, spacing, maxWidth, maxHeight);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                if (placeholder == null) {
                    return;
                }
                int height = placeholder.getIntrinsicHeight();
                int width = placeholder.getIntrinsicWidth();
                setSize(view, url, width, height, spacing, maxWidth, maxHeight);
            }
        });

    }
}


