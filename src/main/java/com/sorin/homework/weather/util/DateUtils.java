package com.sorin.homework.weather.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static LocalDate stringToDate(String stringDate, String dateFormat) {
        return LocalDate.parse(stringDate, DateTimeFormatter.ofPattern(dateFormat));
    }

}
