package com.example.dosirakbe.domain.user.controller;

import com.example.dosirakbe.domain.auth.dto.AppleUserInfo;
import com.example.dosirakbe.domain.auth.dto.KakaoUserInfo;
import com.example.dosirakbe.domain.auth.dto.NaverUserInfo;
import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.auth.service.CustomOAuth2UserService;
import com.example.dosirakbe.domain.user.dto.request.NickNameRequest;
import com.example.dosirakbe.domain.user.dto.response.UserProfileResponse;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.domain.user.service.UserService;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.ExceptionEnum;
import com.example.dosirakbe.global.util.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/api/user/register")
    public ResponseEntity<ApiResult<Map<String, String>>> authenticateUser(@RequestHeader("Authorization") String socialAccessToken) {
        try {
            if (socialAccessToken.startsWith("Bearer ")) {
                socialAccessToken = socialAccessToken.substring(7);
            }

            Map<String, String> tokens;

            if (isKakaoToken(socialAccessToken).isPresent()) {
                KakaoUserInfo kakaoUserInfo = customOAuth2UserService.processKakaoToken(socialAccessToken);
                tokens = userService.registerUser(kakaoUserInfo);
            } else if (isNaverToken(socialAccessToken).isPresent()) {
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

    @PostMapping("/api/user/logout")
    public ResponseEntity<ApiResult<Map<String, String>>> logoutUser(@RequestHeader("Authorization") String socialAccessToken) {
        if (socialAccessToken.startsWith("Bearer ")) {
            socialAccessToken = socialAccessToken.substring(7);
        }

        try {
            String userName;
            Long userId;

            if (isKakaoToken(socialAccessToken).isPresent()) {
                KakaoUserInfo kakaoUserInfo = isKakaoToken(socialAccessToken).get();
                userName = kakaoUserInfo.getProvider() + " " + kakaoUserInfo.getProviderId();
                userId = userRepository.findByUserName(userName)
                        .map(User::getUserId)
                        .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
                userService.processKakaoLogout(socialAccessToken, userId);

            } else if (isNaverToken(socialAccessToken).isPresent()) {
                NaverUserInfo naverUserInfo = isNaverToken(socialAccessToken).get();
                userName = naverUserInfo.getProvider() + " " + naverUserInfo.getProviderId();
                userId = userRepository.findByUserName(userName)
                        .map(User::getUserId)
                        .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
                userService.processNaverLogout(socialAccessToken, userId);

            } else {
                throw new ApiException(ExceptionEnum.INVALID_ACCESS_TOKEN);
            }

            ApiResult<Map<String, String>> apiResult = ApiResult.<Map<String, String>>builder()
                    .status(StatusEnum.SUCCESS)
                    .message("로그아웃 성공")
                    .data(null)
                    .build();
            return ResponseEntity.ok(apiResult);

        } catch (Exception e) {
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
        }
    }

    @PostMapping("/api/user/withdraw")
    public ResponseEntity<ApiResult<Map<String, String>>> withdrawUser(@RequestHeader("Authorization") String socialAccessToken) {
        if (socialAccessToken.startsWith("Bearer ")) {
            socialAccessToken = socialAccessToken.substring(7);
        }

        try {
            String userName;
            Long userId;

            if (isKakaoToken(socialAccessToken).isPresent()) {
                KakaoUserInfo kakaoUserInfo = isKakaoToken(socialAccessToken).get();
                userName = kakaoUserInfo.getProvider() + " " + kakaoUserInfo.getProviderId();
                userId = userRepository.findByUserName(userName)
                        .map(User::getUserId)
                        .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

                userService.processKakaoWithdraw(socialAccessToken, userId);

            } else if (isNaverToken(socialAccessToken).isPresent()) {
                NaverUserInfo naverUserInfo = isNaverToken(socialAccessToken).get();
                userName = naverUserInfo.getProvider() + " " + naverUserInfo.getProviderId();
                userId = userRepository.findByUserName(userName)
                        .map(User::getUserId)
                        .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

                userService.processNaverWithdraw(socialAccessToken, userId);

            } else {
                ApiResult<Map<String, String>> apiResult = ApiResult.<Map<String, String>>builder()
                        .status(StatusEnum.FAILURE)
                        .message("유효하지 않은 소셜 토큰입니다.")
                        .data(null)
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResult);
            }

            ApiResult<Map<String, String>> apiResult = ApiResult.<Map<String, String>>builder()
                    .status(StatusEnum.SUCCESS)
                    .message("회원 탈퇴가 완료되었습니다.")
                    .data(null)
                    .build();
            return ResponseEntity.ok(apiResult);

        } catch (Exception e) {
            ApiResult<Map<String, String>> apiResult = ApiResult.<Map<String, String>>builder()
                    .status(StatusEnum.FAILURE)
                    .message("회원 탈퇴 처리 중 오류가 발생했습니다.")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResult);
        }
    }

    private Optional<KakaoUserInfo> isKakaoToken(String accessToken) {
        try {
            return Optional.of(customOAuth2UserService.processKakaoToken(accessToken));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<NaverUserInfo> isNaverToken(String accessToken) {
        try {
            return Optional.of(customOAuth2UserService.processNaverToken(accessToken));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<AppleUserInfo> isAppleToken(String accessToken) {
        try {
            return Optional.of(customOAuth2UserService.processAppleToken(accessToken));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    //회원가입시사용
    @PostMapping("/api/user/nickName")
    public ResponseEntity<?> generateNickname(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                              @RequestBody NickNameRequest nickNameRequest) {

        Long userId = customOAuth2User.getUserDTO().getUserId();

        try {
            User updatedUser = userService.updateNickName(userId, nickNameRequest.getNickName());

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

    //닉네임 중복확인
    @GetMapping("/api/user/check-nickName")
    public ResponseEntity<?> checkNicknName(@RequestParam("nickName") String nickName) {
        boolean exists = userRepository.existsByNickName(nickName);
        if (exists) {
            ApiResult result = ApiResult.builder()
                    .status(StatusEnum.FAILURE)
                    .message("이미 사용중인 닉네임이에요")
                    .exception(null)
                    .build();
            return ResponseEntity.badRequest().body(result);
        }

        ApiResult result = ApiResult.builder()
                .status(StatusEnum.SUCCESS)
                .message("사용 가능한 닉네임이에요")
                .exception(null)
                .build();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/user/mypage/profile")
    public ResponseEntity<ApiResult<UserProfileResponse>> getUserProfile(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Long userId = customOAuth2User.getUserDTO().getUserId();
        UserProfileResponse userProfileResponse = userService.getUserProfile(userId);

        ApiResult<UserProfileResponse> result = ApiResult.<UserProfileResponse>builder()
                .status(StatusEnum.SUCCESS)
                .message("프로필 정보를 반환하였습니다")
                .data(userProfileResponse)
                .exception(null)
                .build();

        return ResponseEntity.ok(result);
    }

    @PutMapping("/api/mypage/nickName")
    public ResponseEntity<ApiResult> updateNickname(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                    @RequestBody NickNameRequest nickNameRequest) {
        Long userId = customOAuth2User.getUserDTO().getUserId();

        try {
            User updatedUser = userService.updateNickName(userId, nickNameRequest.getNickName());

            Map<String, String> responseData = new HashMap<>();
            responseData.put("nickName", updatedUser.getNickName());

            ApiResult<Map<String, String>> result = ApiResult.<Map<String, String>>builder()
                    .status(StatusEnum.SUCCESS)
                    .message("닉네임이 성공적으로 변경되었습니다.")
                    .data(responseData)
                    .exception(null)
                    .build();

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            ApiResult result = ApiResult.builder()
                    .status(StatusEnum.FAILURE)
                    .message("중복된 닉네임입니다.")
                    .exception(null)
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

}
