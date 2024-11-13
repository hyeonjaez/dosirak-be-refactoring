package com.example.dosirakbe.domain.activity_log.service;

import com.example.dosirakbe.domain.activity_log.dto.mapper.ActivityLogMapper;
import com.example.dosirakbe.domain.activity_log.dto.response.ActivityLogResponse;
import com.example.dosirakbe.domain.activity_log.entity.ActivityLog;
import com.example.dosirakbe.domain.activity_log.entity.ActivityType;
import com.example.dosirakbe.domain.activity_log.repository.ActivityLogRepository;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityLogService {
    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;
    private final ActivityLogMapper activityLogMapper;

    public List<ActivityLogResponse> getTodayDateActivityLog(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
        LocalDate localDate = LocalDate.now();
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);

        List<ActivityLog> activityLogList = activityLogRepository.findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(user, startOfDay, endOfDay);
        return activityLogMapper.mapToActivityLogResponseList(activityLogList);
    }


    public List<ActivityLogResponse> getThatDateActivityLog(Long userId, LocalDate today) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        List<ActivityLog> activityLogList = activityLogRepository.findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(user, startOfDay, endOfDay);
        return activityLogMapper.mapToActivityLogResponseList(activityLogList);
    }

    // 선택한 월의 첫날의 활동 로그 조회
    public List<ActivityLogResponse> getActivityLogForFirstDayOfMonth(Long userId, LocalDate month) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

        LocalDate firstDayOfMonth = month.withDayOfMonth(1);
        LocalDateTime startOfDay = firstDayOfMonth.atStartOfDay();
        LocalDateTime endOfDay = firstDayOfMonth.atTime(LocalTime.MAX);

        List<ActivityLog> activityLogList = activityLogRepository.findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(user, startOfDay, endOfDay);

        return activityLogMapper.mapToActivityLogResponseList(activityLogList);
    }

    @Transactional
    public void addActivityLog(Long userId, Long contentId, ActivityType activityType, BigDecimal distance) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

        if (activityType.equals(ActivityType.LOW_CARBON_MEANS_OF_TRANSPORTATION)) {
            if (Objects.isNull(distance) || distance.compareTo(BigDecimal.ZERO) < 0) {
                throw new ApiException(ExceptionEnum.INVALID_REQUEST);
            }
        }

        ActivityLog activityLog = new ActivityLog(contentId, user, activityType);
        activityLogRepository.save(activityLog);
    }


}
