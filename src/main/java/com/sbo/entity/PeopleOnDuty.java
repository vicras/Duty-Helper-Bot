package com.sbo.entity;

import com.sbo.common.time.LocalDateTimeInterval;
import com.sbo.entity.enums.EntityStatus;
import com.sbo.entity.enums.PeopleOnDutyStatus;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static com.sbo.entity.enums.PeopleOnDutyStatus.ACTIVE;
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

    @Column(name = "on_duty_from", nullable = false)
    private LocalDateTime onDutyFrom;

    @Column(name = "on_duty_to", nullable = false)
    private LocalDateTime onDutyTo;

    @Enumerated(STRING)
    @Column(name = "status", nullable = false)
    private PeopleOnDutyStatus status = ACTIVE;

    @Column(name = "is_people_could_change", nullable = false)
    private Boolean isPeopleCouldChange = true;

    @Builder
    public PeopleOnDuty(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, EntityStatus entityStatus, Person person, Duty duty, LocalDateTime onDutyFrom, LocalDateTime onDutyTo, PeopleOnDutyStatus status, Boolean isPeopleCouldChange) {
        super(id, createdAt, updatedAt, entityStatus);
        this.person = person;
        this.duty = duty;
        this.onDutyFrom = onDutyFrom;
        this.onDutyTo = onDutyTo;
        this.status = status;
        this.isPeopleCouldChange = isPeopleCouldChange;
    }

    public LocalDateTimeInterval getWorkInterval() {
        return new LocalDateTimeInterval(onDutyFrom, onDutyFrom);
    }
}
