package com.michen.olaxueyuan.common.manager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by LIJIAN on 2015/8/12.
 */
public class DateUtils {

    private static final SimpleDateFormat _format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 返回指定格式的日期字符串。
     *
     * @param formatString ：为null时，使用"yyyy-MM-dd";
     * @param time
     * @return
     */

    public static String getFormatDate(String formatString, Object time) {
        if (formatString != null) {
            return new SimpleDateFormat(formatString).format(time);
        } else {
            return _format.format(time);
        }
    }

    /**
     * 判断当前日期是星期几 第一种方法
     *
     * @param pTime 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */

    public static int dayForWeek(String pTime) {

        Calendar c = Calendar.getInstance();

        try {
            c.setTime(_format.parse(pTime));
            int dayForWeek;

            if (c.get(Calendar.DAY_OF_WEEK) == 1) {

                dayForWeek = 7;

            } else {

                dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;

            }

            return dayForWeek;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;


    }

    /**
     * 返回当前日期对应的周一和周日
     *
     * @param currentDate
     * @return
     */
    public static String[] getFirstAndLastOfWeek(String currentDate) {
        String[] dateArray = new String[2];
        SimpleDateFormat returnFormat = new SimpleDateFormat("MM-dd");
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        try {
            c.setTime(_format.parse(currentDate));
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            dateArray[0] = returnFormat.format(c.getTime());

            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            dateArray[1] = returnFormat.format(c.getTime());
            return dateArray;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;


    }

    /**
     * 获取当前日期之前六天加当前日期共七天的数据
     *
     * @param currentDate
     * @return
     */

    public static ArrayList<String> getStringDateBeforeOfWeek(long currentDate) {
        ArrayList<String> dateList = new ArrayList<String>();
        Calendar c = Calendar.getInstance();
        for (int i = -6; i <= 0; i++) {
            c.setTimeInMillis(currentDate);
            c.add(Calendar.DAY_OF_YEAR, i);
            dateList.add(_format.format(c.getTime()));
        }
        return dateList;
    }

    /**
     * 获取当前月的第一天和最后一天。
     *
     * @param currentDate
     * @return
     */

    public static String[] getFirstAndLastOfMonth(String currentDate) {
        String[] dateArray = new String[2];
        SimpleDateFormat returnFormat = new SimpleDateFormat("MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(_format.parse(currentDate));
            c.set(Calendar.DATE, 1);
            dateArray[0] = returnFormat.format(c.getTime());

            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DATE, -1);
            dateArray[1] = returnFormat.format(c.getTime());
            return dateArray;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 获取当前日期对应一月的字符串日期列表
     *
     * @param currentDate
     * @return
     */

    public static ArrayList<String> getStringDateOfMonth(String currentDate) {
        ArrayList<String> dateList = new ArrayList<String>();
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(_format.parse(currentDate));
            for (int i = 1; i <= c.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                c.set(Calendar.DATE, i);
                dateList.add(_format.format(c.getTime()));
            }
            return dateList;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取给定日期前30天加上当前日期共31天的字符串日期列表
     *
     * @param currentDate
     * @return
     */

    public static ArrayList<String> getStringDateOfMonthBeforeDate(long currentDate) {
        ArrayList<String> dateList = new ArrayList<String>();
//        dateList.add(_format.format(currentDate));
        Calendar c = Calendar.getInstance();
        for (int i = -30; i <= 0; i++) {
            c.setTimeInMillis(currentDate);
            c.add(Calendar.DAY_OF_YEAR, i);
            dateList.add(_format.format(c.getTime()));
        }
        return dateList;
    }


}
