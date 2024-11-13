package com.example.dosirakbe.domain.user.service;

import com.example.dosirakbe.domain.auth.dto.OAuth2UserInfo;
import com.example.dosirakbe.domain.auth.entity.RefreshToken;
import com.example.dosirakbe.domain.auth.repository.RefreshTokenRepository;
import com.example.dosirakbe.domain.user.dto.response.UserProfileResponse;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.generator.NickNameGenerator;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import com.example.dosirakbe.global.util.JwtUtil;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final NickNameGenerator nickNameGenerator;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    public Map<String, String> registerUser(OAuth2UserInfo oAuth2UserInfo) {
        String userName = oAuth2UserInfo.getProvider() + " " + oAuth2UserInfo.getProviderId();
        User user = userRepository.findByUserName(userName).orElseGet(() -> {
            User newUser = new User();
            newUser.setUserName(userName);
            newUser.setName(oAuth2UserInfo.getName());
            newUser.setEmail(oAuth2UserInfo.getEmail());
            newUser.setProfileImg(oAuth2UserInfo.getProfileImg());
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setUpdatedAt(LocalDateTime.now());
            newUser.setUserValid(true);
            return userRepository.save(newUser);
        });

        long accessTokenExpiration = 24 * 60 * 60 * 1000L;
        String accessToken = jwtUtil.createJwt(user.getUserName(), user.getName(), user.getUserId(), accessTokenExpiration);


        String refreshToken = getRefreshToken(user);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    @Transactional
    public void processKakaoLogout(String accessToken, Long userId) {
        String url = "https://kapi.kakao.com/v1/user/logout";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
            refreshTokenRepository.deleteByUser_UserId(userId);
        } catch (HttpClientErrorException e) {
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
        }
    }

    @Transactional
    public void processKakaoWithdraw(String accessToken, Long userId) {
        String url = "https://kapi.kakao.com/v1/user/unlink";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

            String newUserName = "delete " + user.getUserName();

            user.setUserValid(false);
            user.setUserName(newUserName);
            userRepository.save(user);

            refreshTokenRepository.deleteByUser_UserId(userId);

        } catch (HttpClientErrorException e) {
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
        }
    }



    @Transactional
    public void processNaverLogout(String accessToken, Long userId) {
            refreshTokenRepository.deleteByUser_UserId(userId);
    }

    @Transactional
    public void processNaverWithdraw(String accessToken, Long userId) {
        String url = "https://nid.naver.com/oauth2.0/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "delete");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("access_token", accessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            restTemplate.exchange(url, HttpMethod.POST, request, Void.class);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

            user.setUserValid(false);
            user.setUserName("delete " + user.getUserName());
            userRepository.save(user);

            refreshTokenRepository.deleteByUser_UserId(userId);

        } catch (HttpClientErrorException e) {
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
        }
    }

    public User updateNickName(Long userId, String nickName) {
        if (nickName == null || nickName.trim().isEmpty()) {
            nickName = generateNickname();
        }

        if (userRepository.existsByNickName(nickName)) {
            throw new ApiException(ExceptionEnum.DUPLICATE_NICKNAME);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

        user.setNickName(nickName);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return user;
    }

    private String generateNickname() {
        for (int i = 0; i < 5; i++) {
            String generatedNickName = nickNameGenerator.getNickName();
            if (!userRepository.existsByNickName(generatedNickName)) {
                return generatedNickName;
            }
        }
        throw new IllegalArgumentException("중복되지 않는 닉네임을 생성하는데 실패했습니다.");
    }


    private String getRefreshToken(User user) {
        return refreshTokenRepository.findByUser_UserId(user.getUserId())
                .map(existingToken -> {
                    if (jwtUtil.validToken(existingToken.getRefreshToken())) {
                        return existingToken.getRefreshToken();
                    } else {
                        String newRefreshToken = createNewRefreshToken(user);
                        existingToken.setRefreshToken(newRefreshToken);
                        refreshTokenRepository.save(existingToken);
                        return newRefreshToken;
                    }
                })
                .orElseGet(() -> {
                    String newRefreshToken = createNewRefreshToken(user);
                    RefreshToken newTokenEntity = new RefreshToken(user, newRefreshToken);
                    refreshTokenRepository.save(newTokenEntity);
                    return newRefreshToken;
                });
    }

    private String createNewRefreshToken(User user) {
        long refreshTokenExpiration = 7 * 24 * 60 * 60 * 1000L;
        return jwtUtil.createJwt(user.getUserName(), user.getName(), user.getUserId(), refreshTokenExpiration);
    }

    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

        return new UserProfileResponse(
                user.getNickName(),
                user.getEmail(),
                user.getName(),
                user.getCreatedAt(),
                user.getReward()
        );
    }



}
