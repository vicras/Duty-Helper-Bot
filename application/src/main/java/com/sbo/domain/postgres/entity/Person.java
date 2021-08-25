package com.sbo.domain.postgres.entity;

import com.sbo.domain.postgres.entity.enums.EntityStatus;
import com.sbo.domain.postgres.entity.enums.Language;
import com.sbo.domain.postgres.entity.enums.PersonRole;
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

import static com.sbo.domain.postgres.entity.enums.Language.ENGLISH;
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

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "telegram_id", nullable = false, unique = true)
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

    @Column(name = "home_address")
    private String homeAddress;

    @Column(name = "language", nullable = false)
    @Enumerated(STRING)
    private Language language = ENGLISH;

    @ElementCollection
    @Enumerated(STRING)
    @BatchSize(size = 50)
    @CollectionTable(name = "roles")
    private Set<PersonRole> roles;

    @NotEmpty
    @Column(name = "state")
    private String state = "class com.sbo.bot.state.impl.StartState";

    @Builder
    public Person(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, EntityStatus entityStatus, String firstName, String lastName, String patronymic, Long telegramId, LocalDate birthDate, Long tel, String mail, String homeAddress, Set<PersonRole> roles) {
        super(id, createdAt, updatedAt, entityStatus);
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.telegramId = telegramId;
        this.birthDate = birthDate;
        this.tel = tel;
        this.mail = mail;
        this.homeAddress = homeAddress;
        this.roles = roles;
    }

    public String telegramLink() {
        return String.format("[%s](tg://user?id=%d)", shortFirstLastName(), telegramId);
    }

    public String shortFirstLastName() {
        return String.format("%s. %s", firstName.charAt(0), lastName);
    }
}