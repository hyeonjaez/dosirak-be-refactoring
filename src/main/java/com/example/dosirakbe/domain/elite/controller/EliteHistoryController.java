package com.example.dosirakbe.domain.elite.controller;


import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.elite.dto.EliteHistoryDto;
import com.example.dosirakbe.domain.elite.dto.EliteHistoryRequestDto;
import com.example.dosirakbe.domain.elite.dto.EliteHistoryResponseDto;
import com.example.dosirakbe.domain.elite.service.EliteHistoryService;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import org.springframework.http.ResponseEntity;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EliteHistoryController {

    private final EliteHistoryService eliteHistoryService;

    @GetMapping("/elite-history/user")
    public ResponseEntity<ApiResult<List<EliteHistoryResponseDto>>> getEliteHistoryByUserId(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
            ) {
        Long userId = getUserIdByOAuth(customOAuth2User);
        List<EliteHistoryResponseDto> history = eliteHistoryService.findEliteHistoryWithProblemByUserId(userId);
        return ResponseEntity.ok(
                ApiResult.<List<EliteHistoryResponseDto>>builder()
                        .status(StatusEnum.SUCCESS)
                        .message("사용자의 히스토리 조회 성공")
                        .data(history)
                        .build()
        );
    }

    // 문제 풀이 기록 추가 (정답 또는 오답)
    @PostMapping("/elite-history/record")
    public ResponseEntity<ApiResult<Void>> recordAnswer(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestParam Long problemId,
            @RequestParam boolean isCorrect
    ) {
        Long userId = getUserIdByOAuth(customOAuth2User);
        eliteHistoryService.recordAnswer(userId, problemId, isCorrect);

        return ResponseEntity.ok(
                ApiResult.<Void>builder()
                        .status(StatusEnum.SUCCESS)
                        .message("문제 풀이 기록 및 업데이트 성공")
                        .data(null)
                        .build()
        );
    }

    // 맞은 문제 조회
    @GetMapping("/elite-history/user/correct")
    public ResponseEntity<ApiResult<List<EliteHistoryResponseDto>>> getCorrectProblemsByUserId(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Long userId = getUserIdByOAuth(customOAuth2User);
        List<EliteHistoryResponseDto> correctHistory = eliteHistoryService.findCorrectProblemsByUserId(userId);
        return ResponseEntity.ok(
                ApiResult.<List<EliteHistoryResponseDto>>builder()
                        .status(StatusEnum.SUCCESS)
                        .message("맞은 문제 조회 성공")
                        .data(correctHistory)
                        .build()
        );
    }

    // 틀린 문제 조회
    @GetMapping("/elite-history/user/incorrect")
    public ResponseEntity<ApiResult<List<EliteHistoryResponseDto>>> getIncorrectProblemsByUserId(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
            ) {
        Long userId = getUserIdByOAuth(customOAuth2User);
        List<EliteHistoryResponseDto> incorrectHistory = eliteHistoryService.findIncorrectProblemsByUserId(userId);
        return ResponseEntity.ok(
                ApiResult.<List<EliteHistoryResponseDto>>builder()
                        .status(StatusEnum.SUCCESS)
                        .message("틀린 문제 조회 성공")
                        .data(incorrectHistory)
                        .build()
        );
    }

    private Long getUserIdByOAuth(CustomOAuth2User customOAuth2User) {
        return customOAuth2User.getUserDTO().getUserId();
    }

}
