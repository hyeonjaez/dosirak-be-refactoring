package com.example.dosirakbe.domain.user_activity.service;

import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.domain.user_activity.dto.mapper.UserActivityMapper;
import com.example.dosirakbe.domain.user_activity.dto.response.UserActivityResponse;
import com.example.dosirakbe.domain.user_activity.entity.UserActivity;
import com.example.dosirakbe.domain.user_activity.repository.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserActivityService {
    private final UserActivityRepository userActivityRepository;
    private final UserRepository userRepository;
    private final UserActivityMapper userActivityMapper;

    public List<UserActivityResponse> getUserActivityList(Long userId, YearMonth yearMonth) {
        User user = userRepository.findById(userId).orElseThrow(); //TODO throw
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<UserActivity> byUserAndCreatedAtBetweenOrderByCreatedAtAsc = userActivityRepository.findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(user, startDate, endDate);

        return userActivityMapper.mapToUserActivityResponseList(byUserAndCreatedAtBetweenOrderByCreatedAtAsc);
    }
}
