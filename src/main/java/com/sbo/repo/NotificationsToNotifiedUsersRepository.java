package com.sbo.repo;

import com.sbo.entity.relations.NotificationsToNotifiedUsers;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Dmitars
 */
public interface NotificationsToNotifiedUsersRepository extends JpaRepository<NotificationsToNotifiedUsers, Long> {
}
