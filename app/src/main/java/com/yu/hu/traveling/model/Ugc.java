package com.yu.hu.traveling.model;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.blankj.utilcode.util.GsonUtils;
import com.yu.hu.traveling.BR;

import java.util.Objects;

/**
 * @author Hy
 * created on 2020/04/16 19:13
 * <p>
 * 对应每个帖子下面的四个按钮状态
 **/
@SuppressWarnings("unused")
public class Ugc extends BaseObservable {


    /**
     * likeCount : 1305
     * shareCount : 127
     * commentCount : 618
     * hasFavorite : false
     * hasLiked : false
     * hasDissed : false
     */

    public int likeCount;
    public int shareCount;
    public int commentCount;
    public int browseCount; //浏览量
    public boolean hasFavorite;
    public boolean hasLiked;
    public boolean hasDissed;

    @Bindable
    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public boolean isHasdiss() {
        return hasDissed;
    }

    @Bindable
    public boolean isHasLiked() {
        return hasLiked;
    }

    public void toggleLiked(boolean hasLiked) {
        if (this.hasLiked == hasLiked) {
            return;
        }
        this.hasLiked = !this.hasLiked;
        if (this.hasLiked) {
            if (hasDissed) hasDissed = false;
            likeCount++;
        } else {
            likeCount--;
        }
        notifyPropertyChanged(BR._all);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ugc ugc = (Ugc) o;
        return likeCount == ugc.likeCount &&
                shareCount == ugc.shareCount &&
                commentCount == ugc.commentCount &&
                browseCount == ugc.browseCount &&
                hasFavorite == ugc.hasFavorite &&
                hasLiked == ugc.hasLiked &&
                hasDissed == ugc.hasDissed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(likeCount, shareCount, commentCount, browseCount, hasFavorite, hasLiked, hasDissed);
    }

    @NonNull
    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
