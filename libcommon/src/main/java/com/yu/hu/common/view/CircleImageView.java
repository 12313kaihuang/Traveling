package com.yu.hu.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.yu.hu.common.utils.LogUtil;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author Hy
 * created on 2020/04/16 12:42
 * <p>
 * 可绘制圆角的ImageView
 * @see #setImageUrl(CircleImageView, String, boolean, int)  设置圆角
 * @see #setBlurImageUrl(ImageView, String, int)   设置高斯模糊图
 **/
@SuppressWarnings("unused")
public class CircleImageView extends AppCompatImageView {

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ViewHelper.setViewOutLine(this, attrs, defStyleAttr, 0);
    }

    /**
     * 设置图片url与大小
     *
     * @param widthPx    图片宽度
     * @param heightPx   图片高度
     * @param marginLeft 左边间距 dp
     * @param imageUrl   图片url
     */
    public void bindData(int widthPx, int heightPx, int marginLeft, String imageUrl) {
        bindData(widthPx, heightPx, marginLeft, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenWidth(), imageUrl);
    }

    /**
     * 设置图片url与大小
     * 1. 宽为0或高为0 根据图片实际大小等比按下面方式设置大小
     * 2. 宽大于高  宽为屏幕宽度，高等比设置
     * 3. 高大于宽  高为屏幕宽度，宽等比设置
     */
    public void bindData(int widthPx, int heightPx, final int marginLeft, final int maxWidth, final int maxHeight, String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }
        //宽或高等于0且没有加载过
        if ((widthPx <= 0 || heightPx <= 0) && !hasLoaded(this, imageUrl)) {
            //noinspection deprecation
            Glide.with(this).load(imageUrl).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    int height = resource.getIntrinsicHeight();
                    int width = resource.getIntrinsicWidth();
                    setSize(width, height, marginLeft, maxWidth, maxHeight);

                    setImageDrawable(resource);
                }
            });
            return;
        }

        setSize(widthPx, heightPx, marginLeft, maxWidth, maxHeight);
        setImageUrl(this, imageUrl, false);
    }

    private void setSize(int width, int height, int marginLeft, int maxWidth, int maxHeight) {
        int finalWidth, finalHeight;
        if (width > height) {
            finalWidth = maxWidth;
            finalHeight = (int) (height / (width * 1.0f / finalWidth));
        } else {
            finalHeight = maxHeight;
            finalWidth = (int) (width / (height * 1.0f / finalHeight));
        }

        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = finalWidth;
        params.height = finalHeight;
        if (params instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) params).leftMargin = height > width ? ConvertUtils.dp2px(marginLeft) : 0;
        } else if (params instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) params).leftMargin = height > width ? ConvertUtils.dp2px(marginLeft) : 0;
        }
        setLayoutParams(params);
    }

    public void setImageUrl(String imageUrl) {
        setImageUrl(this, imageUrl, false);
    }

    @BindingAdapter(value = {"image_url", "isCircle"})
    public static void setImageUrl(CircleImageView view, String imageUrl, boolean isCircle) {
        setImageUrl(view, imageUrl, isCircle, 0);
    }

    //圆角  如果radius设置0 则直接裁剪成圆形的
    @SuppressLint("CheckResult")
    @BindingAdapter(value = {"image_url", "isCircle", "radius"}, requireAll = false)
    public static void setImageUrl(CircleImageView view, String imageUrl, boolean isCircle, int radius) {
        if (hasLoaded(view, imageUrl)) {
            return;
        }

        RequestBuilder<Drawable> builder = Glide.with(view).load(imageUrl);
        if (isCircle) {
            builder.transform(new CircleCrop());
        } else if (radius > 0) {
            builder.transform(new RoundedCornersTransformation(ConvertUtils.dp2px(radius), 0));
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null && layoutParams.width > 0 && layoutParams.height > 0) {
            builder.override(layoutParams.width, layoutParams.height);
        }
        builder.into(view);
    }

    //设置高斯模糊图
    @BindingAdapter(value = {"blur_url", "radius"})
    public static void setBlurImageUrl(ImageView imageView, String blurUrl, int radius) {
        setBlurImageUrl(imageView, blurUrl, radius, true);
    }

    /**
     * @param needCache 是否需要缓存，如果{@code  true}则下次执行该方法是会先判断是否已加载过，如果已加载过就不会再次加载了
     */
    @BindingAdapter(value = {"blur_url", "radius", "need_cache"})
    public static void setBlurImageUrl(ImageView imageView, String blurUrl, int radius, boolean needCache) {
        if (needCache && hasLoaded(imageView, blurUrl)) {
            return;
        }
        Glide.with(imageView).load(blurUrl).override(radius)
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
            LogUtil.i(loadedImgUrl + "-图片已加载");
            return true;
        }
        imageView.setTag(targetUrl);
        return false;
    }
}
