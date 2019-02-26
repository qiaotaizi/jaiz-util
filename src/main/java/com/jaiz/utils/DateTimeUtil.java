package com.jaiz.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期,时间工具类
 */
public class DateTimeUtil {

    /**
     * 获取当前时间戳 单位 秒
     * @return
     */
    public static final long now(){
        return System.currentTimeMillis()/1000l;
    }

    /**
     * 获取当前时间至当天结束前的秒数
     * @return
     */
    public static final long secondsBeforeTomorrow(){
        long current=now();
        Calendar dayEnd=Calendar.getInstance();
        dayEnd.set(Calendar.HOUR_OF_DAY,23);
        dayEnd.set(Calendar.MINUTE,59);
        dayEnd.set(Calendar.SECOND,59);
        return dayEnd.getTimeInMillis()/1000l-current;
    }

    /**
     * 获取当前月份的第一天
     * @return
     */
    public static final Date firstDayOfThisMonth(){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        return calendar.getTime();
    }

    /**
     * 获取下个月的第一天
     * @return
     */
    public static final Date firstDayOfNextMonth(){
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MONTH,1);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        return calendar.getTime();
    }

    /**
     * 判断给定时间戳是否是周末
     * @param ts
     * @return
     */
    public static boolean isWeekend(long ts) {
        return isWeekend(new Date(ts));
    }

    /**
     * 判断给定时间戳是否是周末
     * @param d
     * @return
     */
    public static boolean isWeekend(Date d){
        Calendar c=Calendar.getInstance();
        c.setTime(d);
        return isWeekend(c);
    }

    /**
     * 判断给定时间戳是否是周末
     * @param c
     * @return
     */
    public static boolean isWeekend(Calendar c){
        return c.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY;
    }
}
