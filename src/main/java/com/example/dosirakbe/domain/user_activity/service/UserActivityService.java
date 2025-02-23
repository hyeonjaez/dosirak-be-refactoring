package com.example.dosirakbe.domain.user_activity.service;

import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.implement.UserReader;
import com.example.dosirakbe.domain.user_activity.dto.mapper.UserActivityMapper;
import com.example.dosirakbe.domain.user_activity.dto.response.UserActivityResponse;
import com.example.dosirakbe.domain.user_activity.entity.UserActivity;
import com.example.dosirakbe.domain.user_activity.implement.UserActivityReader;
import com.example.dosirakbe.domain.user_activity.implement.UserActivityWriter;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * packageName    : com.example.dosirakbe.domain.user_activity.service<br>
 * fileName       : UserActivityService<br>
 * author         : Fiat_lux<br>
 * date           : 11/03/24<br>
 * description    : 사용자 활동 기록과 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/03/24        Fiat_lux                최초 생성<br>
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserActivityService {
    private final UserActivityReader userActivityReader;
    private final UserActivityWriter userActivityWriter;
    private final UserReader userReader;
    private final UserActivityMapper userActivityMapper;

    /**
     * 특정 사용자의 지정된 월에 대한 활동 목록을 조회하여 {@link UserActivityResponse} Response DTO 리스트로 반환합니다.
     *
     * <p>
     * 이 메서드는 사용자의 ID와 조회할 월 정보를 기반으로 해당 사용자의 활동 기록을 조회합니다.
     * 지정된 월의 첫째 날과 마지막 날 사이의 활동을 생성일 기준 오름차순으로 정렬하여 반환합니다.
     * </p>
     *
     * @param userId 사용자의 고유 식별자 {@link Long}
     * @param month  조회할 월을 나타내는 {@link YearMonth} 객체 (선택 사항)
     * @return 조회된 사용자 활동을 포함하는 {@link List} 형태의 {@link UserActivityResponse} 객체 리스트
     * @throws ApiException {@link ExceptionEnum#DATA_NOT_FOUND} 예외 발생 시
     */
    public List<UserActivityResponse> getUserActivityList(Long userId, YearMonth month) {
        User user = userReader.findByUserIdButThrow(userId);

        LocalDate firstDay = month.atDay(1);
        LocalDate lastDay = month.atEndOfMonth();

        List<UserActivity> userActivitiesByDate = userActivityReader.getUserActivitiesByDate(user, firstDay, lastDay);

        return userActivityMapper.mapToUserActivityResponseList(userActivitiesByDate);
    }

    /**
     * 특정 사용자의 오늘 날짜에 대한 활동 기록을 생성하거나 기존 기록을 증가시킵니다.
     *
     * <p>
     * 이 메서드는 사용자의 ID를 기반으로 오늘 날짜에 대한 활동 기록을 조회합니다.
     * 만약 활동 기록이 존재하면 {@link UserActivity#addCommitCount()} 메서드를 통해 커밋 수를 증가시키고,
     * 존재하지 않으면 새로운 {@link UserActivity} 엔티티를 생성하여 저장합니다.
     * </p>
     *
     * @param userId 사용자의 고유 식별자 {@link Long}
     * @throws ApiException {@link ExceptionEnum#DATA_NOT_FOUND} 예외 발생 시
     */
    @Transactional
    public void createOrIncrementUserActivity(Long userId) {
        User user = userReader.findByUserIdButThrow(userId);

        userActivityReader.findTodayActivityByUser(user)
                .ifPresentOrElse(userActivityWriter::addCommitUserActivity,
                        () -> userActivityWriter.createUserActivity(new UserActivity(user))
                );
    }


}
