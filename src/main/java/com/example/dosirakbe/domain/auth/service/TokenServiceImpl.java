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


/**
 * packageName    : com.example.dosirakbe.domain.auth.service<br>
 * fileName       : TokenServiceImpl<br>
 * author         : yyujin1231<br>
 * date           : 10/20/24<br>
 * description    : 액세스 토큰과 리프레시 토큰의 생성 및 갱신 관련 로직을 처리하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/20/24        yyujin1231                최초 생성<br>
 */


@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

    @Value("${spring.jwt.access-token.expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME; // 액세스 토큰 유효기간

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    /**
     * <p>만료된 액세스 토큰을 재발급합니다.</p>
     *
     * <p>SecurityContext에서 인증 정보를 가져와 사용자를 확인한 뒤, 리프레시 토큰을 검증하여 새로운 액세스 토큰을 생성합니다.</p>
     *
     * @return {@link TokenResponse} 새로 발급된 액세스 토큰 정보를 포함하는 객체
     * @throws ApiException {@link ExceptionEnum#SECURITY} 인증 정보가 없거나 유효하지 않을 때
     * @throws ApiException {@link ExceptionEnum#DATA_NOT_FOUND} 사용자 또는 리프레시 토큰이 존재하지 않을 때
     * @throws ApiException {@link ExceptionEnum#ACCESS_DENIED_EXCEPTION} 리프레시 토큰이 만료되었을 때
     */

    @Override
    public TokenResponse reissueAccessToken() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ApiException(ExceptionEnum.SECURITY);
        }

        String userName = ((CustomOAuth2User) authentication.getPrincipal()).getUserName();

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

        Long userId = user.getUserId();

        Optional<RefreshToken> savedRefreshToken = refreshTokenRepository.findByUser_UserId(userId);

        if (savedRefreshToken.isEmpty()) {
            throw new ApiException(ExceptionEnum.DATA_NOT_FOUND);
        }

        String refreshToken = savedRefreshToken.get().getRefreshToken();

        if (jwtUtil.isExpired(refreshToken)) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
        }

        String accessToken = jwtUtil.createJwt(userName,
                savedRefreshToken.get().getUser().getName(),
                userId, ACCESS_TOKEN_EXPIRATION_TIME);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}