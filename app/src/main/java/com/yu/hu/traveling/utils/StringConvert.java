package com.yu.hu.traveling.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.blankj.utilcode.util.Utils;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.ninegridlayout.entity.GridItem;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.model.Note;

import java.util.List;

@SuppressWarnings("unused")
public class StringConvert {

    public static String convertUgc(int count) {
        if (count < 10000) {
            return String.valueOf(count);
        }

        return count / 10000 + "万";
    }

    public static String convertBrowseCount(int count) {
        if (count < 10_000) {
            return count + "浏览";
        }
        return count / 10_000 + "万浏览";
    }

    //个人首页帖子数什么的
    public static CharSequence convertSpannable(int count, String intro) {
        String countStr = String.valueOf(count);
        SpannableString ss = new SpannableString(countStr + intro);
        ss.setSpan(new ForegroundColorSpan(Color.BLACK), 0, countStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(16, true), 0, countStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, countStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public static String convertLikeAndCommentNum(int likeCount, int commentCount) {
        return Utils.getApp().getResources()
                .getString(R.string.like_and_comment_num, likeCount, commentCount);
    }

    public static String convertNoteFirstCover(Note note) {
        List<GridItem> imgItems = note.getImgItems();
        if (imgItems == null || imgItems.size() == 0 || imgItems.get(0) == null) {
            return null;
        }
        return imgItems.get(0).cover;
    }

    public static String convertNoteFirstUrl(Note note) {
        List<GridItem> imgItems = note.getImgItems();
        if (imgItems == null || imgItems.size() == 0 || imgItems.get(0) == null) {
            return null;
        }
        return imgItems.get(0).url;
    }

    /**
     * 高亮关键字
     *
     * @param target 需要匹配的字符串
     * @return SpannableString
     */
    public static SpannableString convertHighlightString(String searchContent, String target) {
        LogUtil.d("convertHighlightString - content = " + searchContent + ",target = " + target);
        if (TextUtils.isEmpty(target) || !target.contains(searchContent)) {
            return new SpannableString(target);
        }

        int start = target.indexOf(searchContent);
        SpannableString spannableString = new SpannableString(target);
        if (start < 0) {
            return spannableString;
        }
        int end = start + searchContent.length();
        //0xFFFFBE00
        spannableString.setSpan(new ForegroundColorSpan(0XFFFF678F), start, end, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
