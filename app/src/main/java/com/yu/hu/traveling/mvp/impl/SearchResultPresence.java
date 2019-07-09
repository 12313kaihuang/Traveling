package com.yu.hu.traveling.mvp.impl;

import com.yu.hu.library.mvp.Presence;
import com.yu.hu.traveling.entity.note.Note;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.mvp.impl
 * 文件名：SearchResultPresence
 * 创建者：HY
 * 创建时间：2019/7/2 14:52
 * 描述：  游记相关Presence
 *
 * @see com.yu.hu.traveling.activity.SearchResultActivity
 */
public interface SearchResultPresence extends Presence {

    /**
     * @param noteList 查询结果
     */
    void onSearchResult(@NonNull List<Note> noteList);

    /**
     * @param noteList 查询更多结果
     */
    void onSearchMoreResult(@NonNull List<Note> noteList);
}
