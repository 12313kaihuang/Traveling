package com.android.traveling.widget.gridview;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.widget.gridview
 * 文件名：MyGridView
 * 创建者：HY
 * 创建时间：2019/3/18 10:25
 * 描述：  用于发表游记时的选择图片
 */

public class MyGridView extends ViewGroup {

    private int maxImageSize = 9;                   // 最大显示的图片数
    private int gridSpacing = 3;                    // 宫格间距，单位dp
    private AddPictureView pictureView;

    private int columnCount;    // 列数
    private int rowCount;       // 行数
    private int gridWidth;      // 宫格宽度
    private int gridHeight;     // 宫格高度
    private ImageLoader mImageLoader;        //全局的图片加载器(必须设置,否者不显示图片)


    private List<ImageView> imageViews;
    private List<Uri> imgUriList;

    public MyGridView(Context context) {
        this(context, null);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        pictureView = new AddPictureView(context, null);
        addView(pictureView);
        imageViews = new ArrayList<>();
        mImageLoader = new ImageLoader() {
            @Override
            public void onDisplayImage(Context context, ImageView imageView, Uri uri) {
                try {
                    imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(context.getContentResolver(),uri));
                } catch (IOException e) {
                    e.printStackTrace();
//                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_default_image,null));
                }
            }

            @Override
            public Bitmap getCacheImage(String url) {
                return null;
            }
        };
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int totalWidth = width - getPaddingLeft() - getPaddingRight();
        if (imgUriList != null && imgUriList.size() > 0) {
            //无论是几张图片，宽高都按总宽度的 1/3
            gridWidth = gridHeight = (totalWidth - gridSpacing * 2) / 3;
        }
        width = gridWidth * columnCount + gridSpacing * (columnCount - 1) + getPaddingLeft() + getPaddingRight();
        int height = gridHeight * rowCount + gridSpacing * (rowCount - 1) + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (imgUriList == null) return;
        int childrenCount = imgUriList.size();
        for (int i = 0; i < childrenCount; i++) {
            ImageView childrenView = (ImageView) getChildAt(i);
            if (mImageLoader != null) {
                mImageLoader.onDisplayImage(getContext(), childrenView, imgUriList.get(i));
            }
            int rowNum = i / columnCount;
            int columnNum = i % columnCount;
            int left = (gridWidth + gridSpacing) * columnNum + getPaddingLeft();
            int top = (gridHeight + gridSpacing) * rowNum + getPaddingTop();
            int right = left + gridWidth;
            int bottom = top + gridHeight;
            childrenView.layout(left, top, right, bottom);
        }
    }


    public interface ImageLoader {
        /**
         * 需要子类实现该方法，以确定如何加载和显示图片
         *
         * @param context   上下文
         * @param imageView 需要展示图片的ImageView
         * @param uri       图片地址
         */
        void onDisplayImage(Context context, ImageView imageView, Uri uri);

        /**
         * @param url 图片的地址
         * @return 当前框架的本地缓存图片
         */
        Bitmap getCacheImage(String url);
    }
}
