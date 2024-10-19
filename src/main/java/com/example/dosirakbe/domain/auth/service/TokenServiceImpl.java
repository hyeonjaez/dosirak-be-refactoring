package com.example.dosirakbe.domain.auth.service;

import com.example.dosirakbe.domain.auth.dto.response.TokenResponse;
import com.example.dosirakbe.domain.auth.entity.RefreshToken;
import com.example.dosirakbe.domain.auth.repository.RefreshTokenRepository;
import com.example.dosirakbe.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Override
    public TokenResponse reissueAccessToken(String authorizationHeader) {

        log.info("Authorization Header: {}", authorizationHeader);
        // 헤더에서 토큰 추출
        String refreshToken = jwtUtil.getTokenFromHeader(authorizationHeader);

        // 리프레시 토큰에서 사용자 ID 추출
        Long userId = jwtUtil.getUserId(refreshToken);

        // DB에서 리프레시 토큰 조회
        Optional<RefreshToken> savedRefreshToken = refreshTokenRepository.findByUser_UserId(userId);

        if (savedRefreshToken.isEmpty() || !savedRefreshToken.get().getRefreshToken().equals(refreshToken)) {
            throw new IllegalArgumentException("리프레쉬 토큰 없음 또는 불일치");
        }

        // 리프레시 토큰 만료 체크
        if (jwtUtil.isExpired(refreshToken)) {
            throw new IllegalArgumentException("리프레쉬 토큰 만료");
        }

        // 새로운 액세스 토큰 발급
        String accessToken = jwtUtil.createJwt(savedRefreshToken.get().getUser().getUserName(), savedRefreshToken.get().getUser().getName(), userId, ACCESS_TOKEN_EXPIRATION_TIME);

        // 새 액세스 토큰 반환
        return TokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}