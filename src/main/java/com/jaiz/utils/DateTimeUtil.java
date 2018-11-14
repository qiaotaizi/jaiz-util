package com.jaiz.utils;

import java.util.Calendar;

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
}
