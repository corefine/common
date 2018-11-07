package org.corefine.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 *
 * @version 2016-01-15
 */
public class DateUtil {

    private static final String[] parsePatterns = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm"};

    /** 年月日时分秒格式：yyyy-MM-dd HH:mm:ss */
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /** 年月日时分秒格式：yyyy-MM-dd HH:mm */
    public static final String DATE_T_PATTERN = "yyyy-MM-dd HH:mm";
    /** 年月日格式：yyyy-MM-dd */
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    /** 时分秒格式：HH:mm:ss */
    public static final String TIME_PATTERN = "HH:mm:ss";
    /** 时分格式：HH:mm */
    public static final String MINUTE_PATTERN = "HH:mm";
    /** 年月格式：yyyy-MM */
    public static final String MONTH_PATTERN = "yyyy-MM";
    /** 年月格式：yyyy-MM */
    public static final String CHINA_MONTH_PATTERN = "yyyy年MM月";

    /** 短年月日时分秒格式：yyyyMMddHHmmss */
    public static final String YYYYMMDDHHMMSS_PATTERN = "yyyyMMddHHmmss";
    /** 短年月日格式：yyyyMMdd */
    public static final String YYYYMMDD_PATTERN = "yyyyMMdd";
    /** 短年月日格式：yyyyMMddHH */
    public static final String YYYYMMDDHH_PATTERN = "yyyyMMddHH";
    /** 短时分秒格式：HHmmss */
    public static final String HHMMSS_PATTERN = "HHmmss";
    /** UTC格式：UTC_PATTERN */
    public static final String UTC_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    /**
     * 获取前时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String formatCurrentDate() {
        return formatCurrentDate(DATE_TIME_PATTERN);
    }

    /**
     * 获取前时间
     *
     * @return yyyy-MM-dd
     */
    public static String formatCurrentDateYMD() {
        return formatCurrentDate(DATE_PATTERN);
    }

    /**
     * 获取前时间
     *
     * @return yyyy-MM-dd
     */
    public static String formatCurrentDateHM() {
        return formatCurrentDate(DATE_T_PATTERN);
    }

    /**
     * 获取前时间
     *
     * @param pattern 格式
     * @return 字符串时间
     */
    public static String formatCurrentDate(String pattern) {
        return formatDate(new Date(), pattern);
    }

    /**
     * 格式化日期
     *
     * @param date 要格式化的日期
     * @return yyyy-MM-dd
     */
    public static String formatDate(Date date) {
        return formatDate(date, DATE_PATTERN);
    }

    /**
     * 格式化日期时间
     *
     * @param date 要格式化的日期
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, DATE_TIME_PATTERN);
    }

    /**
     * 格式化日期时间
     *
     * @param date 要格式化的日期
     * @return yyyy-MM-dd
     */
    public static String formatTime(Date date) {
        return formatDate(date, TIME_PATTERN);
    }

    /**
     * 格式化日期
     *
     * @param date    要格式化的日期
     * @param pattern 规则
     * @return 字符串格式的日期
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null)
            return null;
        DateFormat defaultFormat = new SimpleDateFormat(pattern);
        return defaultFormat.format(date);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        if (date == null)
            return null;
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = formatDate(date, pattern[0].toString());
        } else {
            formatDate = formatDate(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 解析日期时间(yyyy-MM-dd HH:mm:ss)
     */
    public static Date parseDateTime(String strDate) {
        return parseDate(strDate, DATE_TIME_PATTERN);
    }

    /**
     * 解析日期(yyyy-MM-dd)
     */
    public static Date parseDate(String strDate) {
        return parseDate(strDate, DATE_PATTERN);
    }

    /**
     * 解析UTC日期
     */
    public static Date parseUTCDate(String date) {
        if (date == null)
            return null;
        return parseDate(date.replace("Z", " UTC"), UTC_PATTERN);
    }

    /**
     * 解析日期
     *
     * @param strDate 字符串日期
     * @param pattern 规则
     */
    public static Date parseDate(String strDate, String pattern) throws RuntimeException {
        if (strDate == null)
            return null;
        DateFormat defaultFormat = new SimpleDateFormat(pattern);
        try {
            return defaultFormat.parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException("日期解析异常", e);
        }
    }

