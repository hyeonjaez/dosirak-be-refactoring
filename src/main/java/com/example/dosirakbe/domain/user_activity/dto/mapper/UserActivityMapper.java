package com.example.dosirakbe.domain.user_activity.dto.mapper;

import com.example.dosirakbe.domain.user_activity.dto.response.UserActivityResponse;
import com.example.dosirakbe.domain.user_activity.entity.UserActivity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * packageName    : com.example.dosirakbe.domain.user_activity.dto.mapper<br>
 * fileName       : UserActivityMapper<br>
 * author         : Fiat_lux<br>
 * date           : 11/02/24<br>
 * description    : 사용자 활동 엔티티와 DTO 간의 매핑을 담당하는 매퍼 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/02/24        Fiat_lux                최초 생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserActivityMapper {

    /**
     * {@link UserActivity} 엔티티를 {@link UserActivityResponse} Response DTO 로 변환합니다.
     *
     * <p>
     * 이 메서드는 {@link UserActivity} 엔티티의 데이터를 {@link UserActivityResponse} Response DTO 로 매핑하여 반환합니다.
     * </p>
     *
     * @param userActivity 매핑할 {@link UserActivity} 엔티티 객체
     * @return 매핑된 {@link UserActivityResponse} Response DTO 객체
     */
    UserActivityResponse mapToUserActivityResponse(UserActivity userActivity);

    /**
     * {@link UserActivity} 엔티티 리스트를 {@link UserActivityResponse} Response DTO 리스트로 변환합니다.
     *
     * <p>
     * 이 메서드는 {@link UserActivity} 엔티티의 리스트를 {@link UserActivityResponse} Response DTO 의 리스트로 매핑하여 반환합니다.
     * </p>
     *
     * @param userActivityList 매핑할 {@link UserActivity} 엔티티 객체들의 리스트
     * @return 매핑된 {@link UserActivityResponse} Response DTO 객체들의 리스트
     */
    List<UserActivityResponse> mapToUserActivityResponseList(List<UserActivity> userActivityList);
}
