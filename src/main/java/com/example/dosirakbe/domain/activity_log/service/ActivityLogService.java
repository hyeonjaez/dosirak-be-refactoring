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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityLogService {
    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;
    private final ActivityLogMapper activityLogMapper;

    public List<ActivityLogResponse> getTodayActivityLog(Long userId, LocalDate today) {
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
    public void addActivityLog(Long userId, Long contentId, ActivityType activityType) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

        ActivityLog activityLog = new ActivityLog(contentId, user, activityType);
        activityLogRepository.save(activityLog);
    }


}
