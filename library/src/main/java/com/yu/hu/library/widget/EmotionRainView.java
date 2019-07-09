package com.yu.hu.library.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.ConvertUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;

/**
 * 文件名：EmotionRainView
 * 创建者：HY
 * 创建时间：2019/6/11 14:56
 * 描述：  自定义控件 表情雨控件
 * <p>
 * 记得给控件爷爷布局添加{@code android:clipChildren="false"}属性，否则卡片堆叠效果可能会没用
 *
 * @see EmotionBean 所需实体类
 * @see #startRain(List)  开始
 * @see #stopRain() 停止
 */
@SuppressWarnings("unused")
public class EmotionRainView extends View implements ICustomView {

    private boolean isRaining = false;

    private int mEmotionHeight, mEmotionWidth;

    private Random mRandom;

    //Bean对象
    private List<EmotionBean> mEmotionBeanList = new ArrayList<>();

    private Matrix mMatrix;

    //画笔
    private Paint mPaint;

    private long mStartTimeStamp;

    public EmotionRainView(Context context) {
        this(context, null);
    }

    public EmotionRainView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmotionRainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    public void initView(Context context) {
        mRandom = new Random();
        mMatrix = new Matrix();
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(android.R.color.holo_red_light));
    }

    /**
     * 开始表情雨
     *
     * @param bitmapList bitmap资源列表 表情雨中的图片
     */
    public void startRain(final List<Bitmap> bitmapList) {
        stopRain();
        setVisibility(VISIBLE);
        initData(bitmapList);
        isRaining = true;
        invalidate();
    }

    /**
     * 停止表情雨
     */
    public void stopRain() {
        setVisibility(GONE);
        isRaining = false;
    }

    /**
     * 初始化数据Bean
     */
    private void initData(List<Bitmap> bitmapList) {
        mEmotionHeight = mEmotionWidth = ConvertUtils.dp2px(50);

        mEmotionBeanList.clear();
        mStartTimeStamp = System.currentTimeMillis();

        int maxDuration = 2000;
        int currentDuration = 0;
        int i = 0;
        int size = bitmapList.size();
        while (currentDuration < maxDuration) {
            EmotionBean emotionBean = new EmotionBean();
            emotionBean.bitmap = bitmapList.get(i % size);
            emotionBean.x = mRandom.nextInt(getWidth());
            //Math.ceil 向上取整
            emotionBean.y = -(int) Math.ceil(mEmotionHeight);

            int duration = mRandom.nextInt(500) + maxDuration;
            emotionBean.velocityY = getHeight() * 16 / duration;
            //Math.round 四舍五入
            emotionBean.velocityX = Math.round(mRandom.nextFloat());
            mEmotionBeanList.add(emotionBean);

            currentDuration += mRandom.nextInt(250);
            i++;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isRaining || mEmotionBeanList.size() == 0) {
            return;
        }

        long totalTimeStamp = System.currentTimeMillis() - mStartTimeStamp;
        for (int i = 0; i < mEmotionBeanList.size(); i++) {
            EmotionBean bean = mEmotionBeanList.get(i);
            Bitmap bitmap = bean.bitmap;
            if (bitmap.isRecycled() || bean.y > getHeight()
                    || totalTimeStamp < bean.appearTimeStamp) {
                continue;
            }

            mMatrix.reset();
            float heightScale = mEmotionHeight * 1.0f / bitmap.getHeight();
            float widthScale = mEmotionWidth * 1.0f / bitmap.getWidth();
            mMatrix.setScale(widthScale, heightScale);

            bean.x += bean.velocityX;
            bean.y += bean.velocityY;

            mMatrix.postTranslate(bean.x, bean.y);

            canvas.drawBitmap(bitmap, mMatrix, mPaint);
        }

        //重绘
        postInvalidate();
    }

    /**
     * 控件所需实体类
     */
    private class EmotionBean {

        /**
         * 需要绘制的bitmap
         */
        Bitmap bitmap;

        /**
         * 坐标点
         */
        int x, y;

        /**
         * 速率
         */
        int velocityX, velocityY;

        /**
         * 图标开始下落的时间
         */
        int appearTimeStamp;

    }
}
