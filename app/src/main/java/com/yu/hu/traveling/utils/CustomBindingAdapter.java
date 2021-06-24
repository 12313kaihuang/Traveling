package com.yu.hu.traveling.utils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.yu.hu.traveling.ui.splash.SplashViewModel;

/**
 * @author Hy
 * created on 2020/04/14 17:33
 * <p>
 * CustomBindingAdapter
 * 传递的属性貌似不能带有泛型
 **/
@SuppressWarnings("unused")
public class CustomBindingAdapter {

    @BindingAdapter(value = {"image_url", "viewModel"})
    public static void setImageUrl(ImageView imageView, String imageUrl, SplashViewModel viewModel) {
        Glide.with(imageView)
                .load(imageUrl)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        viewModel.getLoadSuccess().postValue(true);
                        return false;
                    }
                })
                .into(imageView);
    }

}
