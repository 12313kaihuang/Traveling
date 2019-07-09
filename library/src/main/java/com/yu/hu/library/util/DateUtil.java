package com.yu.hu.library.util;

import android.annotation.SuppressLint;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 文件名：DateUtil
 * 创建者：HY
 * 创建时间：2019/1/22 16:04
 * 描述：  DateUtil
 * 判断是否是本年等方法考虑的情况没有考虑全，有待完善
 */

@SuppressWarnings("unused")
public class DateUtil {

    /**
     * 一秒
     */
    private static final long SECOND_IN_MILLIS = 1000;

    /**
     * 一分钟
     */
    private static final long MINUTE_IN_MILLIS = 60 * SECOND_IN_MILLIS;

    /**
     * 一小时
     */
    private static final long HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS;

    /**
     * 一天
     */
    private static final long DAY_IN_MILLIS = 24 * HOUR_IN_MILLIS;

    /**
     * WEEKDAYS
     */
    private static final String[] WEEKDAYS = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    /**
     * Date 转 String
     *
     * @param date date
     * @return 转换成String
     */
    public static String toString(Date date) {
        return sdf.format(date);
    }

    /**
     * String 转 Date
     *
     * @param date yyyy-MM-dd HH:mm:ss格式字符串
     * @return 转换成Date
     */
    @SuppressWarnings("WeakerAccess")
    public static Date toDate(String date) {
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断文章发布距离现在多长时间了 yyyy-MM-dd HH:mm:ss格式
     * 假设比较的是一年以内的日期。
     *
     * @param createTime createTime
     * @return 距离现在多长时间
     */
    @SuppressLint("SimpleDateFormat")
    public static String fromNow(String createTime) {
        Date cTime = toDate(createTime);
        if (cTime == null) {
            return "cTime == null";
        }
        if (isThisYear(cTime)) {
            if (DateUtils.isToday(cTime.getTime())) {
                return new SimpleDateFormat("HH:mm").format(cTime);
            } else if (isYesterday(cTime)) {
                return new SimpleDateFormat("昨天 HH:mm").format(cTime);
            } else if (isBeforeYesterday(cTime)) {
                return "前天 " + new SimpleDateFormat("HH:mm").format(cTime);
            } else {
                return new SimpleDateFormat("MM-dd HH:mm").format(cTime);
            }
        } else {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(cTime);
        }
    }

    /**
     * 判断选择的日期是否在本周内
     *
     * @param time time
     * @return 是否是本周
     */
    public static boolean isThisWeek(Date time) {
        Calendar calendar = Calendar.getInstance();
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(time);
        int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        return paramWeek == currentWeek;
    }

    /**
     * 判断选择的日期是否在本年内
     *
     * @param date date
     * @return 是否是本年
     */
    @SuppressWarnings("WeakerAccess")
    public static boolean isThisYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        int now = calendar.get(Calendar.YEAR);
        calendar.setTime(date);
        return now == calendar.get(Calendar.YEAR);
    }

    /**
     * 是否是昨天
     *
     * @param date date
     * @return 是否是昨天
     */
    @SuppressWarnings("WeakerAccess")
    public static boolean isYesterday(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (isThisYear(date)) {
            int day = calendar.get(Calendar.DAY_OF_YEAR);
            calendar.setTime(date);
            return day - calendar.get(Calendar.DAY_OF_YEAR) == 1;
        } else {
            //不在一年的情况暂未考虑
            return false;
        }
    }

    /**
     * 是否是前天
     *
     * @param date date
     * @return 是否是前天
     */
    @SuppressWarnings("WeakerAccess")
    public static boolean isBeforeYesterday(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (isThisYear(date)) {
            int day = calendar.get(Calendar.DAY_OF_YEAR);
            calendar.setTime(date);
            System.out.println("day=" + day + ",ctime=" + calendar.get(Calendar.DAY_OF_YEAR));
            return day - calendar.get(Calendar.DAY_OF_YEAR) == 2;
        } else {
            //不在一年的情况暂未考虑
            return false;
        }
    }

    /**
     * 获取当前年份
     */
    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 获取当前年份
     */
    public static int getYear(Date date) {
        return Integer.parseInt(toString(date).split("-")[0]);
    }

    /**
     * 获取当前是几月
     */
    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前是几号
     */
    public static int getDay() {
        return Calendar.getInstance().get(Calendar.DATE);
    }

    /**
     * 获取这个日期是星期几
     *
     * @param date date
     * @return 星期几
     */
    public static String getWeekDay(Date date) {
        Calendar calendar = Calendar.getInstance(); // 获得一个日历
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (day < 0) {
            day = 0;
        }
        return WEEKDAYS[day];
    }
}
