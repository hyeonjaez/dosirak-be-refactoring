package com.example.dosirakbe.domain.auth.service;

import com.example.dosirakbe.domain.auth.dto.KakaoUserInfo;
import com.example.dosirakbe.domain.auth.dto.NaverUserInfo;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomOAuth2UserServiceTest {

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    @DisplayName("processKakaoToken - 성공")
    void processKakaoToken_Success() {

        String kakaoAccessToken = "validToken";
        String url = "https://kapi.kakao.com/v2/user/me";

        Map<String, Object> kakaoResponse = new HashMap<>();
        kakaoResponse.put("id", 12345L);
        Map<String, Object> properties = new HashMap<>();
        properties.put("nickname", "testNickname");
        kakaoResponse.put("properties", properties);

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(kakaoResponse, HttpStatus.OK);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(kakaoAccessToken);

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(responseEntity);

        KakaoUserInfo userInfo = customOAuth2UserService.processKakaoToken(kakaoAccessToken);

        assertNotNull(userInfo);
        assertEquals(12345L, Long.valueOf(userInfo.getProviderId()));
        assertEquals("testNickname", userInfo.getName());
        verify(restTemplate, times(1)).exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class));
    }

    @Test
    @DisplayName("processKakaoToken - 실패: unauthorized")
    void processKakaoToken_Failure_Unauthorized() {

        String kakaoAccessToken = "invalidToken";
        String url = "https://kapi.kakao.com/v2/user/me";

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));

        ApiException exception = assertThrows(ApiException.class, () ->
                customOAuth2UserService.processKakaoToken(kakaoAccessToken));

        assertEquals(ExceptionEnum.INVALID_ACCESS_TOKEN, exception.getError());
    }

    @Test
    @DisplayName("processNaverToken - 성공")
    void processNaverToken_Success() {
        String naverAccessToken = "validToken";
        String url = "https://openapi.naver.com/v1/nid/me";

        Map<String, Object> naverResponse = new HashMap<>();
        naverResponse.put("response", Map.of("id", "testId", "name", "testUser"));

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(naverResponse, HttpStatus.OK);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(naverAccessToken);

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(responseEntity);

        NaverUserInfo userInfo = customOAuth2UserService.processNaverToken(naverAccessToken);

        assertNotNull(userInfo);
        assertEquals("testId", userInfo.getProviderId());
        assertEquals("testUser", userInfo.getName());
        verify(restTemplate, times(1)).exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class));
    }

    @Test
    @DisplayName("processNaverToken - 실패: unauthorized")
    void processNaverToken_Failure_Unauthorized() {
        String naverAccessToken = "invalidToken";
        String url = "https://openapi.naver.com/v1/nid/me";

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));

        ApiException exception = assertThrows(ApiException.class, () ->
                customOAuth2UserService.processNaverToken(naverAccessToken));

        assertEquals(ExceptionEnum.INVALID_ACCESS_TOKEN, exception.getError());
    }

    @Test
    @DisplayName("processKakaoToken - 실패: runtime exception")
    void processKakaoToken_Failure_RuntimeException() {

        String kakaoAccessToken = "validToken";
        String url = "https://kapi.kakao.com/v2/user/me";

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(new RuntimeException("Unexpected Error"));

        ApiException exception = assertThrows(ApiException.class, () -> {
            try {
                customOAuth2UserService.processKakaoToken(kakaoAccessToken);
            } catch (RuntimeException e) {
                throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
            }
        });

        assertEquals(ExceptionEnum.RUNTIME_EXCEPTION, exception.getError());
    }

    @Test
    @DisplayName("processNaverToken - 실패: runtime exception")
    void processNaverToken_Failure_RuntimeException() {

        String naverAccessToken = "validToken";
        String url = "https://openapi.naver.com/v1/nid/me";

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(new RuntimeException("Unexpected Error"));


        ApiException exception = assertThrows(ApiException.class, () -> {
            try {
                customOAuth2UserService.processNaverToken(naverAccessToken);
            } catch (RuntimeException e) {
                throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
            }
        });

        assertEquals(ExceptionEnum.RUNTIME_EXCEPTION, exception.getError());
    }
}
