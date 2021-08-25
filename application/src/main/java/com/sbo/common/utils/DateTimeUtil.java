package com.sbo.common.utils;

import com.github.sisyphsu.dateparser.DateParser;
import com.google.common.collect.Range;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public static Range<LocalTime> parseTimeRange(String timeRange) {
        timeRange = timeRange.trim();
        var times = timeRange.contains("-") ?
                timeRange.split(":") :
                timeRange.split(" ");
        var firstTime = LocalTime.parse(times[0]);
        var secondTime = LocalTime.parse(times[1]);
        return Range.closedOpen(firstTime, secondTime);
    }

    public static Range<LocalDateTime> convert2LocalDateTimeRange(Range<LocalTime> localTimeRange, LocalDate date) {
        var lower = localTimeRange.lowerEndpoint().atDate(date);
        var upper = localTimeRange.upperEndpoint().atDate(date);
        return Range.closedOpen(lower, upper);
    }

    public static boolean isTimeRange(String range) {
        try {
            parseTimeRange(range);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
