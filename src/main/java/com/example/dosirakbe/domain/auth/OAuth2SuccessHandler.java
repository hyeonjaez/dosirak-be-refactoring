package com.example.dosirakbe.domain.auth;

import com.example.dosirakbe.domain.auth.dto.GoogleUserInfo;
import com.example.dosirakbe.domain.auth.dto.NaverUserInfo;
import com.example.dosirakbe.domain.auth.dto.OAuth2UserInfo;
import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.auth.entity.RefreshToken;
import com.example.dosirakbe.domain.auth.repository.RefreshTokenRepository;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.swing.interop.SwingInterOpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    public OAuth2SuccessHandler(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {

        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository=refreshTokenRepository;
        this.userRepository=userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        String userName = customUserDetails.getUserName();
        String name = customUserDetails.getName();
        String email = customUserDetails.getEmail();
        String profileImg = customUserDetails.getProfileImg();

        User user = userRepository.findByUserName(userName);

        //기존유저인경우 바로토큰반환
        if (user != null) {
            Long userId = user.getUserId();
            String accessToken = jwtUtil.createJwt(user.getUserName(), user.getName(), user.getUserId(), 60 * 60 * 1000L);  // 1시간 유효
            String refreshToken = jwtUtil.createJwt(user.getUserName(), user.getName(), user.getUserId(), 7 * 24 * 60 * 60 * 1000L);  // 7일 유효

            Optional<RefreshToken> existToken = refreshTokenRepository.findByUser_UserId(userId);
            if (existToken.isPresent()) {
                RefreshToken existingToken = existToken.get();
                existingToken.setRefreshToken(refreshToken);
                refreshTokenRepository.save(existingToken);
            } else {
                refreshTokenRepository.save(new RefreshToken(user, refreshToken));
            }

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);

            ApiResult result = ApiResult.builder()
                    .status("200")
                    .message("로그인 성공")
                    .build();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(Map.of(
                    "status", result.getStatus(),
                    "message", result.getMessage(),
                    "tokens", tokens)));

        } else {
            // 신규 유저일 경우 임시 토큰 발급
            String tempAccessToken = jwtUtil.createTemporalJwt(userName, name, email, profileImg, 60 * 60 * 1000L ); // 임시 토큰

            ApiResult result = ApiResult.builder()
                    .status("200")
                    .message("신규 유저입니다. 임시 토큰 발급")
                    .build();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("tempAccessToken", tempAccessToken);

            response.getWriter().write(new ObjectMapper().writeValueAsString(Map.of(
                    "status", result.getStatus(),
                    "message", result.getMessage(),
                    "result", responseBody)));
        }
    }


}
