package com.abc.backend.CNPM.repository;

import com.abc.backend.CNPM.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    long countByIsReadFalse();

    List<Notification> findTop5ByOrderBySentAtDesc();
}
