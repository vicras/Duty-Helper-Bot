package com.sbo.repo;

import com.sbo.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Dmitars
 */
public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
