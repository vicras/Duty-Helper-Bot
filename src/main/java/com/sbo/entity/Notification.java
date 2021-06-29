package com.sbo.entity;

import com.sbo.entity.enums.NotificationType;
import com.sbo.entity.relations.NotificationsToNotifiedUsers;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "notifications")
@EqualsAndHashCode(callSuper = true)
public class Notification extends BaseEntity{
    private String text;

    @ManyToOne
    private Person author;

    private LocalDateTime expirationDate;

    private NotificationType notificationType;

    @ManyToOne
    NotificationsToNotifiedUsers notificationsToNotifiedUsers;
}
