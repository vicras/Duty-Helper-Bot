package com.sbo.bot.handler.impl;

import com.github.sisyphsu.dateparser.DateParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static com.sbo.common.utils.DateTimeUtil.date2LocalDate;

/**
 * @author viktar hraskou
 */
class BirthdayHandlerTest {

    @Test
    void tryToParseDate() {

        //given
        DateParser parser = DateParser.newBuilder().build();
        var expected = LocalDate.of(2000, 11, 30);

        //when
        var dates = new ArrayList<LocalDate>();
        dates.add(date2LocalDate(parser.parseDate("20001130")));
        dates.add(date2LocalDate(parser.parseDate("30.11.2000")));
        dates.add(date2LocalDate(parser.parseDate("2000.11.30")));
        dates.add(date2LocalDate(parser.parseDate("2000/11/30")));
        dates.add(date2LocalDate(parser.parseDate("30/11/2000")));

        //then
        dates.forEach(date -> Assertions.assertEquals(expected, date));
    }

}