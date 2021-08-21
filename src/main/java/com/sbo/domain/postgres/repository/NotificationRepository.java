package com.sbo.domain.postgres.repository;

import com.sbo.domain.postgres.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Dmitars
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
