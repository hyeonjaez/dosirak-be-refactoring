package com.example.dosirakbe.domain.user.service;

import com.example.dosirakbe.domain.activity_log.repository.ActivityLogRepository;
import com.example.dosirakbe.domain.auth.dto.OAuth2UserInfo;
import com.example.dosirakbe.domain.auth.entity.RefreshToken;
import com.example.dosirakbe.domain.auth.repository.RefreshTokenRepository;
import com.example.dosirakbe.domain.user.dto.response.UserProfileResponse;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.generator.NickNameGenerator;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.domain.user_activity.repository.UserActivityRepository;
import com.example.dosirakbe.domain.user_chat_room.repository.UserChatRoomRepository;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import com.example.dosirakbe.global.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private NickNameGenerator nickNameGenerator;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UserChatRoomRepository userChatRoomRepository;

    @Mock
    private UserActivityRepository userActivityRepository;

    @Mock
    private ActivityLogRepository activityLogRepository;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setNickName("환경");
        mockUser.setEmail("leeyujin1231@naver.com");
        mockUser.setName("이유진");
        mockUser.setProfileImg("https://example.com/profile.jpg");
        mockUser.setCreatedAt(LocalDateTime.now());
        mockUser.setUpdatedAt(LocalDateTime.now());
        mockUser.setUserValid(true);
        Mockito.when(userChatRoomRepository.existsByUser(mockUser)).thenReturn(false);
        Mockito.when(userActivityRepository.existsByUser(mockUser)).thenReturn(false);
        Mockito.when(activityLogRepository.existsByUser(mockUser)).thenReturn(false);
    }

    @Test
    @DisplayName("testRegisterUser - 성공")
    void testRegisterUser_Success() {
        OAuth2UserInfo userInfo = Mockito.mock(OAuth2UserInfo.class);
        Mockito.when(userInfo.getProvider()).thenReturn("kakao");
        Mockito.when(userInfo.getProviderId()).thenReturn("12345");
        Mockito.when(userInfo.getName()).thenReturn("이유진");
        Mockito.when(userInfo.getEmail()).thenReturn("leeyujin1231@naver.com");
        Mockito.when(userInfo.getProfileImg()).thenReturn("https://example.com/profile.jpg");

        Mockito.when(userRepository.findByUserName(any())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(any())).thenReturn(mockUser);
        Mockito.when(jwtUtil.createJwt(any(), any(), any(), any())).thenReturn("mockAccessToken");

        String refreshToken = "mockRefreshToken";
        Mockito.when(refreshTokenRepository.save(any())).thenReturn(new RefreshToken(mockUser, refreshToken));

        var result = userService.registerUser(userInfo);

        assertNotNull(result);
        assertEquals("mockAccessToken", result.get("accessToken"));
        assertNotNull(result.get("refreshToken"));
    }

    @Test
    @DisplayName("testUpdateNickName - 성공")
    void testUpdateNickName_Success() {
        String newNickName = "NewNickName";

        Mockito.when(userRepository.existsByNickName(eq(newNickName))).thenReturn(false);
        Mockito.when(userRepository.findById(eq(1L))).thenReturn(Optional.of(mockUser));
        Mockito.when(userRepository.save(any())).thenReturn(mockUser);

        User result = userService.updateNickName(1L, newNickName);

        assertNotNull(result);
        assertEquals(newNickName, result.getNickName());
    }

    @Test
    @DisplayName("testUpdateNickName - 실패 : 닉네임 중복")
    void testUpdateNickName_DuplicateNickName() {
        String newNickName = "DuplicateNickName";

        Mockito.when(userRepository.existsByNickName(eq(newNickName))).thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> {
            userService.updateNickName(1L, newNickName);
        });

        assertEquals(ExceptionEnum.DUPLICATE_NICKNAME, exception.getError());
    }

    @Test
    @DisplayName("testGetUserProfile - 성공")
    void testGetUserProfile_Success() {
        Mockito.when(userRepository.findById(eq(1L))).thenReturn(Optional.of(mockUser));

        UserProfileResponse result = userService.getUserProfile(1L);

        assertNotNull(result);
        assertEquals("환경", result.getNickName());
        assertEquals("leeyujin1231@naver.com", result.getEmail());
    }

    @Test
    @DisplayName("testGetUserProfile - 실패 : user not found")
    void testGetUserProfile_UserNotFound() {
        Mockito.when(userRepository.findById(eq(1L))).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> {
            userService.getUserProfile(1L);
        });

        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());
    }

    @Test
    @DisplayName("testProcessKakaoLogout - 성공")
    void testProcessKakaoLogout_Success() {
        String accessToken = "mockAccessToken";

        Mockito.when(restTemplate.exchange(eq("https://kapi.kakao.com/v1/user/logout"), eq(HttpMethod.POST), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(null);

        Mockito.doNothing().when(refreshTokenRepository).deleteByUser_UserId(eq(1L));

        assertDoesNotThrow(() -> userService.processKakaoLogout(accessToken, 1L));

        Mockito.verify(restTemplate).exchange(eq("https://kapi.kakao.com/v1/user/logout"), eq(HttpMethod.POST), any(HttpEntity.class), eq(Void.class));
        Mockito.verify(refreshTokenRepository).deleteByUser_UserId(eq(1L));
    }

    @Test
    @DisplayName("testProcessKakaoLogout - 실패")
    void testProcessKakaoLogout_Failure() {
        String accessToken = "mockAccessToken";

        Mockito.when(restTemplate.exchange(eq("https://kapi.kakao.com/v1/user/logout"), eq(HttpMethod.POST), any(HttpEntity.class), eq(Void.class)))
                .thenThrow(new HttpClientErrorException(org.springframework.http.HttpStatus.BAD_REQUEST));

        ApiException exception = assertThrows(ApiException.class, () -> userService.processKakaoLogout(accessToken, 1L));


        assertEquals(ExceptionEnum.RUNTIME_EXCEPTION, exception.getError());

        Mockito.verify(restTemplate).exchange(eq("https://kapi.kakao.com/v1/user/logout"), eq(HttpMethod.POST), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    @DisplayName("testProcessKakaoWithdraw - 성공")
    void testProcessKakaoWithdraw_Success() {
        String accessToken = "mockAccessToken";

        Mockito.when(restTemplate.exchange(eq("https://kapi.kakao.com/v1/user/unlink"), eq(HttpMethod.POST), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(null);

        Mockito.when(userRepository.findById(eq(1L))).thenReturn(Optional.of(mockUser));
        Mockito.when(userRepository.save(any())).thenReturn(mockUser);

        assertDoesNotThrow(() -> userService.processKakaoWithdraw(accessToken, 1L));

        Mockito.verify(restTemplate).exchange(eq("https://kapi.kakao.com/v1/user/unlink"), eq(HttpMethod.POST), any(HttpEntity.class), eq(Void.class));
        Mockito.verify(userRepository).save(any(User.class));
        Mockito.verify(refreshTokenRepository).deleteByUser_UserId(eq(1L));
    }

    @Test
    @DisplayName("testProcessKakaoWithdraw - 실패 : user not found")
    void testProcessKakaoWithdraw_UserNotFound() {
        String accessToken = "mockAccessToken";

        Mockito.when(restTemplate.exchange(eq("https://kapi.kakao.com/v1/user/unlink"), eq(HttpMethod.POST), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(null);

        Mockito.when(userRepository.findById(eq(1L))).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> userService.processKakaoWithdraw(accessToken, 1L));

        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());

        Mockito.verify(userRepository).findById(eq(1L));
    }

    @Test
    @DisplayName("카카오 탈퇴 처리 실패 테스트 - HTTP ERROR")
    void testProcessKakaoWithdraw_Failure() {
        String accessToken = "mockAccessToken";

        Mockito.when(restTemplate.exchange(eq("https://kapi.kakao.com/v1/user/unlink"), eq(HttpMethod.POST), any(HttpEntity.class), eq(Void.class)))
                .thenThrow(new HttpClientErrorException(org.springframework.http.HttpStatus.BAD_REQUEST));

        ApiException exception = assertThrows(ApiException.class, () -> userService.processKakaoWithdraw(accessToken, 1L));

        assertEquals(ExceptionEnum.RUNTIME_EXCEPTION, exception.getError());

        Mockito.verify(restTemplate).exchange(eq("https://kapi.kakao.com/v1/user/unlink"), eq(HttpMethod.POST), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    @DisplayName("testGenerateNickname - 성공")
    void testGenerateNickname_Success() throws Exception {
        String generatedNickName = "UniqueNickname";

        Mockito.when(nickNameGenerator.getNickName()).thenReturn(generatedNickName);
        Mockito.when(userRepository.existsByNickName(generatedNickName)).thenReturn(false);

        Method method = userService.getClass().getDeclaredMethod("generateNickname");
        method.setAccessible(true);

        String result = (String) method.invoke(userService);

        assertEquals(generatedNickName, result);

        Mockito.verify(nickNameGenerator, Mockito.times(1)).getNickName();
        Mockito.verify(userRepository, Mockito.times(1)).existsByNickName(generatedNickName);
    }


    @Test
    @DisplayName("testGenerateNickname - 실패 :중복된 닉네임")
    void testGenerateNickname_Failure() throws Exception {

        Mockito.when(nickNameGenerator.getNickName()).thenReturn("DuplicateNickname");
        Mockito.when(userRepository.existsByNickName("DuplicateNickname")).thenReturn(true);


        Method method = userService.getClass().getDeclaredMethod("generateNickname");
        method.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            method.invoke(userService);
        });

        Throwable cause = exception.getCause();
        assertTrue(cause instanceof IllegalArgumentException);
        assertEquals("중복되지 않는 닉네임을 생성하는데 실패했습니다.", cause.getMessage());


        Mockito.verify(nickNameGenerator, Mockito.times(5)).getNickName();
        Mockito.verify(userRepository, Mockito.times(5)).existsByNickName("DuplicateNickname");
    }

    @Test
    @DisplayName("testGetRefreshToken - 성공 : 기존 토큰 유효")
    void testGetRefreshToken_ExistingTokenValid() throws Exception {
        RefreshToken existingToken = new RefreshToken(mockUser, "existingValidToken");

        Mockito.when(refreshTokenRepository.findByUser_UserId(eq(1L))).thenReturn(Optional.of(existingToken));
        Mockito.when(jwtUtil.validToken("existingValidToken")).thenReturn(true);

        Method method = userService.getClass().getDeclaredMethod("getRefreshToken", User.class);
        method.setAccessible(true);

        String result = (String) method.invoke(userService, mockUser);

        assertEquals("existingValidToken", result);

        Mockito.verify(refreshTokenRepository).findByUser_UserId(eq(1L));
        Mockito.verify(jwtUtil).validToken("existingValidToken");
    }

    @Test
    @DisplayName("testGetRefreshToken - 성공 : 기존 토큰 만료")
    void testGetRefreshToken_ExistingTokenExpired() throws Exception {
        RefreshToken existingToken = new RefreshToken(mockUser, "expiredToken");
        String newRefreshToken = "newValidToken";

        Mockito.when(refreshTokenRepository.findByUser_UserId(eq(1L))).thenReturn(Optional.of(existingToken));
        Mockito.when(jwtUtil.validToken("expiredToken")).thenReturn(false);
        Mockito.when(jwtUtil.createJwt(any(), any(), any(), any())).thenReturn(newRefreshToken);


        Method method = userService.getClass().getDeclaredMethod("getRefreshToken", User.class);
        method.setAccessible(true);

        String result = (String) method.invoke(userService, mockUser);

        assertEquals(newRefreshToken, result);

        Mockito.verify(refreshTokenRepository).findByUser_UserId(eq(1L));
        Mockito.verify(jwtUtil).validToken("expiredToken");
        Mockito.verify(refreshTokenRepository).save(existingToken);
    }

    @Test
    @DisplayName("testGetRefreshToken - 성공 : 신규사용자")
    void testGetRefreshToken_NewUser() throws Exception {
        String newRefreshToken = "newValidToken";

        Mockito.when(refreshTokenRepository.findByUser_UserId(eq(1L))).thenReturn(Optional.empty());
        Mockito.when(jwtUtil.createJwt(any(), any(), any(), any())).thenReturn(newRefreshToken);

        Method method = userService.getClass().getDeclaredMethod("getRefreshToken", User.class);
        method.setAccessible(true);

        String result = (String) method.invoke(userService, mockUser);

        assertEquals(newRefreshToken, result);

        Mockito.verify(refreshTokenRepository).findByUser_UserId(eq(1L));
        Mockito.verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("testGetRefreshToken - 실패 : jwt생성 오류")
    void testGetRefreshToken_Failure_JWTError() throws Exception {
        Mockito.when(refreshTokenRepository.findByUser_UserId(eq(1L))).thenReturn(Optional.empty());
        Mockito.when(jwtUtil.createJwt(any(), any(), any(), any()))
                .thenThrow(new RuntimeException("JWT 생성 중 오류 발생"));


        Method method = userService.getClass().getDeclaredMethod("getRefreshToken", User.class);
        method.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            method.invoke(userService, mockUser);
        });

        Throwable cause = exception.getCause();
        assertTrue(cause instanceof RuntimeException);
        assertEquals("JWT 생성 중 오류 발생", cause.getMessage());

        Mockito.verify(refreshTokenRepository).findByUser_UserId(eq(1L));
        Mockito.verify(jwtUtil).createJwt(any(), any(), any(), any());
    }



}