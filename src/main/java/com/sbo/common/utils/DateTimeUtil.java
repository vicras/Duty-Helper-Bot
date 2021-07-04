package com.sbo.common.utils;

import com.github.sisyphsu.dateparser.DateParser;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * @author viktar hraskou
 */
public class DateTimeUtil {
    public static LocalDate date2LocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDate parseDate(String text) throws DateTimeParseException {
        DateParser parser = DateParser.newBuilder().build();
        return date2LocalDate(parser.parseDate(text));
    }
}
