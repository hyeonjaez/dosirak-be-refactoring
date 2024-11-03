package com.example.dosirakbe.domain.user_activity.repository;

import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user_activity.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    List<UserActivity> findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(User user, LocalDate startDate, LocalDate endDate);
}
