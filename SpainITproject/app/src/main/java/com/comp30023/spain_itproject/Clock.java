package com.comp30023.spain_itproject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Service that provides access to the current time
 */
public class Clock {

    /**
     * @return The current local time as a string
     */
    public static String getCurrentLocalTimeStamp() {

        TimeZone tz = TimeZone.getDefault();
        DateFormat df = SimpleDateFormat.getDateTimeInstance();
        df.setTimeZone(tz);

        return df.format(new Date());
    }

    /**
     * @param timeZone The timezone that the timestamp is being requested in respect to
     * @return The current timestamp relative to the provided TimeZone
     */
    public static String getCurrentTimeStamp(TimeZone timeZone) {

        DateFormat df = SimpleDateFormat.getDateTimeInstance();
        df.setTimeZone(timeZone);

        return df.format(new Date());
    }

}
