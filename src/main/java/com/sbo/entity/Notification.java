package com.sbo.entity;

import com.sbo.entity.enums.NotificationType;
import com.sbo.entity.relations.NotificationsToNotifiedUsers;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static com.sbo.entity.enums.NotificationType.INFO;

@Data
@Entity
@NoArgsConstructor
@Table(name = "notifications")
@EqualsAndHashCode(callSuper = true)
public class Notification extends BaseEntity {

    @ManyToOne
    NotificationsToNotifiedUsers notificationsToNotifiedUsers;
    private String text;

    @ManyToOne
    private Person author;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "notificationType", nullable = false)
    private NotificationType notificationType = INFO;
}
