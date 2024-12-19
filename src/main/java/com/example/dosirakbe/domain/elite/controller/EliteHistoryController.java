package com.example.dosirakbe.domain.elite.controller;

import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.elite.dto.EliteHistoryResponseDto;
import com.example.dosirakbe.domain.elite.service.EliteHistoryService;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

/**
 * packageName    : com.example.dosirakbe.domain.elite.controller<br>
 * fileName       : EliteHistoryController<br>
 * author         : femmefatlaehaein<br>
 * date           : 11/23/24<br>
 * description    : 사용자의 문제 풀이 기록과 관련된 기능을 제공하는 Controller 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/23/24        femmefatlaehaein       최초 생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EliteHistoryController {

    private final EliteHistoryService eliteHistoryService;

    /**
     * 사용자의 전체 문제 풀이 기록을 조회합니다.
     *
     * @param customOAuth2User 인증된 사용자 정보를 포함한 객체
     * @return 사용자의 전체 문제 풀이 기록 목록
     */
    @GetMapping("/elite-histories/user")
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

    /**
     * 사용자의 문제 풀이 기록을 추가합니다.
     *
     * @param customOAuth2User 인증된 사용자 정보를 포함한 객체
     * @param problemId 기록할 문제의 ID
     * @param isCorrect 문제 풀이 결과 (정답 여부: true/false)
     * @return 작업 성공 여부
     */
    @PostMapping("/elite-histories/record")
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

    /**
     * 사용자가 맞힌 문제 목록을 조회합니다.
     *
     * @param customOAuth2User 인증된 사용자 정보를 포함한 객체
     * @return 사용자가 맞힌 문제 기록 목록
     */
    @GetMapping("/elite-histories/user/correct")
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

    /**
     * 사용자가 틀린 문제 목록을 조회합니다.
     *
     * @param customOAuth2User 인증된 사용자 정보를 포함한 객체
     * @return 사용자가 틀린 문제 기록 목록
     */
    @GetMapping("/elite-histories/user/incorrect")
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

    /**
     * OAuth 인증 정보를 통해 사용자 ID를 가져옵니다.
     *
     * @param customOAuth2User 인증된 사용자 정보를 포함한 객체
     * @return 사용자 ID
     */
    private Long getUserIdByOAuth(CustomOAuth2User customOAuth2User) {
        return customOAuth2User.getUserDTO().getUserId();
    }

}
