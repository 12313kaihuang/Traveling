package com.yu.hu.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yu.hu.library.R;
import com.yu.hu.library.util.AppUtil;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;


/**
 * 文件名：SimpleActionBar
 * 创建者：HY
 * 创建时间：2019/6/22 20:45
 * 描述： 自定义ActionBar
 */
@SuppressWarnings("unused")
public class SimpleActionBar extends LinearLayout implements ICustomView {

    private TextView mTitle;

    private ImageView mLeftImg, mRightImg;

    private OnBtnClickListener mClickListener;

    public SimpleActionBar(Context context) {
        this(context, null);
    }

    public SimpleActionBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleActionBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.widght_action_bar, this, true);

        initView(context);
        initParams(context, attrs);
    }

    @Override
    public void initView(Context context) {
        setMinimumHeight(AppUtil.dip2px(context, 56));
        setGravity(Gravity.CENTER_VERTICAL);

        if (getBackground() == null) {
            setBackgroundColor(getResources().getColor(R.color.default_action_bar_bg));
        }

        mTitle = findViewById(R.id.tv_title);
        mLeftImg = findViewById(R.id.iv_left);
        mRightImg = findViewById(R.id.iv_right);

        mLeftImg.setOnClickListener(v -> {
            if (mClickListener != null) {
                mClickListener.onLeftBtnClick(v);
            }
        });
        mRightImg.setOnClickListener(v -> {
            if (mClickListener != null) {
                mClickListener.onRightBtnClick(v);
            }
        });
    }

    /**
     * 初始化自定义属性
     */
    @Override
    public void initParams(Context context, AttributeSet attrs) {
        if (null == attrs) return;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleActionBar);

        //标题
        String title = typedArray.getString(R.styleable.SimpleActionBar_title);
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
        }
        //标题大小
        mTitle.setTextSize(
                //转换成sp
                AppUtil.px2sp(context,
                        //获得px值
                        typedArray.getDimensionPixelSize(R.styleable.SimpleActionBar_title_size,
                                //默认20sp
                                AppUtil.sp2px(context, 20))));
        //标题颜色
        mTitle.setTextColor(typedArray.getColor(R.styleable.SimpleActionBar_title_color,
                getResources().getColor(R.color.black)));

        //左侧图标
        Drawable leftDrawable = typedArray.getDrawable(R.styleable.SimpleActionBar_left_icon);
        if (leftDrawable != null) {
            mLeftImg.setBackground(leftDrawable);
            mLeftImg.setVisibility(VISIBLE);
        }

        //右侧图标
        Drawable rightDrawable = typedArray.getDrawable(R.styleable.SimpleActionBar_right_icon);
        if (rightDrawable != null) {
            mRightImg.setBackground(rightDrawable);
            mRightImg.setVisibility(VISIBLE);
        }

        typedArray.recycle();
    }

    /**
     * 改变透明度
     *
     * @param alpha alpha
     */
    public void changeAlpha(float alpha) {
        if (alpha < 0.5f) {
            mLeftImg.setImageResource(R.drawable.ic_sort);
            mRightImg.setImageResource(R.drawable.ic_share);
        } else {
            mLeftImg.setImageResource(R.drawable.ic_sort_black);
            mRightImg.setImageResource(R.drawable.ic_share_black);
        }
        getBackground().setAlpha((int) (alpha * 255));
        mTitle.setAlpha(alpha);
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    /**
     * 设置标题字体大小
     */
    public void setTitleSize(float dpSize) {
        mTitle.setTextSize(dpSize);
    }

    /**
     * 设置标题颜色
     */
    public void setTitleColor(@ColorRes int color) {
        mTitle.setTextColor(getResources().getColor(color));
    }

    /**
     * 设置左边图标
     */
    public void setLeftIcon(@DrawableRes int res) {
        mLeftImg.setImageResource(res);
        if (mLeftImg.getVisibility() != VISIBLE) {
            mLeftImg.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置右边图标
     */
    public void setRightIcon(@DrawableRes int res) {
        mRightImg.setImageResource(res);
        if (mRightImg.getVisibility() != VISIBLE) {
            mRightImg.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置点击事件
     */
    public void setOnBtnClickListener(OnBtnClickListener onBtnClickListener) {
        this.mClickListener = onBtnClickListener;
    }

    public interface OnBtnClickListener {
        void onLeftBtnClick(View v);

        void onRightBtnClick(View v);
    }
}
