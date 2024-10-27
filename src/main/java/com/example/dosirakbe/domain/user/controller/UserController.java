package com.example.dosirakbe.domain.user.controller;

import com.example.dosirakbe.domain.auth.dto.KakaoUserInfo;
import com.example.dosirakbe.domain.auth.dto.NaverUserInfo;
import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.auth.service.CustomOAuth2UserService;
import com.example.dosirakbe.domain.user.dto.request.NickNameRequest;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.service.UserService;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserService userService;

    @PostMapping("/api/user/register")
    public ResponseEntity<ApiResult<Map<String, String>>> authenticateUser(@RequestHeader("Authorization") String socialAccessToken) {
        try {
            if (socialAccessToken.startsWith("Bearer ")) {
                socialAccessToken = socialAccessToken.substring(7);
            }

            Map<String, String> tokens;

            if (isKakaoToken(socialAccessToken)) {
                KakaoUserInfo kakaoUserInfo = customOAuth2UserService.processKakaoToken(socialAccessToken);
                tokens = userService.registerUser(kakaoUserInfo);
            } else if (isNaverToken(socialAccessToken)) {
                NaverUserInfo naverUserInfo = customOAuth2UserService.processNaverToken(socialAccessToken);
                tokens = userService.registerUser(naverUserInfo);
            } else {
                ApiResult<Map<String, String>> apiResult = ApiResult.<Map<String, String>>builder()
                        .status(StatusEnum.FAILURE)
                        .message("틀린 혹은 만료된 토큰입니다")
                        .data(null)
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResult);
            }

            ApiResult<Map<String, String>> apiResult = ApiResult.<Map<String, String>>builder()
                    .status(StatusEnum.SUCCESS)
                    .message("소셜 로그인 성공")
                    .data(tokens)
                    .build();

            return ResponseEntity.ok(apiResult);

        } catch (IllegalArgumentException e) {
            ApiResult<Map<String, String>> apiResult = ApiResult.<Map<String, String>>builder()
                    .status(StatusEnum.FAILURE)
                    .message(e.getMessage())
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResult);
        }
    }

    private boolean isKakaoToken(String accessToken) {
        try {
            customOAuth2UserService.processKakaoToken(accessToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isNaverToken(String accessToken) {
        try {
            customOAuth2UserService.processNaverToken(accessToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @PostMapping("/api/user/nickName")
    public ResponseEntity<?> generateNickname(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                              @RequestBody NickNameRequest nickNameRequest) {

        Long userId = customOAuth2User.getUserDTO().getUserId();

        try {
            User updatedUser = userService.updateNickname(userId, nickNameRequest.getNickName());

            Map<String, String> responseData = new HashMap<>();
            responseData.put("nickName", updatedUser.getNickName());

            ApiResult result = ApiResult.builder()
                    .status(StatusEnum.SUCCESS)
                    .message("닉네임이 성공적으로 저장되었습니다.")
                    .data(responseData)
                    .exception(null)
                    .build();
            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {

            ApiResult result = ApiResult.builder()
                    .status(StatusEnum.FAILURE)
                    .message("닉네임이 중복됩니다.")
                    .exception(null)
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }
}
