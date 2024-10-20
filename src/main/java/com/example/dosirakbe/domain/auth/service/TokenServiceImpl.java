package com.example.dosirakbe.domain.auth.service;

import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.auth.dto.response.TokenResponse;
import com.example.dosirakbe.domain.auth.entity.RefreshToken;
import com.example.dosirakbe.domain.auth.repository.RefreshTokenRepository;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

    @Value("${spring.jwt.access-token.expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME; // 액세스 토큰 유효기간

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public TokenResponse reissueAccessToken() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("인증된 사용자가 아닙니다.");
        }

        String userName = ((CustomOAuth2User) authentication.getPrincipal()).getUserName();

        User user = userRepository.findByUserName(userName);

        if (user == null) {
            throw new IllegalArgumentException("해당하는 사용자를 찾을 수 없습니다.");
        }

        Long userId = user.getUserId();

        Optional<RefreshToken> savedRefreshToken = refreshTokenRepository.findByUser_UserId(userId);

        if (savedRefreshToken.isEmpty()) {
            throw new IllegalArgumentException("리프레쉬 토큰이 없습니다.");
        }

        String refreshToken = savedRefreshToken.get().getRefreshToken();

        if (jwtUtil.isExpired(refreshToken)) {
            throw new IllegalArgumentException("리프레쉬 토큰이 만료되었습니다.");
        }

        String accessToken = jwtUtil.createJwt(userName,
                savedRefreshToken.get().getUser().getName(),
                userId, ACCESS_TOKEN_EXPIRATION_TIME);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}