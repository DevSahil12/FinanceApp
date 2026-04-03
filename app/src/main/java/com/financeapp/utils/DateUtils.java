package com.financeapp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String formatDate(long epochMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(new Date(epochMillis));
    }

    public static String formatDateShort(long epochMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM", Locale.getDefault());
        return sdf.format(new Date(epochMillis));
    }

    public static String formatMonthYear(long epochMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        return sdf.format(new Date(epochMillis));
    }

    public static String formatMonthKey(long epochMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        return sdf.format(new Date(epochMillis));
    }

    public static String formatMonthLabel(long epochMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM", Locale.getDefault());
        return sdf.format(new Date(epochMillis));
    }

    public static long getMonthStart() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    public static long getMonthEnd() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTimeInMillis();
    }

    public static long getWeekStart() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    public static long getLastWeekStart() {
        return getWeekStart() - 7 * 24 * 60 * 60 * 1000L;
    }

    public static long getLastWeekEnd() {
        return getWeekStart() - 1;
    }

    public static String getCurrentMonthKey() {
        return formatMonthKey(System.currentTimeMillis());
    }

    public static boolean isToday(long epochMillis) {
        Calendar now = Calendar.getInstance();
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(epochMillis);
        return now.get(Calendar.YEAR) == date.get(Calendar.YEAR)
            && now.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isYesterday(long epochMillis) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(epochMillis);
        return yesterday.get(Calendar.YEAR) == date.get(Calendar.YEAR)
            && yesterday.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR);
    }

    public static String getRelativeDate(long epochMillis) {
        if (isToday(epochMillis)) return "Today";
        if (isYesterday(epochMillis)) return "Yesterday";
        return formatDate(epochMillis);
    }
}