    /**
     * 获取指定日期当天的开始时间
     */
    public static Date getDayStartTime(Date d) {
        if (d == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取指定日期当天的结束时间
     */
    public static Date getDayEndTime(Date d) {
        if (d == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        return cal.getTime();
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return formatDate(new Date(), pattern);
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    /**
     * 获取过去的天数
     */
    public static long pastDays(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取下一天
     */
    public static Date getNextDate(Date date) {
        return getNextDate(date, 1);
    }

    /**
     * 获取下N天
     */
    public static Date getNextDate(Date date, int days) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * 获取上N天
     */
    public static Date getPrevDate(Date date, int days) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -days);
        return calendar.getTime();
    }

    // 获取前月的第一天
    public static String getPreviousMonthFirstDay(Date d) {

        Calendar cal = Calendar.getInstance();// 获取当前日期
        cal.setTime(d);

        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        return formatDate(cal.getTime());
    }

    // 获取前月的最后一天
    public static String getPreviousMonthLastDay(Date d) {
        Calendar cal = Calendar.getInstance();// 获取当前日期
        cal.setTime(d);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        return formatDate(cal.getTime());
    }

    /**
     * 获取榜单日期
     */
    public static Date getProfitRankDate() {
        long current = System.currentTimeMillis();
        long dayStart = DateUtil.getDayStartTime(new Date(current)).getTime();
        Calendar cal = Calendar.getInstance();
        if (current - dayStart < 21 * 60 * 60 * 1000)
            cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    /**
     * 获取榜单日期
     */
    public static Date getDoubleProfitRankDate(Long time) {
        long current;
        if (time == null) {
            current = System.currentTimeMillis();
        } else {
            current = time;
        }
        long dayStart = DateUtil.getDayStartTime(new Date(current)).getTime();
        Calendar cal = Calendar.getInstance();

        long timeDis = current - dayStart;
        if (timeDis < 13 * 60 * 60 * 1000) {
            cal.set(Calendar.HOUR_OF_DAY, 13);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
        } else if (timeDis < 21 * 60 * 60 * 1000) {
            cal.set(Calendar.HOUR_OF_DAY, 21);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
        } else {
            cal.add(Calendar.DATE, 1);
            cal.set(Calendar.HOUR_OF_DAY, 13);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
        }
        return cal.getTime();
    }

    /**
     * 获取过去的分钟
     */
    public static long pastMinutes(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 1000);
    }

    /**
     * 取当前日期的前一天
     */
    public static String getYesterdayDate() {
        return formatDate(getNextDate(new Date(), -1));
    }

    /**
     * 取当前日期的后一天
     */
    public static String getTomorrowDate() {
        return formatDate(getNextDate(new Date(), 1));
    }

    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     */
    public static List<String> getDateList(String beginDate, String endDate) {
        return getDateList(beginDate, endDate, DATE_PATTERN);
    }

    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     */
    public static List<String> getDateList(String beginDate, String endDate, String pattern) {
        List<String> dateList = new ArrayList<String>();
        Date $beginDate = parseDate(beginDate, pattern);
        Date $endDate = parseDate(endDate, pattern);

        dateList.add(beginDate);// 把开始时间加入集合

        Calendar cal = Calendar.getInstance();
        // 使用给定的开始时间， 设置此 Calendar 的时间
        cal.setTime($beginDate);
        while (true) {
            // 根据日历的规则，为给定的日历字段添加指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 此日期是否在指定日期之后
            if ($endDate.after(cal.getTime())) {
                String currDate = formatDate(cal.getTime(), pattern);
                dateList.add(currDate);
            } else {
                break;
            }
        }
        if (!beginDate.equals(endDate)) {// 开始时间不等于结束时间
            dateList.add(endDate);// 把结束时间加入集合
        }
        return dateList;
    }

    /**
     * 相隔几天
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     */
    public static int getDateInterval(String beginDate, String endDate) {
        Date start = DateUtil.parseDate(beginDate);
        Date end = DateUtil.parseDate(endDate);
        return (int) ((end.getTime() - start.getTime()) / (1000 * 3600 * 24));
    }

    /**
     * 相隔几天
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     */
    public static int getDateInterval(Date beginDate, Date endDate) {
        return (int) ((endDate.getTime() - beginDate.getTime()) / (1000 * 3600 * 24));
    }

    /**
     * 获取指定日期某个时间的结束时间
     */
    public static Date getHourEndTime(Date d) {
        if (d == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        return cal.getTime();
    }

    /**
     * 时间戳转时分秒
     *
     * @param mss
     * @return
     */
    public static String formatDuring(long mss) {
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return hours + ":" + minutes + ":" + seconds;
    }

    /**
     * 获取某月的最后一天
     */
    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    public static Date getThisMondayStartTime(Date currDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtil.getDayStartTime(currDate));
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            cal.add(Calendar.WEEK_OF_YEAR, -1);
        }
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    public static Date getThisSundayEndTime(Date currDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtil.getDayEndTime(currDate));
        if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            cal.add(Calendar.WEEK_OF_YEAR, 1);
        }
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return cal.getTime();
    }

}
