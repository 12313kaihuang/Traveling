package com.yu.hu.traveling.yonglian.custom;

import android.util.SparseArray;

import com.yuntongxun.plugin.im.ui.chatting.model.BaseChattingRow;

/**
 * Created with Android Studio IDEA.
 * </p>
 * Tool class for custom chat line layout
 *
 * @author WJ
 * @version 1.0
 * @since 2017/8/21 14:16
 */

public class CustomRowUtil {

    public static final int KEY_100=100;
    public static final int KEY_101=101;
    private static SparseArray<BaseChattingRow> mItemRowMap = new SparseArray<>();

    static {
        mItemRowMap.put(KEY_100, new CustomTxRow(KEY_100));
        mItemRowMap.put(KEY_101, new CustomRxRow(KEY_101));
    }

    public static BaseChattingRow getRow(int key) {
        return mItemRowMap.get(key);
    }

}
