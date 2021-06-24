package com.yu.hu.traveling.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yu.hu.ninegridlayout.utils.PixUtils;
import com.yu.hu.traveling.R;

import org.jetbrains.annotations.NotNull;


/**
 * @author Hy
 * created on 2020/04/24 22:49
 **/
@SuppressWarnings("FieldCanBeLocal")
public class RecordView extends View implements View.OnTouchListener, View.OnClickListener, View.OnLongClickListener {

    //每隔100ms更新一下进度值
    private static final int PROGRESS_INTERVAL = 100;

    // 圆形按钮半径
    private final int mRadius;
    //最大录制时长 单位 s
    private final int maxDuration;
    /**
     * 进度条宽度
     */
    private final int progressWidth;
    /**
     * 进度条颜色
     */
    private final int progressColor;
    /**
     * 填充色
     */
    private final int fillColor;

    private final Paint fillPaint;
    private final Paint progressPaint;
    /**
     * 当前进度
     */
    private int progressValue;
    /**
     * 最大进度
     */
    private int progressMaxValue;
    /**
     * 是否正在录制
     */
    private boolean isRecording;
    private long startRecordTime;
    private Handler mHandler;
    private OnRecordListener onRecordListener;

    public RecordView(Context context) {
        this(context, null);
    }

    public RecordView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RecordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        //自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecordView, defStyleAttr, defStyleRes);
        mRadius = typedArray.getDimensionPixelOffset(R.styleable.RecordView_record_radius, 0);
        maxDuration = typedArray.getInteger(R.styleable.RecordView_record_duration, 10);
        progressWidth = typedArray.getDimensionPixelOffset(R.styleable.RecordView_record_progress_width, PixUtils.dp2px(3));
        progressColor = typedArray.getColor(R.styleable.RecordView_record_progress_color, Color.RED);
        fillColor = typedArray.getColor(R.styleable.RecordView_record_fill_color, Color.WHITE);
        typedArray.recycle();

        progressMaxValue = maxDuration * 1000 / PROGRESS_INTERVAL;

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(fillColor);
        fillPaint.setStyle(Paint.Style.FILL);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(progressColor);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(progressWidth);

        mHandler = createHandler();
        setOnTouchListener(this);
        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    private void finishRecord() {
        if (onRecordListener != null) {
            onRecordListener.finish();
        }
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (isRecording) {
            canvas.drawCircle(width / 2, height / 2, width / 2, fillPaint);

            int left = progressWidth / 2;
            int top = progressWidth / 2;
            int right = width - progressWidth / 2;
            int bottom = height - progressWidth / 2;
            float swapAngle = (progressValue * 1.0f / progressMaxValue) * 360;
            canvas.drawArc(left, top, right, bottom, -90, swapAngle, false, progressPaint);
        } else {
            canvas.drawCircle(width / 2, height / 2, mRadius, fillPaint);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isRecording = true;
            startRecordTime = System.currentTimeMillis();
            mHandler.sendEmptyMessage(0);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            long now = System.currentTimeMillis();
            if (now - startRecordTime > ViewConfiguration.getLongPressTimeout()) {
                //认为是长按录制
                finishRecord();
            }
            mHandler.removeCallbacksAndMessages(null);
            isRecording = false;
            startRecordTime = 0;
            progressValue = 0;
            postInvalidate();
        }
        return false;
    }

    @NotNull
    public Handler createHandler() {
        return new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                progressValue++;
                postInvalidate(); //重绘
                if (progressValue <= progressMaxValue) {
                    sendEmptyMessageDelayed(0, PROGRESS_INTERVAL);
                } else {
                    finishRecord();
                }
            }
        };
    }

    public void setOnRecordListener(OnRecordListener onRecordListener) {
        this.onRecordListener = onRecordListener;
    }

    @Override
    public void onClick(View v) {
        if (onRecordListener != null) {
            onRecordListener.onClick();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (onRecordListener != null) {
            onRecordListener.onLongClick();
        }
        return true;
    }

    public interface OnRecordListener {
        void onClick();

        void onLongClick();

        void finish();
    }
}
