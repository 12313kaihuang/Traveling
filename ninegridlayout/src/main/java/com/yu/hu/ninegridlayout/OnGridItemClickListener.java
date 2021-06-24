package com.yu.hu.ninegridlayout;

import com.yu.hu.ninegridlayout.entity.GridItem;

import java.util.List;

/**
 * @author Hy
 * created on 2020/04/19 11:12
 **/
public interface OnGridItemClickListener {
    void onItemClicked(List<GridItem> items, int position);
}
