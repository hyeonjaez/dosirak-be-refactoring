package com.example.dosirakbe.domain.user.controller;

import com.example.dosirakbe.domain.auth.dto.KakaoUserInfo;
import com.example.dosirakbe.domain.auth.dto.NaverUserInfo;
import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.auth.service.CustomOAuth2UserService;
import com.example.dosirakbe.domain.user.dto.request.NickNameRequest;
import com.example.dosirakbe.domain.user.dto.request.TrackRewardRequest;
import com.example.dosirakbe.domain.user.dto.response.UserProfileResponse;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.domain.user.service.UserService;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.ExceptionEnum;
import com.example.dosirakbe.global.util.StatusEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * packageName    : com.example.dosirakbe.domain.user.controller<br>
 * fileName       : UserController<br>
 * author         : yyujin1231<br>
 * date           : 10/20/24<br>
 * description    : 유저 정보 관련 CRUD controller 클래스 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/20/24        yyujin1231                최초 생성<br>
 */



@RestController
@RequiredArgsConstructor
public class UserController {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * 소셜 인증을 처리하고 JWT 토큰을 반환합니다.
     *
     * @param socialAccessToken 소셜 제공자에서 전달받은 액세스 토큰
     * @return JWT 액세스 및 리프레시 토큰이 포함된 응답
     */

    @PostMapping("/api/users")
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

    /**
     * 사용자의 소셜 로그아웃을 처리합니다.
     *
     * @param socialAccessToken 소셜 제공자에서 전달받은 액세스 토큰
     * @return 로그아웃 성공 여부에 대한 응답
     */

    @PostMapping("/api/users/logout")
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

    /**
     * 사용자 탈퇴를 처리합니다.
     *
     * @param socialAccessToken 소셜 제공자에서 전달받은 액세스 토큰
     * @return 탈퇴 성공 여부에 대한 응답
     */

    @PostMapping("/api/users/withdraw")
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

    /**
     * 소셜 인증 토큰이 카카오 토큰인지 확인합니다.
     *
     * @param accessToken 소셜 제공자로부터 전달받은 액세스 토큰
     * @return 카카오 사용자 정보가 포함된 Optional 객체. 유효하지 않은 토큰일 경우 빈 Optional 반환
     */



    private Optional<KakaoUserInfo> isKakaoToken(String accessToken) {
        try {
            return Optional.of(customOAuth2UserService.processKakaoToken(accessToken));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * 소셜 인증 토큰이 네이버 토큰인지 확인합니다.
     *
     * @param accessToken 소셜 제공자로부터 전달받은 액세스 토큰
     * @return 네이버 사용자 정보가 포함된 Optional 객체. 유효하지 않은 토큰일 경우 빈 Optional 반환
     */

    private Optional<NaverUserInfo> isNaverToken(String accessToken) {
        try {
            return Optional.of(customOAuth2UserService.processNaverToken(accessToken));
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    /**
     * 새로운 닉네임을 생성하여 사용자 정보를 업데이트합니다.
     *
     * @param customOAuth2User 현재 인증된 사용자 정보
     * @param nickNameRequest 요청으로 전달받은 닉네임 정보
     * @return 닉네임 생성 결과를 포함한 응답
     */

    @PostMapping("/api/users/nickname")
    public ResponseEntity<?> generateNickname(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                              @Valid @RequestBody NickNameRequest nickNameRequest) {

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

    /**
     * 닉네임의 중복 여부를 확인합니다.
     *
     * @param nickname 확인하려는 닉네임
     * @return 닉네임 중복 여부를 나타내는 응답
     */

    @GetMapping("/api/users/check/nickname")
    public ResponseEntity<?> checkNicknName(@RequestParam("nickname") String nickname) {
        boolean exists = userRepository.existsByNickName(nickname);
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

    /**
     * 현재 사용자 프로필 정보를 조회합니다.
     *
     * @param customOAuth2User 인증된 사용자 정보
     * @return 사용자 프로필 정보를 포함한 응답
     */

    @GetMapping("/api/users/profile")
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

    /**
     * 사용자 닉네임을 업데이트합니다.
     *
     * @param customOAuth2User 인증된 사용자 정보
     * @param nickNameRequest 업데이트할 닉네임 정보
     * @return 닉네임 업데이트 결과를 포함한 응답
     */
    @PutMapping("/api/users/nickname")
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

    /**
     * 이동 거리를 기반으로 리워드 포인트를 추가합니다.
     *
     * @param customOAuth2User 인증된 사용자 정보
     * @param trackRewardRequest 이동 거리 정보를 포함한 요청
     * @return 리워드 추가 결과 메시지
     */

    @PostMapping("api/rewards/track")
    public ResponseEntity<String> addReward(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody TrackRewardRequest trackRewardRequest) {

        try {
            Long userId = customOAuth2User.getUserDTO().getUserId();
            userService.addRewardPointsByTrack(userId, trackRewardRequest.getDistance());
            return ResponseEntity.ok("리워드 포인트가 성공적으로 추가되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("잘못된 입력값입니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리워드 포인트 추가 중 예상치 못한 오류가 발생했습니다.");
        }
    }

    /**
     * 인증 기반으로 리워드를 추가합니다.
     *
     * @param customOAuth2User 인증된 사용자 정보
     * @return 리워드 추가 결과 메시지
     */

    @PostMapping("api/rewards/dosirak")
    public ResponseEntity<String> addReward(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        Long userId = customOAuth2User.getUserDTO().getUserId();
        userService.addRewardPointsByAuth(userId);
        return ResponseEntity.ok("리워드 포인트가 성공적으로 추가되었습니다.");

    }

    @GetMapping("/api/test/user")
    public ResponseEntity<String> getUserId(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok("현재 사용자 ID: " + customOAuth2User.getUserDTO().getUserId() +
                ", 이름: " + customOAuth2User.getUserDTO().getName());
    }


}