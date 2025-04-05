package com.example.dosirakbe.domain.activity_log.controller;

import com.example.dosirakbe.domain.activity_log.service.ActivityLogService;
import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.global.util.ResponseEntityUtil;
import com.example.dosirakbe.global.util.UserUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * packageName    : com.example.dosirakbe.domain.activity_log.controller<br>
 * fileName       : ActivityLogController<br>
 * author         : Fiat_lux<br>
 * date           : 11/03/24<br>
 * description    :  활동 로그 관련 API 요청을 처리하는 컨트롤러 클래스입니다.
 * <p>
 * 인증된 사용자의 활동 기록을 조회하는 API를 제공합니다.
 * 날짜별 조회, 월별 첫째 날 조회 기능을 포함하고 있으며,
 * {@link ActivityLogService}를 통해 실제 비즈니스 로직을 위임받아 처리합니다.
 * </p>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/03/24        Fiat_lux                최초 생성<br>
 * 12/20/24        Fiat_lux                api 수정<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activity-logs")
public class ActivityLogController {
    private final ActivityLogService activityLogService;

    /**
     * 인증된 사용자의 특정 날짜의 활동 로그를 조회합니다.
     *
     * <p>
     * 날짜 파라미터가 제공되지 않으면 오늘 날짜를 기준으로 조회합니다.
     * </p>
     *
     * @param customOAuth2User 인증된 사용자 정보 (Spring Security OAuth2 기반)
     * @param date             조회할 날짜 (yyyy-MM-dd 형식, 선택값)
     * @return 조회된 활동 로그 정보를 담은 {@link ResponseEntity} 객체
     */
    @GetMapping
    public ResponseEntity<?> getActivityLogByDate(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                 @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = UserUtility.getUserIdByOAuth(customOAuth2User);

        return ResponseEntityUtil.successOk("Activity history for today retrieved successfully", activityLogService.getThatDateActivityLog(userId, date));
    }

    /**
     * 인증된 사용자의 특정 월의 첫째 날에 해당하는 활동 로그를 조회합니다.
     *
     * <p>
     * 예를 들어 2024년 4월이 요청되면, 2024-04-01 하루 동안의 활동 로그를 반환합니다.
     * </p>
     *
     * @param customOAuth2User 인증된 사용자 정보 (Spring Security OAuth2 기반)
     * @param month            조회할 월 (yyyy-MM 형식)
     * @return 조회된 활동 로그 정보를 담은 {@link ResponseEntity} 객체
     */
    @GetMapping("/first-day/{month}")
    public ResponseEntity<?> getActivityLogForFirstDayOfMonth(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                              @PathVariable("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        Long userId = UserUtility.getUserIdByOAuth(customOAuth2User);

        return ResponseEntityUtil.successOk("Activity history for first date retrieved successfully", activityLogService.getActivityLogForFirstDayOfMonth(userId, month));
    }
}