package com.sbo.domain.postgres.entity;


import com.sbo.common.time.LocalDateTimeInterval;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class DutyIntervalData {

    // TODO link with
    @Transient
    private PeopleOnDuty peopleOnDuty;

    @NotEmpty
    @Column(name = "fromTime", nullable = false)
    private LocalDateTime from;

    @NotEmpty
    @Column(name = "toTime", nullable = false)
    private LocalDateTime to;

    public static DutyIntervalData of(PeopleOnDuty peopleOnDuty, LocalDateTimeInterval localDateTimeInterval) {
        var from = localDateTimeInterval.getStart();
        var to = localDateTimeInterval.getEnd();
        return new DutyIntervalData(peopleOnDuty, from, to);
    }
}
