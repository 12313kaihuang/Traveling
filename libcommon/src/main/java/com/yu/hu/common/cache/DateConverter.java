package com.yu.hu.common.cache;


import androidx.room.TypeConverter;

import com.blankj.utilcode.util.TimeUtils;

import java.util.Date;

/**
 * @author Hy
 * created on 2020/04/15 17:27
 **/
@SuppressWarnings("unused")
public class DateConverter {

    @TypeConverter
    public static String date2String(Date date) {
        return TimeUtils.date2String(date);
    }

    @TypeConverter
    public static Date string2Date(String data) {
        return TimeUtils.string2Date(data);
    }
}
