package com.example.dosirakbe.domain.auth.service;

import com.example.dosirakbe.domain.auth.dto.*;
import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.auth.oauth2.CustomRequestEntityConverter;
import com.example.dosirakbe.domain.user.dto.response.UserDTO;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.cglib.core.Local;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public KakaoUserInfo processKakaoToken(String kakaoAccessToken) {
        return getUserInfoFromKakao(kakaoAccessToken);
    }

    private KakaoUserInfo getUserInfoFromKakao(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            return new KakaoUserInfo(response.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new ApiException(ExceptionEnum.INVALID_ACCESS_TOKEN);
            }
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
        }
    }

    public NaverUserInfo processNaverToken(String naverAccessToken) {
        return getUserInfoFromNaver(naverAccessToken);
    }

    private NaverUserInfo getUserInfoFromNaver(String accessToken) {
        String url = "https://openapi.naver.com/v1/nid/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            return new NaverUserInfo(response.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new ApiException(ExceptionEnum.INVALID_ACCESS_TOKEN);
            }
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
        }
    }








}