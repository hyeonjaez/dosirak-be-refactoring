package com.example.dosirakbe.domain.user_activity.controller;

import com.example.dosirakbe.domain.user_activity.dto.response.UserActivityResponse;
import com.example.dosirakbe.domain.user_activity.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-activity")
public class UserActivityController {
    private final UserActivityService userActivityService;

    @GetMapping
    public ResponseEntity<List<UserActivityResponse>> getUserActivity(@RequestParam(required = false) Integer year,
                                                                      @RequestParam(required = false) Integer month) {
        if (isDateParameterValidation(year, month)) {
            //TODO throw
        }

        YearMonth yearMonth;

        Long userId = 1L;
        if (Objects.nonNull(year) && Objects.nonNull(month)) {
            yearMonth = YearMonth.of(year, month);
        } else {
            yearMonth = YearMonth.now();
        }
        List<UserActivityResponse> userActivityList = userActivityService.getUserActivityList(userId, yearMonth);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userActivityList);
    }

    private boolean isDateParameterValidation(Integer year, Integer month) {
        if (0 < year && year < 2025) {
            return false;
        }

        return 0 >= month || month > 12;
    }
}
