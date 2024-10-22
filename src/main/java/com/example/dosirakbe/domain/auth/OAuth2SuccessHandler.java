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
import com.example.dosirakbe.global.util.StatusEnum;
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

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        System.out.println(customUserDetails);

        String userName = customUserDetails.getUserName();
        String name = customUserDetails.getName();
        User user = userRepository.findByUserName(userName);
        Long userId = user.getUserId();



        //token생성
        String accessToken = jwtUtil.createJwt(userName, name, userId,60 * 60 * 1000L);  // 1시간 유효
        String refreshToken = jwtUtil.createJwt(userName, name, userId,7 * 24 * 60 * 60 * 1000L);  // 7일 유효

        Optional<RefreshToken> existToken = refreshTokenRepository.findByUser_UserId(userId);

        if (existToken.isPresent()) {
            // 기존 토큰이 있으면 업데이트
            RefreshToken existingToken = existToken.get();
            existingToken.setRefreshToken(refreshToken);
            refreshTokenRepository.save(existingToken);
        } else {
            // 없으면 새로 저장
            refreshTokenRepository.save(new RefreshToken(user, refreshToken));
        }

        // ApiResult로 반환할 데이터 생성
        ApiResult result = ApiResult.builder()
                .status(StatusEnum.SUCCESS)
                .message("소셜 로그인에 성공하였습니다")
                .exception(null)
                .build();

        // JSON 형태로 토큰을 response로 반환
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        response.getWriter().write(objectMapper.writeValueAsString(
                Map.of("status", result.getStatus(),
                        "message", result.getMessage(),
                        "result", tokens)));

        String responseData = objectMapper.writeValueAsString(
                Map.of("status", result.getStatus(),
                        "message", result.getMessage(),
                        "result", tokens));

        log.info("Response Data: {}", responseData);

        //리프레쉬 토큰 쿠키에
        //response.addCookie(createCookie("refreshToken", refreshToken));
        //response.sendRedirect("http://localhost:3000/nickname");
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

}