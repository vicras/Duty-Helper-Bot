package com.sbo.domain.postgres.repository;

import com.sbo.domain.postgres.entity.relations.NotificationsToNotifiedUsers;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Dmitars
 */
public interface NotificationsToNotifiedUsersRepository extends JpaRepository<NotificationsToNotifiedUsers, Long> {
}
