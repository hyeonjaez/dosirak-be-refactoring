package com.example.dosirakbe.domain.user_activity.service;


import com.example.dosirakbe.domain.activity_log.entity.ActivityType;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.domain.user_activity.dto.mapper.UserActivityMapper;
import com.example.dosirakbe.domain.user_activity.dto.response.UserActivityResponse;
import com.example.dosirakbe.domain.user_activity.entity.UserActivity;
import com.example.dosirakbe.domain.user_activity.repository.UserActivityRepository;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserActivityService {
    private final UserActivityRepository userActivityRepository;
    private final UserRepository userRepository;
    private final UserActivityMapper userActivityMapper;

    public List<UserActivityResponse> getUserActivityList(Long userId, YearMonth month) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));


        LocalDate firstDay = month.atDay(1);  // 해당 월의 첫째 날
        LocalDate lastDay = month.atEndOfMonth();

        List<UserActivity> byUserAndCreatedAtBetweenOrderByCreatedAtAsc = userActivityRepository.findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(user, firstDay, lastDay);

        return userActivityMapper.mapToUserActivityResponseList(byUserAndCreatedAtBetweenOrderByCreatedAtAsc);
    }

    @Transactional
    public void createOrIncrementUserActivity(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

        Optional<UserActivity> userActivityOptional = userActivityRepository.findByUserAndCreatedAt(user, LocalDate.now());
        UserActivity userActivity;
        if (userActivityOptional.isPresent()) {
            userActivity = userActivityOptional.get();
            userActivity.addCommitCount();
        } else {
            userActivity = new UserActivity(user);
            userActivityRepository.save(userActivity);
        }
    }





}
