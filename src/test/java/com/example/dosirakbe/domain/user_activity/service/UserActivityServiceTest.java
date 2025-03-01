package com.example.dosirakbe.domain.user_activity.service;

import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.domain.user_activity.domain.mapper.UserActivityMapper;
import com.example.dosirakbe.domain.user_activity.dto.response.UserActivityResponse;
import com.example.dosirakbe.domain.user_activity.entity.UserActivity;
import com.example.dosirakbe.domain.user_activity.repository.UserActivityRepository;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserActivityServiceTest {

    @InjectMocks
    private UserActivityService userActivityService;

    @Mock
    private UserActivityRepository userActivityRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserActivityMapper userActivityMapper;

    @Test
    @DisplayName("getUserActivityList - 성공")
    void getUserActivityListTest_success() {
        Long userId = 1L;
        YearMonth yearMonth = YearMonth.of(2024, 8);

        User mockUser = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();

        UserActivity mockActivity = mock(UserActivity.class);
        List<UserActivity> mockActivityList = List.of(mockActivity);
        when(userActivityRepository.findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(
                mockUser, firstDay, lastDay)).thenReturn(mockActivityList);

        UserActivityResponse mockResponse = mock(UserActivityResponse.class);
        List<UserActivityResponse> mockResponseList = List.of(mockResponse);
        when(userActivityMapper.mapToUserActivityResponseList(mockActivityList)).thenReturn(mockResponseList);

        List<UserActivityResponse> result = userActivityService.getUserActivityList(userId, yearMonth);

        assertNotNull(result);
        assertEquals(mockResponseList.size(), result.size());
        assertEquals(mockResponseList, result);

        verify(userRepository, times(1)).findById(eq(userId));
        verify(userActivityRepository, times(1)).findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(eq(mockUser), eq(firstDay), eq(lastDay));
        verify(userActivityMapper, times(1)).mapToUserActivityResponseList(eq(mockActivityList));
    }

    @Test
    @DisplayName("getUserActivityList - 실패 : 유저 not found")
    void getUserActivityListTest_fail_userNotFound() {
        Long userId = 1L;
        YearMonth yearMonth = YearMonth.of(2024, 8);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () ->
                        userActivityService.getUserActivityList(userId, yearMonth),
                "Expected ApiException to be thrown");

        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError(), "Exception enum should be DATA_NOT_FOUND");

        verify(userRepository, times(1)).findById(eq(userId));
        verifyNoInteractions(userActivityRepository);
        verifyNoInteractions(userActivityMapper);
    }

    @Test
    @DisplayName("createOrIncrementUserActivity - 성공: 활동 기록 존재, 커밋 수 증가")
    void createOrIncrementUserActivityTest_success_increment() {
        Long userId = 1L;
        User mockUser = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        UserActivity mockActivity = mock(UserActivity.class);
        when(userActivityRepository.findByUserAndCreatedAt(eq(mockUser), eq(LocalDate.now()))).thenReturn(Optional.of(mockActivity));

        userActivityService.createOrIncrementUserActivity(userId);

        verify(userRepository, times(1)).findById(eq(userId));
        verify(userActivityRepository, times(1)).findByUserAndCreatedAt(eq(mockUser), eq(LocalDate.now()));
        verify(mockActivity, times(1)).addCommitCount();
        verify(userActivityRepository, times(0)).save(any(UserActivity.class));
    }

    @Test
    @DisplayName("createOrIncrementUserActivity - 성공: 활동 기록 존재하지 않음, 새로운 활동 기록 생성")
    void createOrIncrementUserActivityTest_success_createNew() {
        Long userId = 1L;
        User mockUser = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        when(userActivityRepository.findByUserAndCreatedAt(eq(mockUser), eq(LocalDate.now()))).thenReturn(Optional.empty());

        UserActivity newActivity = new UserActivity(mockUser);
        when(userActivityRepository.save(any(UserActivity.class))).thenReturn(newActivity);

        userActivityService.createOrIncrementUserActivity(userId);

        verify(userRepository, times(1)).findById(eq(userId));
        verify(userActivityRepository, times(1)).findByUserAndCreatedAt(eq(mockUser), eq(LocalDate.now()));
        verify(userActivityRepository, times(1)).save(any(UserActivity.class));
    }

    @Test
    @DisplayName("createOrIncrementUserActivity - 실패: 유저 not found")
    void createOrIncrementUserActivityTest_fail_userNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ApiException exception =
                assertThrows(
                        ApiException.class, () ->
                                userActivityService.createOrIncrementUserActivity(userId));

        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());

        verify(userRepository, times(1)).findById(eq(userId));
        verifyNoInteractions(userActivityRepository);
        verifyNoInteractions(userActivityMapper);
    }

}