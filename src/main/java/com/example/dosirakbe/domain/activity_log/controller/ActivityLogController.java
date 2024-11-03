package com.example.dosirakbe.domain.activity_log.controller;

import com.example.dosirakbe.domain.activity_log.dto.response.ActivityLogResponse;
import com.example.dosirakbe.domain.activity_log.service.ActivityLogService;
import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activity-logs")
public class ActivityLogController {
    private final ActivityLogService activityLogService;

    @GetMapping("/today")
    public ResponseEntity<ApiResult<List<ActivityLogResponse>>> getTodayActivityLog(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Long userId = getUserIdByOAuth(customOAuth2User);
        List<ActivityLogResponse> activityLogs = activityLogService.getTodayDateActivityLog(userId);

        ApiResult<List<ActivityLogResponse>> result = ApiResult.<List<ActivityLogResponse>>builder()
                .status(StatusEnum.SUCCESS)
                .message("Activity history for today retrieved successfully")
                .data(activityLogs)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/daily/{date}")
    public ResponseEntity<ApiResult<List<ActivityLogResponse>>> getActivityLogForDate(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                                                      @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        Long userId = getUserIdByOAuth(customOAuth2User);
        List<ActivityLogResponse> activityLogs = activityLogService.getThatDateActivityLog(userId, date);

        ApiResult<List<ActivityLogResponse>> result = ApiResult.<List<ActivityLogResponse>>builder()
                .status(StatusEnum.SUCCESS)
                .message("Activity history for that date retrieved successfully")
                .data(activityLogs)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/first-day/{month}")
    public ResponseEntity<ApiResult<List<ActivityLogResponse>>> getActivityLogForFirstDayOfMonth(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                                                                 @PathVariable("month") @DateTimeFormat(pattern = "yyyy-MM") LocalDate month) {

        Long userId = getUserIdByOAuth(customOAuth2User);
        List<ActivityLogResponse> firstDayLogs = activityLogService.getActivityLogForFirstDayOfMonth(userId, month);

        ApiResult<List<ActivityLogResponse>> result = ApiResult.<List<ActivityLogResponse>>builder()
                .status(StatusEnum.SUCCESS)
                .message("Activity history for first date retrieved successfully")
                .data(firstDayLogs)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    private Long getUserIdByOAuth(CustomOAuth2User customOAuth2User) {
        return customOAuth2User.getUserDTO().getUserId();
    }
}
