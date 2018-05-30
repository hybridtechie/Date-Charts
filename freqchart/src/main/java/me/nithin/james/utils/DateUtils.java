package me.nithin.james.utils;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;

public abstract class DateUtils {

    /**
     * Time of the day when the new day starts.
     */
    public static final int NEW_DAY_OFFSET = 3;
    /**
     * Number of milliseconds in one day.
     */
    public static final long DAY_LENGTH = 24 * 60 * 60 * 1000;
    /**
     * Number of milliseconds in one hour.
     */
    public static final long HOUR_LENGTH = 60 * 60 * 1000;
    private static Long fixedLocalTime = null;
    private static TimeZone fixedTimeZone = null;
    private static Locale fixedLocale = null;

    private static Locale getLocale() {
        if (fixedLocale != null) return fixedLocale;
        return Locale.getDefault();
    }

    @NonNull
    public static Timestamp getToday() {
        return new Timestamp(getStartOfToday());
    }

    /**
     * @return array with weekday names starting according to locale settings,
     * e.g. [Mo,Di,Mi,Do,Fr,Sa,So] in Germany
     */
    public static String[] getLocaleDayNames(int format) {
        String[] days = new String[7];

        Calendar calendar = new GregorianCalendar();
        calendar.set(DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        for (int i = 0; i < days.length; i++) {
            days[i] = calendar.getDisplayName(DAY_OF_WEEK, format,
                    getLocale());
            calendar.add(DAY_OF_MONTH, 1);
        }

        return days;
    }

    /**
     * @return array with week days numbers starting according to locale
     * settings, e.g. [2,3,4,5,6,7,1] in Europe
     */
    public static Integer[] getLocaleWeekdayList() {
        Integer[] dayNumbers = new Integer[7];
        Calendar calendar = new GregorianCalendar();
        calendar.set(DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        for (int i = 0; i < dayNumbers.length; i++) {
            dayNumbers[i] = calendar.get(DAY_OF_WEEK);
            calendar.add(DAY_OF_MONTH, 1);
        }
        return dayNumbers;
    }

    private static String[] getDayNames(int format) {
        String[] wdays = new String[7];

        Calendar day = new GregorianCalendar();
        day.set(DAY_OF_WEEK, Calendar.SATURDAY);

        for (int i = 0; i < wdays.length; i++) {
            wdays[i] =
                    day.getDisplayName(DAY_OF_WEEK, format, getLocale());
            day.add(DAY_OF_MONTH, 1);
        }

        return wdays;
    }

    public static long getStartOfToday() {
        return getStartOfDay(getLocalTime() - NEW_DAY_OFFSET * HOUR_LENGTH);
    }

    public static long getStartOfDay(long timestamp) {
        return (timestamp / DAY_LENGTH) * DAY_LENGTH;
    }

    public static long getLocalTime() {
        if (fixedLocalTime != null) return fixedLocalTime;

        TimeZone tz = getTimezone();
        long now = new Date().getTime();
        return now + tz.getOffset(now);
    }

    private static TimeZone getTimezone() {
        if (fixedTimeZone != null) return fixedTimeZone;
        return TimeZone.getDefault();
    }

    public static GregorianCalendar getStartOfTodayCalendar() {
        return getCalendar(getStartOfToday());
    }

    private static GregorianCalendar getCalendar(long timestamp) {
        GregorianCalendar day =
                new GregorianCalendar(TimeZone.getTimeZone("GMT"), getLocale());
        day.setTimeInMillis(timestamp);
        return day;
    }

    public enum TruncateField {
        MONTH, WEEK_NUMBER, YEAR, QUARTER
    }
}
