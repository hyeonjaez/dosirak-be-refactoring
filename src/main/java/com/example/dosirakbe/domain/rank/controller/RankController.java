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

@RestController
@RequestMapping("/api/users")
public class RankController {

    @Autowired
    private RankService rankService;

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