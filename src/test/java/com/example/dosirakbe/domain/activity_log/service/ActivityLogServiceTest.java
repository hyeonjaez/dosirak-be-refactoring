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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityLogServiceTest {

    @InjectMocks
    ActivityLogService activityLogService;

    @Mock
    ActivityLogRepository activityLogRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ActivityLogMapper activityLogMapper;

    @Test
    @DisplayName("getThatDateActivityLog - 성공")
    void getThatDateActivityLogTest_success() {
        Long userId = 1L;
        LocalDate thatDay = LocalDate.of(2024, 8, 8);
        LocalDateTime startOfDay = thatDay.atStartOfDay();
        LocalDateTime endOfDay = thatDay.atTime(LocalTime.MAX);

        User mockUser = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        ActivityLog mockActivityLog = mock(ActivityLog.class);
        List<ActivityLog> mockActivityLogList = List.of(mockActivityLog);
        when(activityLogRepository.findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(mockUser, startOfDay, endOfDay)).thenReturn(mockActivityLogList);

        ActivityLogResponse mockResponse = mock(ActivityLogResponse.class);
        List<ActivityLogResponse> mockResponseList = List.of(mockResponse);
        when(activityLogMapper.mapToActivityLogResponseList(mockActivityLogList)).thenReturn(mockResponseList);

        List<ActivityLogResponse> result = activityLogService.getThatDateActivityLog(userId, thatDay);

        assertNotNull(result);
        assertEquals(result.size(), mockResponseList.size());
        assertEquals(mockResponseList, result);

        verify(userRepository, times(1)).findById(any(Long.class));
        verify(activityLogRepository, times(1)).findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(activityLogMapper, times(1)).mapToActivityLogResponseList(any());

    }

    @Test
    @DisplayName("getThatDateActivityLog - 실패 : 유저 not found")
    void getThatDateActivityLogTest_fail_userNotFound() {
        Long userId = 1L;
        LocalDate thatDay = LocalDate.of(2024, 8, 8);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(ApiException.class, () -> activityLogService.getThatDateActivityLog(userId, thatDay));

        verify(userRepository, times(1)).findById(any(Long.class));
        verify(activityLogRepository, times(0)).findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(activityLogMapper, times(0)).mapToActivityLogResponseList(any());
    }

    @Test
    @DisplayName("getThatDateActivityLog - 지정된 날짜가 null 일 경우 - 성공")
    void getThatDateActivityLogTest_thatDate_isNull_success() {
        Long userId = 1L;
        LocalDate thatDay = null;

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        User mockUser = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        ActivityLog mockActivityLog = mock(ActivityLog.class);
        List<ActivityLog> mockActivityLogList = List.of(mockActivityLog);
        when(activityLogRepository.findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(mockUser, startOfDay, endOfDay)).thenReturn(mockActivityLogList);

        ActivityLogResponse mockResponse = mock(ActivityLogResponse.class);
        List<ActivityLogResponse> mockResponseList = List.of(mockResponse);
        when(activityLogMapper.mapToActivityLogResponseList(mockActivityLogList)).thenReturn(mockResponseList);

        List<ActivityLogResponse> result = activityLogService.getThatDateActivityLog(userId, thatDay);

        assertNotNull(result);
        assertEquals(result.size(), mockResponseList.size());
        assertEquals(mockResponseList, result);

        verify(userRepository, times(1)).findById(any(Long.class));
        verify(activityLogRepository, times(1)).findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(activityLogMapper, times(1)).mapToActivityLogResponseList(any());
    }

    @Test
    @DisplayName("getActivityLogForFirstDayOfMonth - 성공")
    void getActivityLogForFirstDayOfMonthTest_success() {
        Long userId = 1L;
        YearMonth yearMonth = YearMonth.of(2024, 8);

        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDateTime startOfDay = firstDayOfMonth.atStartOfDay();
        LocalDateTime endOfDay = firstDayOfMonth.atTime(LocalTime.MAX);

        User mockUser = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        ActivityLog mockActivityLog = mock(ActivityLog.class);
        List<ActivityLog> mockActivityLogList = List.of(mockActivityLog);

        when(activityLogRepository.findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(mockUser, startOfDay, endOfDay)).thenReturn(mockActivityLogList);
        ActivityLogResponse mockResponse = mock(ActivityLogResponse.class);
        List<ActivityLogResponse> mockResponseList = List.of(mockResponse);
        when(activityLogMapper.mapToActivityLogResponseList(mockActivityLogList)).thenReturn(mockResponseList);

        List<ActivityLogResponse> result = activityLogService.getActivityLogForFirstDayOfMonth(userId, yearMonth);
        assertNotNull(result);
        assertEquals(result.size(), mockResponseList.size());
        assertEquals(mockResponseList, result);
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(activityLogRepository, times(1)).findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(any(), any(), any());
        verify(activityLogMapper, times(1)).mapToActivityLogResponseList(any());
    }

    @Test
    @DisplayName("getActivityLogForFirstDayOfMonth - 실패 : 유저 not found")
    void getActivityLogForFirstDayOfMonthTest_fail_userNotFound() {
        Long userId = 1L;
        YearMonth yearMonth = YearMonth.of(2024, 8);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(ApiException.class, () -> activityLogService.getActivityLogForFirstDayOfMonth(userId, yearMonth));
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(activityLogRepository, times(0)).findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(any(), any(), any());
        verify(activityLogMapper, times(0)).mapToActivityLogResponseList(any());
    }

    @Test
    @DisplayName("addActivityLog - 성공적으로 활동 로그를 추가 (LOW_CARBON_MEANS_OF_TRANSPORTATION)")
    void addActivityLog_Success_LowCarbon() {
        Long userId = 1L;
        Long contentId = 10L;
        ActivityType activityType = ActivityType.LOW_CARBON_MEANS_OF_TRANSPORTATION;
        BigDecimal distance = BigDecimal.valueOf(15.5);

        User mockUser = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        activityLogService.addActivityLog(userId, contentId, activityType, distance);

        verify(userRepository, times(1)).findById(userId);
        verify(activityLogRepository, times(1)).save(any(ActivityLog.class));
    }

    @Test
    @DisplayName("addActivityLog - 성공적으로 활동 로그를 추가 (기타 활동 유형)")
    void addActivityLog_Success_OtherActivityType() {
        Long userId = 1L;
        Long contentId = 20L;
        ActivityType activityType = ActivityType.MULTI_USE_CONTAINER_PACKAGING;

        User mockUser = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        activityLogService.addActivityLog(userId, contentId, activityType, null);

        verify(userRepository, times(1)).findById(userId);
        verify(activityLogRepository, times(1)).save(any(ActivityLog.class));
    }

    @Test
    @DisplayName("addActivityLog - 실패 : user not found")
    void addActivityLog_Fail_UserNotFound() {
        Long userId = 1L;
        Long contentId = 10L;
        ActivityType activityType = ActivityType.LOW_CARBON_MEANS_OF_TRANSPORTATION;
        BigDecimal distance = BigDecimal.valueOf(15.5);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class,
                () -> activityLogService.addActivityLog(userId, contentId, activityType, distance));
        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());

        verify(userRepository, times(1)).findById(userId);
        verifyNoInteractions(activityLogRepository);
    }

    @Test
    @DisplayName("addActivityLog - 실패 : 잘못된 거리 정보")
    void addActivityLog_Fail_DistanceIsNull() {
        Long userId = 1L;
        Long contentId = 10L;
        ActivityType activityType = ActivityType.LOW_CARBON_MEANS_OF_TRANSPORTATION;

        User mockUser = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        ApiException exception = assertThrows(ApiException.class,
                () -> activityLogService.addActivityLog(userId, contentId, activityType, null));
        assertEquals(ExceptionEnum.INVALID_REQUEST, exception.getError());

        verify(userRepository, times(1)).findById(userId);
        verifyNoInteractions(activityLogRepository);
    }

    @Test
    @DisplayName("addActivityLog - 실패 : 잘못된 거리 정보")
    void addActivityLog_Fail_InvalidDistance() {
        Long userId = 1L;
        Long contentId = 10L;
        ActivityType activityType = ActivityType.LOW_CARBON_MEANS_OF_TRANSPORTATION;
        BigDecimal distance = BigDecimal.ZERO;

        User mockUser = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        ApiException exception = assertThrows(ApiException.class,
                () -> activityLogService.addActivityLog(userId, contentId, activityType, distance));
        assertEquals(ExceptionEnum.INVALID_REQUEST, exception.getError());

        verify(userRepository, times(1)).findById(userId);
        verifyNoInteractions(activityLogRepository);
    }
}