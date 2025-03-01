package com.example.dosirakbe.domain.user_activity.implement;

import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user_activity.entity.UserActivity;
import com.example.dosirakbe.domain.user_activity.repository.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserActivityReader {
    private final UserActivityRepository userActivityRepository;

    public List<UserActivity> getUserActivitiesByDate(User user, LocalDate firstDate, LocalDate lastDate) {
        return userActivityRepository.findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(user, firstDate, lastDate);
    }

    public Optional<UserActivity> findTodayActivityByUser(User user) {
        return userActivityRepository.findByUserAndCreatedAt(user, LocalDate.now());
    }


}
