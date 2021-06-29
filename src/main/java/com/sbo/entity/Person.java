package com.sbo.entity;

import com.sbo.entity.enums.PersonRole;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.validator.constraints.Range;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Email;
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
    @Column(name = "last_name")
    private String lastName;

    @NotEmpty
    @Column(name = "patronymic")
    private String patronymic;

    @NotEmpty
    @Column(name = "telegram_id", nullable = false)
    private Long telegramId;

    @PastOrPresent
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Range(min = 100000000000L, max = 999999999999L)
    @Column(name = "telephone")
    private Long tel;

    @Email
    @Column(name = "mail")
    private String mail;

    @NotEmpty
    @Column(name = "address", nullable = false)
    private String address;

    @ElementCollection
    @Enumerated(STRING)
    @BatchSize(size = 50)
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
