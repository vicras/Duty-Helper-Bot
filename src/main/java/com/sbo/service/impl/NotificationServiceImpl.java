package com.sbo.service.impl;

import com.sbo.common.utils.StreamUtil;
import com.sbo.entity.Notification;
import com.sbo.entity.Person;
import com.sbo.entity.relations.NotificationsToNotifiedUsers;
import com.sbo.repo.NotificationRepository;
import com.sbo.repo.NotificationsToNotifiedUsersRepository;
import com.sbo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Dmitars
 */
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationsToNotifiedUsersRepository notificationsToNotifiedUsersRepository;


    @Override
    public List<Notification> findNotExpiredNotifications() {
        return StreamUtil.filter(
                notificationRepository.findAll(),
                notification -> notification.getExpirationDate().isAfter(LocalDateTime.now())
        );
    }

    @Override
    public Set<Person> getNotifiedUsersOf(Notification notification) {
        return notification.getNotificationsToNotifiedUsers().getNotifiedUsers();
    }

    @Override
    public Set<Notification> getNotificationsOf(Person person) {
        return notificationsToNotifiedUsersRepository.findAll().stream()
                .filter(notificationsToNotifiedUsers -> notificationsToNotifiedUsers.getNotifiedUsers().contains(person))
                .map(NotificationsToNotifiedUsers::getNotifications)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public void removeNotification(Notification notification) {
        //TODO: change state of object to removed
    }

    @Override
    public void updateNotification(Notification notification) {
        notificationRepository.save(notification);
    }
}