package com.yu.hu.traveling.utils;

import androidx.room.TypeConverter;

import com.blankj.utilcode.util.GsonUtils;
import com.yu.hu.traveling.model.Comment;
import com.yu.hu.traveling.model.Ugc;
import com.yu.hu.traveling.model.User;

/**
 * @author Hy
 * created on 2020/04/20 14:54
 * <p>
 * 用于{@link com.yu.hu.traveling.model.Note}存储时内部实体类的转换
 **/
@SuppressWarnings("unused")
public class EntityConverter {

    @TypeConverter
    public static String User2String(User user) {
        return GsonUtils.toJson(user);
    }

    @TypeConverter
    public static User string2User(String s) {
        return GsonUtils.fromJson(s, User.class);
    }

    @TypeConverter
    public static String Comment2String(Comment comment) {
        return GsonUtils.toJson(comment);
    }

    @TypeConverter
    public static Comment string2Comment(String s) {
        return GsonUtils.fromJson(s, Comment.class);
    }

    @TypeConverter
    public static String Ugc2String(Ugc ugc) {
        return GsonUtils.toJson(ugc);
    }

    @TypeConverter
    public static Ugc string2Ugc(String s) {
        return GsonUtils.fromJson(s, Ugc.class);
    }
}
