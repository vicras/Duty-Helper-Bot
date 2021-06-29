package com.sbo.service;

import com.sbo.entity.Notification;
import com.sbo.entity.Person;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Dmitars
 */
public interface NotificationService {
    List<Notification> findNotExpiredNotifications();

    Set<Person> getNotifiedUsersOf(Notification notification);

    Set<Notification> getNotificationsOf(Person person);

    void removeNotification(Notification notification);

    void updateNotification(Notification notification);
}
