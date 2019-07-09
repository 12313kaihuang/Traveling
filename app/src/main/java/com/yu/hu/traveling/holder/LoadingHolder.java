package com.yu.hu.traveling.holder;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yu.hu.traveling.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.holder
 * 文件名：LoadingHolder
 * 创建者：HY
 * 创建时间：2019/7/8 18:36
 * 描述：  加载中
 * <p>
 * {@code R.layout.item_loading}
 */
@SuppressWarnings("WeakerAccess")
public class LoadingHolder extends RecyclerView.ViewHolder {

    public ProgressBar progressBar;  //progressBar

    public TextView hint;  //提示文字

    public LoadingHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progress_bar);
        hint = itemView.findViewById(R.id.tv_hint);
    }
}
