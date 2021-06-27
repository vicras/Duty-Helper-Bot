package com.sbo.entity;

import com.sbo.entity.enums.DutyTypes;
import com.sbo.entity.enums.PersonRole;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.EnumType.STRING;

/**
 * @author viktar hraskou
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "duties")
@EqualsAndHashCode(callSuper = true)
public class Duty extends BaseEntity {

    @Column(name = "duty_from", nullable = false)
    private LocalDateTime dutyFrom;

    @Column(name = "duty_to", nullable = false)
    private LocalDateTime dutyTo;

    @Column(name = "max_people_on_duty", nullable = false)
    private Long maxPeopleOnDuty;

    @ElementCollection
    @Enumerated(STRING)
    @CollectionTable(name ="duty_type")
    private Set<DutyTypes> dutyTypes;

    @OneToMany(mappedBy = "duty", cascade = REMOVE)
    private Collection<PeopleOnDuty> peopleOnDuties;

    @Builder
    public Duty(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime dutyFrom, LocalDateTime dutyTo, Long maxPeopleOnDuty, Set<DutyTypes> dutyTypes) {
        super(id, createdAt, updatedAt);
        this.dutyFrom = dutyFrom;
        this.dutyTo = dutyTo;
        this.maxPeopleOnDuty = maxPeopleOnDuty;
        this.dutyTypes = dutyTypes;
    }
}