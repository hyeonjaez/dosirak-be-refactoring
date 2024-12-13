package com.example.dosirakbe.domain.activity_log.dto.mapper;

import com.example.dosirakbe.domain.activity_log.dto.response.ActivityLogResponse;
import com.example.dosirakbe.domain.activity_log.entity.ActivityLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * packageName    : com.example.dosirakbe.domain.activity_log.dto.mapper<br>
 * fileName       : ActivityLogMapper<br>
 * author         : Fiat_lux<br>
 * date           : 11/03/24<br>
 * description    : 활동 로그 엔티티와 응답 DTO 간의 매핑을 담당하는 매퍼 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/03/24        Fiat_lux                최초 생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ActivityLogMapper {

    /**
     * {@link ActivityLog} 엔티티를 {@link ActivityLogResponse} Response DTO 로 매핑합니다.
     *
     * <p>
     * 이 메서드는 활동 로그 엔티티의 생성 시각을 포맷하고, 활동 메시지를 생성하며,
     * 활동 유형의 아이콘 이미지 URL 을 설정하여 응답 DTO 로 변환합니다.
     * </p>
     *
     * @param activityLog 매핑할 {@link ActivityLog} 엔티티
     * @return 매핑된 {@link ActivityLogResponse} Response DTO
     */
    @Mapping(source = "createdAt", target = "createAtTime", qualifiedByName = "formatDateTime")
    @Mapping(target = "activityMessage", source = "activityLog", qualifiedByName = "generateActivityMessage")
    @Mapping(source = "activityType.iconImageUrl", target = "iconImageUrl")
    ActivityLogResponse mapToActivityLogResponse(ActivityLog activityLog);

    /**
     * 여러 개의 {@link ActivityLog} 엔티티를 {@link ActivityLogResponse} Response DTO 리스트로 매핑합니다.
     *
     * <p>
     * 이 메서드는 활동 로그 엔티티 리스트를 순회하면서 각각을 {@link ActivityLogResponse} Response DTO 로 변환하여 리스트로 반환합니다.
     * </p>
     *
     * @param activityLogs 매핑할 {@link ActivityLog} 엔티티 리스트
     * @return 매핑된 {@link ActivityLogResponse} Response DTO 리스트
     */
    List<ActivityLogResponse> mapToActivityLogResponseList(List<ActivityLog> activityLogs);

    /**
     * {@link LocalDateTime} 객체를 특정 형식의 문자열로 포맷합니다.
     *
     * <p>
     * 이 메서드는 활동 로그의 생성 시각을 "HH시 mm분" 형식의 문자열로 변환합니다.
     * </p>
     *
     * @param createdAt 포맷할 {@link LocalDateTime} 객체
     * @return 포맷된 시각 문자열
     */
    @Named("formatDateTime")
    default String formatDateTime(LocalDateTime createdAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH시 mm분");
        return createdAt.format(formatter);
    }

    /**
     * {@link ActivityLog} 엔티티로부터 활동 메시지를 생성합니다.
     *
     * <p>
     * 이 메서드는 활동 로그의 활동 유형과 거리를 기반으로 활동 메시지를 생성합니다.
     * </p>
     *
     * @param activityLog 활동 메시지를 생성할 {@link ActivityLog} 엔티티
     * @return 생성된 활동 메시지 문자열
     */
    @Named("generateActivityMessage")
    default String generateActivityMessage(ActivityLog activityLog) {
        return activityLog.getActivityType().generateMessage(activityLog.getDistance());
    }
}
