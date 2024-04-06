package com.hit.playpal.utils;
import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.NonNull;

public class DateUtils {
    public static final long MINUTE_IN_MILLIS = 60 * 1000;
    public static final long HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS;
    public static final long DAY_IN_MILLIS = 24 * HOUR_IN_MILLIS;

    private DateUtils() {} // static class

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

    @NonNull
    private static String turnMillisToMinutes(long millis) {
        return String.valueOf(Math.round((float) millis / MINUTE_IN_MILLIS)) + "min";
    }

    @NonNull
    private static String turnMillisToHours(long millis) {
        return String.valueOf(Math.round((float) millis / HOUR_IN_MILLIS)) + "h";
    }

    @NonNull
    private static String turnMillisToDays(long millis) {
        return String.valueOf(Math.round((float) millis / DAY_IN_MILLIS)) + "d";
    }

    private static String turnMillisToTimeString(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        return sdf.format(millis);
    }

    private static String turnMillisToDateString(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US); // TODO: check if this is the correct format
        return sdf.format(millis);
    }
}
