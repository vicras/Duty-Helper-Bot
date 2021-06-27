package com.sbo.entity;

import com.sbo.entity.enums.PersonRole;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

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

    @ManyToOne(fetch = LAZY)
    private Person person;

    @ManyToOne(fetch = LAZY)
    private Duty duty;

    @Column(name = "on_duty_from")
    private LocalDateTime onDutyFrom;

    @Column(name = "on_duty_to")
    private LocalDateTime onDutyTo;

    @Builder
    public PeopleOnDuty(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, Person person, Duty duty, LocalDateTime onDutyFrom, LocalDateTime onDutyTo) {
        super(id, createdAt, updatedAt);
        this.person = person;
        this.duty = duty;
        this.onDutyFrom = onDutyFrom;
        this.onDutyTo = onDutyTo;
    }
}
