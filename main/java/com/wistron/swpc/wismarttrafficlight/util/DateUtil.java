package com.wistron.swpc.wismarttrafficlight.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;

public class DateUtil {

    @Value("${traffic.time_zone}")
    private static String time_zone;

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String HOUR_TIME_FORMAT = "HHmm";

    private static String getTimeZone() {
        if(time_zone == null) {
            return "GMT+8:00";
        } else {
            return time_zone;
        }
    }

    public static String getLocalHourTime() {
        TimeZone timeZone = TimeZone.getTimeZone(getTimeZone());
        Calendar utcCal = Calendar.getInstance(timeZone);
        DateFormat dateFormat = new SimpleDateFormat(HOUR_TIME_FORMAT);

        return dateFormat.format(utcCal.getTime());
    }

    public static String getLocalDateString() {
        TimeZone timeZone = TimeZone.getTimeZone(getTimeZone());
        Calendar utcCal = Calendar.getInstance(timeZone);
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        return dateFormat.format(utcCal.getTime());
    }

    public static String getLocalTime() {
        TimeZone timeZone = TimeZone.getTimeZone(getTimeZone());
        Calendar utcCal = Calendar.getInstance(timeZone);
        DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);

        return dateFormat.format(utcCal.getTime());
    }

    /**
     * 返回 utc 时间  yyyy-MM-ddTHH:mm:ssZ
     * @return
     */
    public static String getUtcTime() {
        Calendar utcCal = Calendar.getInstance();
        int offset = utcCal.get(Calendar.ZONE_OFFSET);
        utcCal.add(Calendar.MILLISECOND, -offset);

        DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);

        return dateFormat.format(utcCal.getTime());
    }

    /**
     * 返回当前时间 utc 毫秒数
     * @return
     */
    public static long getUtcMillis() {
        return Calendar.getInstance().getTimeInMillis();
    }


    /**
     * 验证日期格式为 yyyy-MM-dd
     * @param dateStr
     * @return
     */
    public static boolean isLegalDate(String dateStr) {

        if ((dateStr == null) || (dateStr.length() != 10)) {
            return false;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(dateStr);
            return dateStr.equals(dateFormat.format(date));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 时间验证
     * rsyslog 下载日期不超过 2 年
     * 趋势流量图不超过 2 周
     * @param dateStr
     * @param year
     * @param day
     * @return
     */
    public static boolean validateSearchDate(String dateStr, int year, int day) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String beforeStr = "";

        String currentStr = dateFormat.format(Calendar.getInstance().getTime());

        if (year != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -year);
            beforeStr = dateFormat.format(calendar.getTime());
        }

        if (day != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -day);
            beforeStr = dateFormat.format(calendar.getTime());
        }

        return dateStr.compareTo(beforeStr) >= 0
                && dateStr.compareTo(currentStr) <= 0;
    }

}
