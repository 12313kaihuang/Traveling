package com.android.traveling.widget.gridview;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.android.traveling.R;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.widget
 * 文件名：AddPictureView
 * 创建者：HY
 * 创建时间：2019/3/17 21:04
 * 描述：  添加图片
 */

public class AddPictureView extends ConstraintLayout {


    public AddPictureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_camera, this);
    }

}
