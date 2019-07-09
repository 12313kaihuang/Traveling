package com.yu.hu.traveling.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.yu.hu.traveling.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 项目名：CoordinatorLayoutDemo
 * 包名：  com.yu.hu.coordinatorlayoutdemo
 * 文件名：RecyclerAdapter
 * 创建者：HY
 * 创建时间：2019/6/27 16:10
 * 描述：  TODO
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<String> itemList;

    private List<String> colorList;

    private Random random;

    public RecyclerAdapter() {
        itemList = new ArrayList<>();
        colorList = new ArrayList<>();

        random = new Random();
        int itemNum = random.nextInt(10) + 15;
        for (int i = 0; i < itemNum; i++) {
            itemList.add("position = " + i);
            colorList.add(getRandomColor());
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.title.setText(itemList.get(i));
        Log.d("Apapter", "onBindViewHolder:  i=" + i + ",color=" + colorList.get(i));
        viewHolder.title.setBackgroundColor(Color.parseColor(colorList.get(i)));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /**
     * 获取十六进制的颜色代码.例如  "#5A6677"
     * 分别取R、G、B的随机值，然后加起来即可
     *
     * @return String
     */
    public String getRandomColor() {
        String r, g, b;
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;

        Log.d("getRandomColor", "R = " + r + ",g=" + g + ",b=" + b);
        return "#" + r + g + b;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }
}
