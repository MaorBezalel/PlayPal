package com.hit.playpal.utils;
import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.NonNull;

/**
 * Static utility class for date and time related operations.
 * This class provides methods to convert time in milliseconds to various formats.
 */
public class DateUtils {
    /**
     * Constant representing a minute in milliseconds.
     */
    public static final long MINUTE_IN_MILLIS = 60 * 1000;

    /**
     * Constant representing an hour in milliseconds.
     */
    public static final long HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS;

    /**
     * Constant representing a day in milliseconds.
     */
    public static final long DAY_IN_MILLIS = 24 * HOUR_IN_MILLIS;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private DateUtils() {} // static class

    /**
     * Converts a timestamp to a relative time display string.
     *
     * @param timestamp The timestamp to convert.
     * @return A string representing the relative time.
     */
    public static String getRelativeTimeDisplay(long timestamp) {
        long now = System.currentTimeMillis();
        long difference = now - timestamp;

        if (difference < MINUTE_IN_MILLIS) {
            return "Just now";
        } else if (difference < HOUR_IN_MILLIS) {
            return turnMillisToMinutes(difference);
        } else if (difference < 2 * HOUR_IN_MILLIS) {
            return "1h";
        } else if (difference < DAY_IN_MILLIS) {
            return turnMillisToTimeString(timestamp);
        } else if (difference < 2 * DAY_IN_MILLIS) {
            return "Yesterday";
        } else {
            return turnMillisToDateString(timestamp);
        }

    }

    /**
     * Converts milliseconds to a string representing the number of minutes.
     *
     * @param millis The time in milliseconds.
     * @return A string representing the time in minutes.
     */
    @NonNull
    private static String turnMillisToMinutes(long millis) {
        return String.valueOf(Math.round((float) millis / MINUTE_IN_MILLIS)) + "min";
    }

    /**
     * Converts milliseconds to a string representing the number of hours.
     *
     * @param millis The time in milliseconds.
     * @return A string representing the time in hours.
     */
    @NonNull
    private static String turnMillisToHours(long millis) {
        return String.valueOf(Math.round((float) millis / HOUR_IN_MILLIS)) + "h";
    }

    /**
     * Converts milliseconds to a string representing the number of days.
     *
     * @param millis The time in milliseconds.
     * @return A string representing the time in days.
     */
    @NonNull
    private static String turnMillisToDays(long millis) {
        return String.valueOf(Math.round((float) millis / DAY_IN_MILLIS)) + "d";
    }

    /**
     * Converts a timestamp to a time string in the format "HH:mm".
     *
     * @param millis The timestamp to convert.
     * @return A string representing the time.
     */
    private static String turnMillisToTimeString(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        return sdf.format(millis);
    }

    /**
     * Converts a timestamp to a date string in the format "MM/dd/yyyy".
     *
     * @param millis The timestamp to convert.
     * @return A string representing the date.
     */
    private static String turnMillisToDateString(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US); // TODO: check if this is the correct format
        return sdf.format(millis);
    }
}
