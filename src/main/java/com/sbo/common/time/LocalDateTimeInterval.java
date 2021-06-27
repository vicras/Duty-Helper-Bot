package com.sbo.common.time;


import com.sbo.entity.DutyIntervalData;
import com.sbo.entity.PeopleOnDuty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
public class LocalDateTimeInterval {
    @Getter
    LocalDateTime start;

    @Getter
    LocalDateTime end;

    public boolean intersects(LocalDateTimeInterval anotherInterval) {
        return anotherInterval.contains(start)
                || anotherInterval.contains(end)
                || contains(anotherInterval.start.plusMinutes(1))
                || contains(anotherInterval.end.minusMinutes(1));
    }

    public static LocalDateTimeInterval of(DutyIntervalData dutyIntervalData) {
        var from = dutyIntervalData.getFrom();
        var to = dutyIntervalData.getTo();
        return new LocalDateTimeInterval(from, to);
    }

    public static LocalDateTimeInterval of(PeopleOnDuty peopleOnDuty){
        var from = peopleOnDuty.getOnDutyFrom();
        var to = peopleOnDuty.getOnDutyTo();
        return new LocalDateTimeInterval(from, to);
    }

    public int getHoursBetween() {
        var temp = end.getHour() - start.getHour();
        temp = end.getMinute() != 0 ? temp + 1 : temp;
        return temp;
    }

    public boolean contains(LocalDateTime localTime) {
        return (localTime.isBefore(end) && localTime.isAfter(start)
                || localTime.isEqual(end) || localTime.isEqual(start));
    }

    public int getPointPosition(LocalDateTime localDateTime) {
        if (localDateTime.isBefore(start))
            return -1;
        else if (localDateTime.isBefore(end))
            return 0;
        else
            return 1;
    }
}
