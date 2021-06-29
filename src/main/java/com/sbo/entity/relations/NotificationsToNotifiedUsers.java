package com.sbo.entity.relations;

import com.sbo.entity.BaseEntity;
import com.sbo.entity.Notification;
import com.sbo.entity.Person;
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
