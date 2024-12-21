package com.example.dosirakbe.domain.elite.controller;

import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.elite.dto.ProblemDto;
import com.example.dosirakbe.domain.elite.service.ProblemService;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.StatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.example.dosirakbe.domain.elite.controller<br>
 * fileName       : ProblemController<br>
 * author         : femmefatlaehaein<br>
 * date           : 11/23/24<br>
 * description    : 문제와 관련된 API를 제공하는 Controller 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/23/24        femmefatlaehaein       최초 생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProblemController {

    private final ProblemService problemService;

    /**
     * 사용자가 아직 풀지 않은 문제를 랜덤으로 조회합니다.<br>
     * 인증된 사용자 정보를 기반으로 해당 사용자가 풀지 않은 문제 중 하나를 반환합니다.<br>
     *
     * @param customOAuth2User 인증된 사용자 정보를 포함한 객체
     * @return 사용자가 풀지 않은 문제(ProblemDto)를 포함한 응답<br>
     *         - 성공: HTTP 200 응답과 함께 랜덤 문제 반환<br>
     *         - 실패: HTTP 200 응답과 함께 "풀지 않은 문제가 없습니다." 메시지 반환
     */
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

    /**
     * 문제 ID를 기준으로 특정 문제를 조회합니다.<br>
     * 주어진 Problem ID를 사용하여 해당 문제를 검색합니다.<br>
     *
     * @param problemId 조회할 문제의 ID
     * @return 문제(ProblemDto)를 포함한 응답<br>
     *         - 성공: HTTP 200 응답과 함께 문제 반환<br>
     *         - 실패: HTTP 404 응답과 함께 "해당 문제를 찾을 수 없습니다." 메시지 반환
     */
    @GetMapping("/problems/{problemId}")
    public ResponseEntity<ApiResult<ProblemDto>> getProblemById(
            @PathVariable @NotNull(message = "문제 ID는 null일 수 없습니다.") Long problemId
    ) {
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

    /**
     * OAuth 인증 정보를 사용하여 사용자 ID를 가져옵니다.<br>
     *
     * @param customOAuth2User 인증된 사용자 정보를 포함한 객체
     * @return 인증된 사용자의 ID
     */
    private Long getUserIdByOAuth(CustomOAuth2User customOAuth2User) {
        return customOAuth2User.getUserDTO().getUserId();
    }

}
