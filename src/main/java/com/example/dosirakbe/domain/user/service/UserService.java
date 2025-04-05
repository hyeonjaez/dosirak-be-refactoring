//package com.example.dosirakbe.domain.user.service;
//
//import com.example.dosirakbe.domain.activity_log.repository.ActivityLogRepository;
//import com.example.dosirakbe.domain.auth.dto.OAuth2UserInfo;
//import com.example.dosirakbe.domain.auth.entity.RefreshToken;
//import com.example.dosirakbe.domain.auth.repository.RefreshTokenRepository;
//import com.example.dosirakbe.domain.user.dto.response.UserProfileResponse;
//import com.example.dosirakbe.domain.user.entity.User;
//import com.example.dosirakbe.domain.user.generator.NickNameGenerator;
//import com.example.dosirakbe.domain.user.repository.UserRepository;
//import com.example.dosirakbe.domain.user_activity.repository.UserActivityRepository;
//import com.example.dosirakbe.domain.user_chat_room.repository.UserChatRoomRepository;
//import com.example.dosirakbe.global.util.JwtUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//
///**
// * packageName    : com.example.dosirakbe.domain.user.service<br>
// * fileName       : UserService<br>
// * author         : yyujin1231<br>
// * date           : 10/20/24<br>
// * description    : 사용자 관리와 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.<br>
// * ===========================================================<br>
// * DATE              AUTHOR             NOTE<br>
// * -----------------------------------------------------------<br>
// * 10/20/24        yyujin1231                최초 생성<br>
// */
//
//
//
//@Service
//@RequiredArgsConstructor
//public class UserService {
//
//    private final UserRepository userRepository;
//    private final NickNameGenerator nickNameGenerator;
//    private final JwtUtil jwtUtil;
//    private final RefreshTokenRepository refreshTokenRepository;
//    private final RestTemplate restTemplate;
//    private final UserChatRoomRepository userChatRoomRepository;
//    private final UserActivityRepository userActivityRepository;
//    private final ActivityLogRepository activityLogRepository;
//
//    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
//    private String clientId;
//    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
//    private String clientSecret;
//    public static final String DELETE_USER_NICKNAME = "(알 수 없음)";
//    public static final String DELETE_USER_IMAGE = "https://dosirakbucket.s3.ap-northeast-2.amazonaws.com/aeb4b5c6-8766-4280-96ee-93dd2cb1baf5.png";
//
//    /**
//     * <p> 이 메서드는 사용자를 등록하고, JWT 액세스 토큰과 리프레시 토큰을 생성합니다.</p>
//     *
//     * @param oAuth2UserInfo OAuth2 제공자로부터 받은 사용자 정보
//     * @return 생성된 액세스 토큰과 리프레시 토큰을 포함하는 Map 객체
//     */
//
//    @Transactional
//    public Map<String, String> registerUser(OAuth2UserInfo oAuth2UserInfo) {
//        String userName = oAuth2UserInfo.getProvider() + " " + oAuth2UserInfo.getProviderId();
//        User user = userRepository.findByUserName(userName).orElseGet(() -> {
//            User newUser = new User();
//            newUser.setUserName(userName);
//            newUser.setName(oAuth2UserInfo.getName());
//            newUser.setEmail(oAuth2UserInfo.getEmail());
//            newUser.setProfileImg(oAuth2UserInfo.getProfileImg());
//            newUser.setCreatedAt(LocalDateTime.now());
//            newUser.setUpdatedAt(LocalDateTime.now());
//            newUser.setUserValid(true);
//            return userRepository.save(newUser);
//        });
//
//        long accessTokenExpiration = 24 * 60 * 60 * 1000L;
//        String accessToken = jwtUtil.createJwt(user.getUserName(), user.getName(), user.getUserId(), accessTokenExpiration);
//
//
//        String refreshToken = getRefreshToken(user);
//
//        Map<String, String> tokens = new HashMap<>();
//        tokens.put("accessToken", accessToken);
//        tokens.put("refreshToken", refreshToken);
//
//        return tokens;
//    }
//
//    /**
//     * <p>이 메서드는 카카오 사용자를 로그아웃 처리합니다.</p>
//     *
//     * @param accessToken 카카오 액세스 토큰
//     * @param userId 사용자 ID
//     * @throws ApiException {@link ExceptionEnum#RUNTIME_EXCEPTION} 로그아웃 처리 중 오류 발생 시
//     */
//
//    @Transactional
//    public void processKakaoLogout(String accessToken, Long userId) {
//        String url = "https://kapi.kakao.com/v1/user/logout";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        try {
//            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
//            refreshTokenRepository.deleteByUser_UserId(userId);
//        } catch (HttpClientErrorException e) {
//            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
//        }
//    }
//
//    /**
//     * <p>이 메서드는 카카오 사용자를 탈퇴 처리합니다.</p>
//     *
//     * <p>
//     * Soft Delete 방식을 사용하여 데이터를 물리적으로 삭제하지 않고,
//     * 사용자 계정을 비활성화하고 닉네임과 프로필 이미지를 변경합니다.
//     * </p>
//     *
//     * @param accessToken 카카오 액세스 토큰
//     * @param userId 사용자 ID
//     * @throws ApiException {@link ExceptionEnum#DATA_NOT_FOUND} 사용자 또는 탈퇴 요청이 유효하지 않을 경우
//     */
//
//    @Transactional
//    public void processKakaoWithdraw(String accessToken, Long userId) {
//        String url = "https://kapi.kakao.com/v1/user/unlink";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        try {
//            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
//
//            user.setUserValid(false);
//            user.setUserName("delete " + user.getUserName());
//            user.setNickName(DELETE_USER_NICKNAME);
//            user.setProfileImg(DELETE_USER_IMAGE);
//            userRepository.save(user);
//
//
//            deleteByUser(user);
//            refreshTokenRepository.deleteByUser_UserId(userId);
//
//
//        } catch (HttpClientErrorException e) {
//            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
//        }
//    }
//
//    /**
//     * <p>이 메서드는 카카오 사용자를 로그아웃 처리합니다.</p>
//     *
//     * @param accessToken 네이버 액세스 토큰
//     * @param userId 사용자 ID
//     */
//
//
//
//    @Transactional
//    public void processNaverLogout(String accessToken, Long userId) {
//        refreshTokenRepository.deleteByUser_UserId(userId);
//    }
//
//
//    /**
//     * <p>이 메서드는 네이버 사용자를 탈퇴 처리합니다.</p>
//     *
//     * <p>
//     * Soft Delete 방식을 사용하여 데이터를 물리적으로 삭제하지 않고,
//     * 사용자 계정을 비활성화하고 닉네임과 프로필 이미지를 변경합니다.
//     * </p>
//     *
//     * @param accessToken 네이버 액세스 토큰
//     * @param userId 사용자 ID
//     * @throws ApiException {@link ExceptionEnum#DATA_NOT_FOUND} 탈퇴 요청이 유효하지 않을 경우
//     */
//
//    @Transactional
//    public void processNaverWithdraw(String accessToken, Long userId) {
//        String url = "https://nid.naver.com/oauth2.0/token";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "delete");
//        params.add("client_id", clientId);
//        params.add("client_secret", clientSecret);
//        params.add("access_token", accessToken);
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//
//        try {
//            restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
//
//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
//
//            user.setUserValid(false);
//            user.setUserName("delete " + user.getUserName());
//            user.setNickName(DELETE_USER_NICKNAME);
//            user.setProfileImg(DELETE_USER_IMAGE);
//            userRepository.save(user);
//
//            deleteByUser(user);
//            refreshTokenRepository.deleteByUser_UserId(userId);
//
//        } catch (HttpClientErrorException e) {
//            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
//        }
//    }
//
//    /**
//     * <p>이 메서드는 사용자의 닉네임을 업데이트합니다.</p>
//     *
//     * @param userId 사용자 ID
//     * @param nickName 새 닉네임
//     * @return 업데이트된 사용자 객체
//     * @throws ApiException {@link ExceptionEnum#DUPLICATE_NICKNAME} 닉네임이 중복된 경우
//     * @throws ApiException {@link ExceptionEnum#DATA_NOT_FOUND} 사용자를 찾을 수 없는 경우
//     */
//
//    public User updateNickName(Long userId, String nickName) {
//        if (nickName == null || nickName.trim().isEmpty()) {
//            nickName = generateNickname();
//        }
//
//        if (userRepository.existsByNickName(nickName)) {
//            throw new ApiException(ExceptionEnum.DUPLICATE_NICKNAME);
//        }
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
//
//        user.setNickName(nickName);
//        user.setUpdatedAt(LocalDateTime.now());
//        userRepository.save(user);
//        return user;
//    }
//
//    /**
//     * <p>이 메서드는 새 닉네임을 자동 생성합니다.</p>
//     *
//     * @return 생성된 닉네임
//     * @throws IllegalArgumentException 닉네임 생성에 실패한 경우
//     */
//
//    private String generateNickname() {
//        for (int i = 0; i < 5; i++) {
//            String generatedNickName = nickNameGenerator.getNickName();
//            if (!userRepository.existsByNickName(generatedNickName)) {
//                return generatedNickName;
//            }
//        }
//        throw new IllegalArgumentException("중복되지 않는 닉네임을 생성하는데 실패했습니다.");
//    }
//
//    /**
//     * <p>이 메서드는 사용자의 리프레시 토큰을 가져옵니다. 만료된 경우 새로 생성합니다.</p>
//     *
//     * @param user 사용자 객체
//     * @return 리프레시 토큰
//     */
//
//
//    private String getRefreshToken(User user) {
//        return refreshTokenRepository.findByUser_UserId(user.getUserId())
//                .map(existingToken -> {
//                    if (jwtUtil.validToken(existingToken.getRefreshToken())) {
//                        return existingToken.getRefreshToken();
//                    } else {
//                        String newRefreshToken = createNewRefreshToken(user);
//                        existingToken.setRefreshToken(newRefreshToken);
//                        refreshTokenRepository.save(existingToken);
//                        return newRefreshToken;
//                    }
//                })
//                .orElseGet(() -> {
//                    String newRefreshToken = createNewRefreshToken(user);
//                    RefreshToken newTokenEntity = new RefreshToken(user, newRefreshToken);
//                    refreshTokenRepository.save(newTokenEntity);
//                    return newRefreshToken;
//                });
//    }
//
//    /**
//     * <p>새로운 리프레시 토큰을 생성합니다.</p>
//     *
//     * @param user 사용자 객체
//     * @return 생성된 리프레시 토큰 문자열
//     */
//
//    private String createNewRefreshToken(User user) {
//        long refreshTokenExpiration = 7 * 24 * 60 * 60 * 1000L;
//        return jwtUtil.createJwt(user.getUserName(), user.getName(), user.getUserId(), refreshTokenExpiration);
//    }
//
//    /**
//     * <p>이 메서드는 사용자의 프로필 정보를 가져옵니다.</p>
//     *
//     * @param userId 사용자 ID
//     * @return {@link UserProfileResponse} 객체
//     */
//
//
//    public UserProfileResponse getUserProfile(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
//
//        return new UserProfileResponse(
//                user.getNickName(),
//                user.getEmail(),
//                user.getName(),
//                user.getCreatedAt(),
//                user.getReward()
//        );
//    }
//
//    /**
//     * <p>사용자의 연관 데이터를 삭제합니다.</p>
//     *
//     * @param user 삭제할 사용자 객체
//     */
//
//    private void deleteByUser(User user) {
//        if (userChatRoomRepository.existsByUser(user)) {
//            userChatRoomRepository.deleteByUser(user);
//        }
//
//        if (userActivityRepository.existsByUser(user)) {
//            userActivityRepository.deleteByUser(user);
//        }
//
//        if (activityLogRepository.existsByUser(user)) {
//            activityLogRepository.deleteByUser(user);
//        }
//    }
//
//
//    /**
//     * <p>이 메서드는 이동 거리 기반 리워드 포인트를 추가합니다.</p>
//     *
//     * @param userId 사용자 ID
//     * @param distance 이동 거리
//     */
//
//
//    @Transactional
//    public void addRewardPointsByTrack(Long userId, double distance){
//        int result = (int) Math.round(distance * 7 );
//        userRepository.updateReward(userId, result);
//    }
//
//
//    /**
//     * <p>이 메서드는 인증 기반 리워드 포인트를 추가합니다.</p>
//     *
//     * @param userId 사용자 ID
//     */
//
//    @Transactional
//    public void addRewardPointsByAuth(Long userId) {
//        userRepository.updateReward(userId,10);
//    }
//
//
//}
