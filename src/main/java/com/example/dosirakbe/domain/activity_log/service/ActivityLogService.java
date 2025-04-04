package com.example.dosirakbe.domain.activity_log.service;

import com.example.dosirakbe.domain.activity_log.dto.mapper.ActivityLogMapper;
import com.example.dosirakbe.domain.activity_log.dto.response.ActivityLogResponse;
import com.example.dosirakbe.domain.activity_log.entity.ActivityLog;
import com.example.dosirakbe.domain.activity_log.entity.ActivityType;
import com.example.dosirakbe.domain.activity_log.repository.ActivityLogRepository;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

/**
 * packageName    : com.example.dosirakbe.domain.activity_log.service<br>
 * fileName       : ActivityLogService<br>
 * author         : Fiat_lux<br>
 * date           : 11/03/24<br>
 * description    : 활동 로그 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/03/24        Fiat_lux                최초 생성<br>
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityLogService {
    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;
    private final ActivityLogMapper activityLogMapper;

    /**
     * 사용자의 지정된 날짜에 해당하는 활동 로그를 조회합니다.
     *
     * <p>
     * 이 메서드는 주어진 사용자 ID에 해당하는 {@link User} 엔티티를 조회하고,
     * 지정된 날짜의 시작 시각과 종료 시각 사이에 생성된 모든 {@link ActivityLog} 엔티티를
     * 조회하여 {@link ActivityLogResponse} DTO 리스트로 변환하여 반환합니다.
     * </p>
     *
     * @param userId 조회할 활동 로그의 소유자 사용자 ID
     * @param thatDay  조회할 날짜
     * @return 지정된 날짜의 활동 로그를 포함한 {@link ActivityLogResponse} DTO 리스트
     * @throws ApiException {@link ExceptionEnum#DATA_NOT_FOUND} 예외 발생 시
     */
    public List<ActivityLogResponse> getThatDateActivityLog(Long userId, LocalDate thatDay) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
        if (Objects.isNull(thatDay)) {
            thatDay = LocalDate.now();
        }
        LocalDateTime startOfDay = thatDay.atStartOfDay();
        LocalDateTime endOfDay = thatDay.atTime(LocalTime.MAX);

        List<ActivityLog> activityLogList = activityLogRepository.findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(user, startOfDay, endOfDay);
        return activityLogMapper.mapToActivityLogResponseList(activityLogList);
    }

    /**
     * 선택한 월의 첫째 날에 해당하는 활동 로그를 조회합니다.
     *
     * <p>
     * 이 메서드는 주어진 사용자 ID에 해당하는 {@link User} 엔티티를 조회하고,
     * 지정된 월의 첫째 날의 시작 시각과 종료 시각 사이에 생성된 모든 {@link ActivityLog} 엔티티를
     * 조회하여 {@link ActivityLogResponse} DTO 리스트로 변환하여 반환합니다.
     * </p>
     *
     * @param userId 조회할 활동 로그의 소유자 사용자 ID
     * @param month  조회할 월
     * @return 선택한 월의 첫째 날의 활동 로그를 포함한 {@link ActivityLogResponse} DTO 리스트
     * @throws ApiException {@link ExceptionEnum#DATA_NOT_FOUND} 예외 발생 시
     */
    public List<ActivityLogResponse> getActivityLogForFirstDayOfMonth(Long userId, YearMonth month) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

        LocalDate firstDayOfMonth = month.atDay(1);
        LocalDateTime startOfDay = firstDayOfMonth.atStartOfDay();
        LocalDateTime endOfDay = firstDayOfMonth.atTime(LocalTime.MAX);

        List<ActivityLog> activityLogList = activityLogRepository.findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(user, startOfDay, endOfDay);

        return activityLogMapper.mapToActivityLogResponseList(activityLogList);
    }

    /**
     * 새로운 활동 로그를 추가합니다.
     *
     * <p>
     * 이 메서드는 주어진 사용자 ID에 해당하는 {@link User} 엔티티를 조회하고,
     * 활동 유형에 따라 적절한 {@link ActivityLog} 엔티티를 생성한 후 저장합니다.
     * {@link ActivityType#LOW_CARBON_MEANS_OF_TRANSPORTATION}인 경우, 이동 거리도 함께 저장됩니다.
     * </p>
     *
     * @param userId       활동 로그를 추가할 사용자 ID
     * @param contentId    활동 로그와 관련된 콘텐츠 ID
     * @param activityType 활동의 유형 {@link ActivityType}
     * @param distance     활동 중 이동한 거리 (선택 사항)
     * @throws ApiException {@link ExceptionEnum#DATA_NOT_FOUND} 또는 {@link ExceptionEnum#INVALID_REQUEST} 예외 발생 시
     */
    @Transactional
    public void addActivityLog(Long userId, Long contentId, ActivityType activityType, BigDecimal distance) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

        ActivityLog activityLog;
        if (activityType.equals(ActivityType.LOW_CARBON_MEANS_OF_TRANSPORTATION)) {
            if (Objects.isNull(distance) || distance.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ApiException(ExceptionEnum.INVALID_REQUEST);
            }
            activityLog = new ActivityLog(contentId, user, activityType, distance);
        } else {
            activityLog = new ActivityLog(contentId, user, activityType);
        }

        activityLogRepository.save(activityLog);

    }


}
