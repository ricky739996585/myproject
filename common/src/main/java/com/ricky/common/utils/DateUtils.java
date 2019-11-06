package com.ricky.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

/**
 * 日期处理
 *
 * @author xieyuqing
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /** 时间格式(yyyyMMdd) */
    public final static String DATE_FORMAT_MOUNTH_DAY = "yyyyMMdd";

    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String formatPattern(Date date) {
        return format(date, DATE_TIME_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    public static Date parse(String date) {
        return parse(date, DATE_TIME_PATTERN);
    }

    /**
     * 将日期字符串转换为日期类型
     *
     * @param date    日期字符串
     * @param pattern 转换模式
     * @return 返回日期类型时间
     */
    public static Date parse(String date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            System.out.println("时间转换异常！");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd）
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return 日期字符串
     */
    private static String formatDate(Date date, Object... pattern) {
        if (date == null) {
            return null;
        }
        String formatDate;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }


    /**
     * 获取某天的开始时间
     *
     * @param dateTime 字符串日期
     * @return 某天的开始时间
     */
    public static String getBeginTime(String dateTime) {
        if (StringUtils.isNotEmpty(dateTime)) {
            Date date = parse(dateTime, DATE_PATTERN);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf("00"));
            calendar.set(Calendar.MINUTE, Integer.valueOf("00"));
            calendar.set(Calendar.SECOND, Integer.valueOf("00"));
            return formatPattern(calendar.getTime());
        }
        return null;
    }

    /**
     * 获取某天的结束时间
     *
     * @param dateTime 字符串日期
     * @return 某天的结束时间
     */
    public static String getEndTime(String dateTime) {
        if (StringUtils.isNotEmpty(dateTime)) {
            Date date = parse(dateTime, DATE_PATTERN);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            return formatPattern(calendar.getTime());
        }
        return null;
    }


    /**
     * 日期往后增加天数
     *
     * @param date    日期
     * @param addDays 天数
     * @return 增多天数后的日期
     */
    public static Date addDay(Date date, int addDays) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, addDays);
        return calendar.getTime();
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param date1 开始日期 "yyyy-MM-dd"
     * @param date2 结束日期 "yyyy-MM-dd"
     * @return 获取两个日期之间的天数
     */
    public static int countDays(String date1, String date2) {
        Date d1 = parse(date1, DATE_PATTERN);
        Date d2 = parse(date2, DATE_PATTERN);
        int days = (int) ((d2.getTime() - d1.getTime()) / (1000 * 3600 * 24));
        return days + 1;
    }

    /**
     * 获取两个日期之间的所有日期（yyyy-MM-dd）
     *
     * @param begin 最小日期
     * @param end   最大日期
     * @return 返回结果
     * @author xyq
     * @date 2018年10月23日14:26:36
     */
    public static List<String> getBetweenDates(String begin, String end) {
        Date beginDate = parse(begin, DATE_PATTERN);
        Date endDate = parse(end, DATE_PATTERN);
        List<String> result = new ArrayList<>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(beginDate);

        while (beginDate.getTime() <= endDate.getTime()) {
            result.add(format(tempStart.getTime()));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
            beginDate = tempStart.getTime();
        }
        return result;
    }


    /**
     * 将秒数转换为日时分秒，
     */
    public static String secondToTime(long second) {
        //转换小时
        long hours = second / 3600;
        //剩余秒数
        second = second % 3600;
        //转换分钟
        long minutes = second / 60;
        //剩余秒数
        second = second % 60;
        return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", second);
    }

    /**
     * 将时间转换为秒
     *
     * @param time "48:13:35"
     * @return 返回秒数
     */
    public static int timeToSecond(String time) {
        if (StringUtils.isNotEmpty(time)) {
            String[] split = time.split(":");
            Integer hour = Integer.valueOf(split[0]);
            Integer minute = Integer.valueOf(split[1]);
            Integer second = Integer.valueOf(split[2]);
            return hour * 3600 + minute * 60 + second;
        }
        return 0;
    }

    /**
     * 时间戳转时间，10位，精确只到秒
     */
    public static Date timestampToDate(long timestamp) {
        return Date.from(timestampToLocalDateTime(timestamp).atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 时间戳转时间，10位，精确只到秒
     */
    private static LocalDateTime timestampToLocalDateTime(long timestamp) {
        return LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.ofHours(8));
    }

    /***
     *  功能描述：日期转换cron表达式
     * @param date
     * @param dateFormat : e.g:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatDateByPattern(Date date,String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }
    /***
     * convert Date to cron ,eg.  "0 07 10 15 1 ? 2016"
     * @param date  : 时间点
     * @return
     */
    public static String getCron(Date  date){
        String dateFormat="ss mm HH dd MM ? yyyy";
        return formatDateByPattern(date, dateFormat);
    }
    /**
     * cron表达式转为日期
     * @param cron
     * @return
     */
    public static Date getCronToDate(String cron) {
         String dateFormat="ss mm HH dd MM ? yyyy";
         SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
         Date date = null;
         try {
             date = sdf.parse(cron);
         } catch (ParseException e) {
             return null;
         }
         return date;
     }

    /**
     * 获取当前时间的minute分钟后的时间
     * @param minute
     * @return
     */
     public static Date getAfterMinuteTime(Integer minute){
         long currentTime = System.currentTimeMillis() + minute * 60 * 1000;
         return new Date(currentTime);
     }

    /**
     * 获取当前时间到下一天的毫秒数
     * @return
     */
     public static long getNextDayMillis(){
         Calendar curDate = Calendar.getInstance();
         Calendar nextDayDate = new GregorianCalendar(curDate.get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate.get(Calendar.DATE)+1, 0, 0, 0);
         return nextDayDate.getTimeInMillis() - curDate.getTimeInMillis();
     }

    /**
     * 时长转毫秒  时间格式:HH:mm:ss
     * @param duration	HH:mm:ss
     * @return	毫秒数
     */
    public static Long videDuration(String duration){
        long second = 0;
        if(StringUtils.isNotBlank(duration)){
            try {
                String[] my =duration.split(":");
                int hour =Integer.parseInt(my[0]);
                int min =Integer.parseInt(my[1]);
                int sec =Integer.parseInt(my[2]);
                second =(hour*3600+min*60+sec)*1000;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return second;
    }



}
