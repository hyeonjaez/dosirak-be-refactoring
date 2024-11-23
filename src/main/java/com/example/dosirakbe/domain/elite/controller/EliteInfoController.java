package com.example.dosirakbe.domain.elite.controller;

import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.elite.dto.EliteInfoDto;
import com.example.dosirakbe.domain.elite.service.EliteInfoService;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EliteInfoController {

    private final EliteInfoService eliteInfoService;

    @GetMapping("/elite-info/user")
    public ResponseEntity<ApiResult<EliteInfoDto>> getEliteInfoByUserId(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
            ) {

        Long userId = getUserIdByOAuth(customOAuth2User);
        Optional<EliteInfoDto> eliteInfo = eliteInfoService.findEliteInfoByUserId(userId);
        if (eliteInfo.isPresent()) {
            return ResponseEntity.ok(
                    ApiResult.<EliteInfoDto>builder()
                            .status(StatusEnum.SUCCESS)
                            .message("사용자 정보 조회 성공")
                            .data(eliteInfo.get())
                            .build()
            );
        } else {
            return ResponseEntity.status(404).body(
                    ApiResult.<EliteInfoDto>builder()
                            .status(StatusEnum.ERROR)
                            .message("해당 사용자 정보를 찾을 수 없습니다")
                            .data(null)
                            .build()
            );
        }
    }

    private Long getUserIdByOAuth(CustomOAuth2User customOAuth2User) {
        return customOAuth2User.getUserDTO().getUserId();
    }

}
