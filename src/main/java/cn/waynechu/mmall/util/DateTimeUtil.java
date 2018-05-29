package cn.waynechu.mmall.util;


import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author waynechu
 * Created 2017-10-19 11:20
 */
public class DateTimeUtil {

    private static final String STANDARD_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String STANDARD_DATE_FORMAT = "yyyy-MM-dd";

    private DateTimeUtil() {
    }

    public static LocalDateTime toLocalDateTimeFromDate(Date date) {
        Instant instant = Instant.ofEpochMilli(date.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static LocalDateTime toLocalDateTimeFromString(String text, String format) {
        return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(format));
    }

    public static LocalDateTime toLocalDateTimeFromString(String text) {
        if (text == null) {
            return null;
        }
        return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(STANDARD_DATETIME_FORMAT));
    }

    public static LocalDate toLocalDateFromDate(Date date) {
        Instant instant = Instant.ofEpochMilli(date.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDate toLocalDateFromString(String text, String format) {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern(format));
    }

    public static LocalDate toLocalDateFromString(String text) {
        if (text == null) {
            return null;
        }
        return toLocalDateFromString(text, STANDARD_DATE_FORMAT);
    }

    public static LocalTime toLocalTimeFromDate(Date date) {
        Instant instant = Instant.ofEpochMilli(date.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime();
    }

    public static Date toDateFromLocalDateTime(LocalDateTime date) {
        Instant instant = date.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static Date toDateFromLocalDate(LocalDate date) {
        Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static Date toDateFromLocalTime(LocalTime date) {
        Instant instant = date.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static Date toDateFromString(String string, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = formatter.parse(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date toDateFromString(String string) {
        return toDateFromString(string, STANDARD_DATETIME_FORMAT);
    }

    public static String toStringFromLocalDate(LocalDate date, String format) {
        return date.format(DateTimeFormatter.ofPattern(format));
    }

    public static String toStringFromLocalDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return toStringFromLocalDate(date, STANDARD_DATE_FORMAT);
    }

    public static String toStringFromLocalDateTime(LocalDateTime date, String format) {
        return date.format(DateTimeFormatter.ofPattern(format));
    }

    public static String toStringFromLocalDateTime(LocalDateTime date) {
        if (date == null) {
            return "";
        }
        return toStringFromLocalDateTime(date, STANDARD_DATETIME_FORMAT);
    }

}
