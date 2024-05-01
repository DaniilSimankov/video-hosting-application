package ru.simankovd.videoservice.utils;

import java.time.LocalDateTime;

public class DateUtil {

    public static String getCurrentDateCommentFormat() {
        return LocalDateTime.now().getDayOfMonth()+ "-" +
                LocalDateTime.now().getMonthValue() + "-" +
                LocalDateTime.now().getYear() + " " +
                LocalDateTime.now().getHour() + ":" +
                LocalDateTime.now().getMinute();
    }

    public static String getCurrentDateVideoFormat() {
        return LocalDateTime.now().getMonth().toString()+ " " +
                LocalDateTime.now().getDayOfMonth() + ", " +
                LocalDateTime.now().getYear() + " " +
                LocalDateTime.now().getHour() + ":" +
                LocalDateTime.now().getMinute();
    }
}
