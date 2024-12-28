package com.example.dosirakbe.domain.rank.controller;

import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.rank.dto.response.RankResponse;
import com.example.dosirakbe.domain.rank.service.RankService;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


/**
 * packageName    : com.example.dosirakbe.domain.rank.controller<br>
 * fileName       : RankController<br>
 * author         : yyujin1231<br>
 * date           : 11/15/24<br>
 * description    : 사용자 랭킹 제공 관련 CRUD controller 클래스 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/15/24        yyujin1231                최초 생성<br>
 */


@RestController
@RequestMapping("/api/users")
public class RankController {

    @Autowired
    private RankService rankService;

    /**
     * 전체 사용자 랭킹을 반환합니다.
     *
     * <p>
     * 이 메서드는 모든 사용자의 랭킹 정보를 조회하여 반환합니다.
     * </p>
     *
     * @return 전체 사용자 랭킹을 포함하는 {@link ApiResult} 형태의 {@link List} 객체
     */

    @GetMapping("/rank")
    public ResponseEntity<ApiResult<List<RankResponse>>> getAllRanks() {
        List<RankResponse> ranks = rankService.getRankedUsers();
        return ResponseEntity.ok(
                ApiResult.<List<RankResponse>>builder()
                        .status(StatusEnum.SUCCESS)
                        .message("전체 랭킹 반환")
                        .data(ranks)
                        .build()
        );
    }

    /**
     * 특정 사용자의 랭킹을 반환합니다.
     *
     * <p>
     * 이 메서드는 인증된 사용자의 ID를 기반으로 해당 사용자의 랭킹을 반환합니다.
     * </p>
     *
     * @param customOAuth2User 인증된 사용자의 정보를 포함하는 {@link CustomOAuth2User} 객체
     * @return 특정 사용자의 랭킹 정보를 포함하는 {@link ApiResult} 형태의 {@link RankResponse} 객체
     */

    @GetMapping("/me/rank")
    public ResponseEntity<ApiResult<RankResponse>> getRankByUserId(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Long userId = customOAuth2User.getUserDTO().getUserId();
        RankResponse rank = rankService.getRankByUserId(userId);

        return ResponseEntity.ok(
                ApiResult.<RankResponse>builder()
                        .status(StatusEnum.SUCCESS)
                        .message("사용자의 랭킹 반환")
                        .data(rank)
                        .build()
        );
    }
}