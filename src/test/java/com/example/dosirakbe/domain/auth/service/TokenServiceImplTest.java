package com.example.dosirakbe.domain.auth.service;

import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.auth.dto.response.TokenResponse;
import com.example.dosirakbe.domain.auth.entity.RefreshToken;
import com.example.dosirakbe.domain.auth.repository.RefreshTokenRepository;
import com.example.dosirakbe.domain.user.dto.response.UserDTO;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import com.example.dosirakbe.global.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TokenServiceImplTest {

    @Autowired
    private TokenServiceImpl tokenService;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Test
    @DisplayName("reissueAccessToken - 성공")
    void reissueAccessToken_Success() {

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);

        User user = new User();
        user.setUserId(1L);
        user.setUserName("testUser");

        when(authentication.getPrincipal()).thenReturn(new CustomOAuth2User(new UserDTO("testName", "testUser", "profileImgUrl", 1L)));

        when(userRepository.findByUserName("testUser")).thenReturn(java.util.Optional.of(user));

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken("validRefreshToken");
        refreshToken.setUser(user);

        when(refreshTokenRepository.findByUser_UserId(1L)).thenReturn(java.util.Optional.of(refreshToken));
        when(jwtUtil.isExpired("validRefreshToken")).thenReturn(false);
        when(jwtUtil.createJwt(eq("testUser"), eq(user.getName()), eq(1L), anyLong())).thenReturn("newAccessToken");

        TokenResponse tokenResponse = tokenService.reissueAccessToken();

        assertNotNull(tokenResponse);
        assertEquals("newAccessToken", tokenResponse.getAccessToken());
        verify(userRepository, times(1)).findByUserName("testUser");
        verify(refreshTokenRepository, times(1)).findByUser_UserId(1L);
        verify(jwtUtil, times(1)).createJwt(eq("testUser"), eq(user.getName()), eq(1L), anyLong());
    }


    @Test
    @DisplayName("reissueAccessToken - 실패 : no authentication")
    void reissueAccessToken_Failure_NoAuthentication() {

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);

        ApiException exception = assertThrows(ApiException.class, () -> tokenService.reissueAccessToken());

        assertEquals(ExceptionEnum.SECURITY, exception.getError());
        verifyNoInteractions(userRepository, refreshTokenRepository, jwtUtil);
    }


    @Test
    @DisplayName("reissueAccessToken - 실패 : 유저 not found")
    void reissueAccessToken_Failure_UserNotFound() {

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);

        User user = new User();
        user.setUserName("nonExistentUser");

        when(authentication.getPrincipal()).thenReturn(new CustomOAuth2User(new UserDTO("testName", "nonExistentUser", "profileImgUrl", 2L)));
        when(userRepository.findByUserName("nonExistentUser")).thenReturn(java.util.Optional.empty());


        ApiException exception = assertThrows(ApiException.class, () -> tokenService.reissueAccessToken());

        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());
        verify(userRepository, times(1)).findByUserName("nonExistentUser");
        verifyNoInteractions(refreshTokenRepository, jwtUtil);
    }

    @Test
    @DisplayName("reissueAccessToken - 실패 : refreshToken 없을때")
    void reissueAccessToken_Failure_RefreshTokenNotFound() {

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        UserDTO userDTO = new UserDTO("testName", "testUser", "profileImgUrl", 1L);
        when(authentication.getPrincipal()).thenReturn(new CustomOAuth2User(userDTO));

        User user = new User();
        user.setUserId(1L);
        user.setUserName("testUser");

        when(userRepository.findByUserName("testUser")).thenReturn(java.util.Optional.of(user));
        when(refreshTokenRepository.findByUser_UserId(1L)).thenReturn(java.util.Optional.empty());


        ApiException exception = assertThrows(ApiException.class, () -> tokenService.reissueAccessToken());

        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());
        verify(refreshTokenRepository, times(1)).findByUser_UserId(1L);
        verifyNoInteractions(jwtUtil);
    }

    @Test
    @DisplayName("reissueAccessToken - 실패 : refreshToken 만료되었을때")
    void reissueAccessToken_Failure_RefreshTokenExpired() {

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        UserDTO userDTO = new UserDTO("testName", "testUser", "profileImgUrl", 1L);
        when(authentication.getPrincipal()).thenReturn(new CustomOAuth2User(userDTO));

        User user = new User();
        user.setUserId(1L);
        user.setUserName("testUser");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken("expiredRefreshToken");
        refreshToken.setUser(user);

        when(userRepository.findByUserName("testUser")).thenReturn(java.util.Optional.of(user));
        when(refreshTokenRepository.findByUser_UserId(1L)).thenReturn(java.util.Optional.of(refreshToken));
        when(jwtUtil.isExpired("expiredRefreshToken")).thenReturn(true);


        ApiException exception = assertThrows(ApiException.class, () -> tokenService.reissueAccessToken());

        assertEquals(ExceptionEnum.ACCESS_DENIED_EXCEPTION, exception.getError());
        verify(jwtUtil, times(1)).isExpired("expiredRefreshToken");
    }
}
