package com.yu.hu.traveling.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Hy
 * created on 2020/04/19 21:41
 * <p>
 * 对应一条搜索记录
 **/
@Entity
public class SearchHistory {

    @NonNull
    @PrimaryKey
    public String searchContent = "";

    public long searchTime;

    @SuppressWarnings("unused")
    public SearchHistory() {
    }

    @Ignore
    public SearchHistory(@NotNull String searchContent) {
        this.searchContent = searchContent;
        this.searchTime = System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchHistory that = (SearchHistory) o;
        return searchTime == that.searchTime &&
                searchContent.equals(that.searchContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchContent, searchTime);
    }
}
