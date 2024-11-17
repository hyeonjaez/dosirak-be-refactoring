package com.example.dosirakbe.domain.activity_log.repository;

import com.example.dosirakbe.domain.activity_log.entity.ActivityLog;
import com.example.dosirakbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(User user, LocalDateTime start, LocalDateTime end);

    void deleteByUser(User user);

    boolean existsByUser(User user);
}
