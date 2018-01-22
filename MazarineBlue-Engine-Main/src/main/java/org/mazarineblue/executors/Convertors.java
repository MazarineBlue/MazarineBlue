/*
 * Copyright (c) 2018 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.mazarineblue.executors;

import static java.lang.Long.parseLong;
import static java.lang.String.format;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.mazarineblue.executors.exceptions.DurationAmountNotFoundException;
import org.mazarineblue.executors.exceptions.TimeUnitNotFoundException;

public class Convertors {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
            
    public static String toString(Date input) {
        return new SimpleDateFormat(DATE_TIME_FORMAT).format(input);
    }

    public static Date toDate(String input) {
        input = input.trim();
        return input.equals("now") ? new Date()
                : input.startsWith("+") ? parseStringForFutureDate(input.substring(1))
                : input.startsWith("-") ? parseStringForPastDate(input.substring(1))
                : parseStringForAbsoluteDate(input);
    }

    private static Date parseStringForFutureDate(String input) {
        long now = System.currentTimeMillis();
        long offset = toDuration(input).toMillis();
        return new Date(now + offset);
    }

    private static Date parseStringForPastDate(String input) {
        long now = System.currentTimeMillis();
        long offset = toDuration(input).toMillis();
        return new Date(now - offset);
    }
    private static Date parseStringForAbsoluteDate(String input) {
        Date d = parseStringUsingFormat(new SimpleDateFormat(DATE_TIME_FORMAT), input);
        if (d == null)
            d = parseStringUsingFormat(new SimpleDateFormat(DATE_FORMAT), input);
        if (d == null)
            d = parseStringForTime(input);
        if (d != null)
            return d;
        throw new IllegalArgumentException("Not supported yet.");
    }

    private static Date parseStringUsingFormat(DateFormat format, String input) {
        try {
            return format.parse(input);
        } catch (ParseException ex) {
            return null;
        }
    }

    private static Date parseStringForTime(String input) {
        try {
            Calendar dst = Calendar.getInstance();
            Date d = new SimpleDateFormat(TIME_FORMAT).parse(input);
            dst.setTime(d);
            copyDate(Calendar.getInstance(), dst);
            return dst.getTime();
        } catch (ParseException ex) {
            return null;
        }
    }

    private static void copyDate(Calendar src, Calendar dst) {
        dst.set(YEAR, src.get(YEAR));
        dst.set(MONTH, src.get(MONTH));
        dst.set(DAY_OF_MONTH, src.get(DAY_OF_MONTH));
    }

    public static Duration toDuration(String input) {
        Duration duration = getDuration(input.trim());
        if (duration == null)
            throw new IllegalArgumentException("Input not parseble: " + input);
        return duration;
    }

    private static Duration getDuration(String input) {
        int index = indexOfTimeUnit(input);
        long duration = parseLong(input.substring(0, index));
        TimeUnit unit = toTimeUnit(input.substring(index));
        return newInstance(duration, unit);
    }

    private static int indexOfTimeUnit(String input) {
        char c = '0';
        int index = -1;
        while (c >= '0' && c <= '9') {
            if (++index == input.length())
                throw new TimeUnitNotFoundException(input);
            c = input.charAt(index);
        }
        if (index == 0)
            throw new DurationAmountNotFoundException(input);
        return index;
    }

    public static TimeUnit toTimeUnit(String timeUnit) {
        switch (timeUnit.toLowerCase().trim()) {
            case "days":
            case "d":
                return TimeUnit.DAYS;
            case "hours":
            case "h":
                return TimeUnit.HOURS;
            case "minutes":
            case "min":
            case "m":
                return TimeUnit.MINUTES;
            case "seconds":
            case "sec":
            case "s":
                return TimeUnit.SECONDS;
            case "milliseconds":
            case "millis":
            case "ms":
                return TimeUnit.MILLISECONDS;
            case "microseconds":
            case "micros":
            case "µs":
                return TimeUnit.MICROSECONDS;
            case "nanoseconds":
            case "nanos":
            case "ns":
                return TimeUnit.NANOSECONDS;
            default:
                throw new IllegalArgumentException(format("Unsupported time unit: %s", timeUnit));
        }
    }

    private static Duration newInstance(long duration, TimeUnit unit) {
        switch(unit) {
            case DAYS:          return Duration.ofDays(duration);
            case HOURS:         return Duration.ofHours(duration);
            case MINUTES:       return Duration.ofMinutes(duration);
            case SECONDS:       return Duration.ofSeconds(duration);
            case MILLISECONDS:  return Duration.ofMillis(duration);
            case MICROSECONDS:  return Duration.ofNanos(1000 * duration);
            case NANOSECONDS:   return Duration.ofNanos(duration);
            default:            return null;
        }
    }

    private Convertors() {
    }
}
