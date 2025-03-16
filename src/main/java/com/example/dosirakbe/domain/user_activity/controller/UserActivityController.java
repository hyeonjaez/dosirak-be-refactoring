package com.example.dosirakbe.domain.user_activity.controller;

import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.user_activity.dto.response.UserActivityResponse;
import com.example.dosirakbe.domain.user_activity.service.UserActivityService;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

/**
 * packageName    : com.example.dosirakbe.domain.user_activity.controller<br>
 * fileName       : UserActivityController<br>
 * author         : Fiat_lux<br>
 * date           : 11/02/24<br>
 * description    : 사용자 활동 관련 API 요청을 처리하는 컨트롤러 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/02/24        Fiat_lux                최초 생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-activities")
public class UserActivityController {
    private final UserActivityService userActivityService;

    /**
     * 특정 사용자의 월간 활동 요약을 조회하여 반환합니다.
     *
     * <p>
     * 이 메서드는 인증된 사용자의 ID와 선택적인 월 정보를 기반으로 사용자 활동 로그를 조회합니다.
     * 월 정보가 제공되지 않은 경우 현재 월의 활동 로그를 반환합니다.
     * </p>
     *
     * @param customOAuth2User 인증된 사용자의 정보를 포함하는 {@link CustomOAuth2User} 객체
     * @param month            조회할 월을 나타내는 {@link YearMonth} 객체 (형식: yyyy-MM, 선택 사항)
     * @return 월간 활동 요약을 포함하는 {@link ApiResult} 형태의 {@link List} 객체
     */
    @GetMapping("/monthly")
    public ResponseEntity<ApiResult<List<UserActivityResponse>>> getMonthlyActivitySummary(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                                                           @RequestParam(value = "month", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {

        Long userId = getUserIdByOAuth(customOAuth2User);

        YearMonth targetMonth = Objects.nonNull(month) ? month : YearMonth.now();

        List<UserActivityResponse> monthlySummary = userActivityService.getUserActivityList(userId, targetMonth);

        ApiResult<List<UserActivityResponse>> result = ApiResult.<List<UserActivityResponse>>builder()
                .status(StatusEnum.SUCCESS)
                .message("Monthly Activity summary retrieved successfully")
                .data(monthlySummary)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    /**
     * 인증된 사용자의 고유 식별자를 추출합니다.
     *
     * <p>
     * 이 메서드는 {@link CustomOAuth2User} 객체에서 사용자의 고유 ID를 추출하여 반환합니다.
     * </p>
     *
     * @param customOAuth2User 인증된 사용자의 정보를 포함하는 {@link CustomOAuth2User} 객체
     * @return 사용자의 고유 식별자 {@link Long} 값
     */
    private Long getUserIdByOAuth(CustomOAuth2User customOAuth2User) {
        return customOAuth2User.getUserDTO().getUserId();
    }
}
