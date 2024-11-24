package com.example.dosirakbe.domain.user_activity.controller;

import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.chat_room.dto.response.UserChatRoomBriefParticipationResponse;
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

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-activity")
public class UserActivityController {
    private final UserActivityService userActivityService;

    @GetMapping("/monthly")
    public ResponseEntity<ApiResult<List<UserActivityResponse>>> getMonthlyActivitySummary(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                                                           @RequestParam(value = "month", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        Long userId = getUserIdByOAuth(customOAuth2User);

        YearMonth targetMonth = (month != null) ? month : YearMonth.now();

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

    private Long getUserIdByOAuth(CustomOAuth2User customOAuth2User) {
        return customOAuth2User.getUserDTO().getUserId();
    }
}
