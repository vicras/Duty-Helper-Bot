package com.sbo.domain.postgres.entity.relations;

import com.sbo.domain.postgres.entity.BaseEntity;
import com.sbo.domain.postgres.entity.Notification;
import com.sbo.domain.postgres.entity.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * @author Dmitars
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "notificationsToNotified")
@EqualsAndHashCode(callSuper = true)
public class NotificationsToNotifiedUsers extends BaseEntity {
    @OneToMany
    Set<Person> notifiedUsers;

    @OneToMany
    Set<Notification> notifications;
}
