package com.sbo.entity;

import com.sbo.entity.enums.PersonRole;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static javax.persistence.EnumType.STRING;

/**
 * @author viktar hraskou
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "person")
@EqualsAndHashCode(callSuper = true)
public class Person extends BaseEntity {

    @NotEmpty
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotEmpty
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotEmpty
    @Column(name = "patronymic", nullable = false)
    private String patronymic;

    @PastOrPresent
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @NotEmpty
    @Column(name = "address", nullable = false)
    private String address;

    @ElementCollection
    @Enumerated(STRING)
    @CollectionTable(name = "roles")
    private Set<PersonRole> roles;

    @Builder
    public Person(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String firstName, String lastName, LocalDate birthDate, Set<PersonRole> roles) {
        super(id, createdAt, updatedAt);
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.roles = roles;
    }
}
