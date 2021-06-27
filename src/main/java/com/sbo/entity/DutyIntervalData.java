package com.sbo.entity;


import com.sbo.common.time.LocalDateTimeInterval;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class DutyIntervalData {
    @ManyToOne
    private PeopleOnDuty peopleOnDuty;

    @NotEmpty
    @Column(nullable = false)
    private LocalDateTime from;

    @NotEmpty
    @Column(nullable = false)
    private LocalDateTime to;

    public static DutyIntervalData of(PeopleOnDuty peopleOnDuty, LocalDateTimeInterval localDateTimeInterval) {
        var from = localDateTimeInterval.getStart();
        var to = localDateTimeInterval.getEnd();
        return new DutyIntervalData(peopleOnDuty, from, to);
    }
}