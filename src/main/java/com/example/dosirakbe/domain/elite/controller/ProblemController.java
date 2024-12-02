package com.example.dosirakbe.domain.elite.controller;

import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.elite.dto.ProblemDto;
import com.example.dosirakbe.domain.elite.service.ProblemService;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProblemController {

    private final ProblemService problemService;

    // 사용자(userId)가 풀지 않은 문제 랜덤 조회
    @GetMapping("/problems/random")
    public ResponseEntity<ApiResult<ProblemDto>> getRandomProblemNotSolvedByUser(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Long userId = getUserIdByOAuth(customOAuth2User);
        ProblemDto problem = problemService.getRandomProblemNotSolvedByUser(userId);

        if (problem == null) {
            return ResponseEntity.ok(
                    ApiResult.<ProblemDto>builder()
                            .status(StatusEnum.SUCCESS)
                            .message("사용자가 풀지 않은 문제가 없습니다.")
                            .data(null)
                            .build()
            );
        }

        return ResponseEntity.ok(
                ApiResult.<ProblemDto>builder()
                        .status(StatusEnum.SUCCESS)
                        .message("사용자가 풀지 않은 문제 랜덤 조회 성공")
                        .data(problem)
                        .build()
        );
    }

    // ProblemId로 문제 조회
    @GetMapping("/problems/{problemId}")
    public ResponseEntity<ApiResult<ProblemDto>> getProblemById(@PathVariable Long problemId) {
        ProblemDto problem = problemService.findProblemById(problemId);

        if (problem == null) {
            return ResponseEntity.status(404).body(
                    ApiResult.<ProblemDto>builder()
                            .status(StatusEnum.FAILURE)
                            .message("해당 문제를 찾을 수 없습니다.")
                            .data(null)
                            .build()
            );
        }

        return ResponseEntity.ok(
                ApiResult.<ProblemDto>builder()
                        .status(StatusEnum.SUCCESS)
                        .message("문제 조회 성공")
                        .data(problem)
                        .build()
        );
    }

    private Long getUserIdByOAuth(CustomOAuth2User customOAuth2User) {
        return customOAuth2User.getUserDTO().getUserId();
    }

}