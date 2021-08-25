package com.sbo.domain.postgres.entity;

import com.google.common.collect.Range;
import com.sbo.domain.postgres.converter.RangeConverter;
import com.sbo.domain.postgres.entity.enums.EntityStatus;
import com.sbo.domain.postgres.entity.enums.PeopleOnDutyStatus;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static com.sbo.domain.postgres.entity.enums.PeopleOnDutyStatus.ACTIVE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

/**
 * @author viktar hraskou
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "people_on_duty")
@EqualsAndHashCode(callSuper = true)
public class PeopleOnDuty extends BaseEntity {

    @ManyToOne(fetch = LAZY, optional = false)
    private Person person;

    @ManyToOne(fetch = LAZY, optional = false)
    private Duty duty;

    @Convert(converter = RangeConverter.class)
    @Column(name = "range", nullable = false)
    private Range<LocalDateTime> range;

    @Enumerated(STRING)
    @Column(name = "status", nullable = false)
    private PeopleOnDutyStatus status = ACTIVE;

    @Column(name = "is_people_could_change", nullable = false)
    private Boolean isPeopleCouldChange = true;

    @Builder
    public PeopleOnDuty(Range<LocalDateTime> range, Long id, LocalDateTime createdAt, LocalDateTime updatedAt, EntityStatus entityStatus, Person person, Duty duty, PeopleOnDutyStatus status, Boolean isPeopleCouldChange) {
        super(id, createdAt, updatedAt, entityStatus);
        this.person = person;
        this.duty = duty;
        this.range = range;
        this.status = status;
        this.isPeopleCouldChange = isPeopleCouldChange;
    }

    public LocalDateTime getFromTime() {
        return range.lowerEndpoint();
    }

    public LocalDateTime getToTime() {
        return range.upperEndpoint();
    }
}
