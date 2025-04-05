package com.example.dosirakbe.domain.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * packageName    : com.example.dosirakbe.global.util<br>
 * fileName       : JwtUtil<br>
 * author         : Yujin Lee<br>
 * date           : 10/19/24<br>
 * description    : JWT 토큰의 생성, 검증, 정보 추출을 처리하는 유틸리티 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/19/24        Yujin Lee                최초 생성<br>
 */
@Component
@Slf4j
public class JwtUtil {

    private final SecretKey secretKey;

    /**
     * {@link JwtUtil} 객체를 생성합니다.
     *
     * <p>
     * 주어진 비밀키를 사용하여 JWT 서명을 위한 {@link SecretKey}를 생성합니다.
     * </p>
     *
     * @param secret JWT 서명에 사용될 비밀키
     */
    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * 토큰에서 사용자 이름을 추출합니다.
     *
     * @param token JWT 토큰
     * @return 사용자 이름
     */
    public String getUserName(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userName", String.class);
    }

    /**
     * 토큰에서 이름을 추출합니다.
     *
     * @param token JWT 토큰
     * @return 이름
     */
    public String getName(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("name", String.class);
    }

    /**
     * 토큰에서 이메일을 추출합니다.
     *
     * @param token JWT 토큰
     * @return 이메일
     */
    public String getEmail(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }

    /**
     * 토큰에서 프로필 이미지를 추출합니다.
     *
     * @param token JWT 토큰
     * @return 프로필 이미지 URL
     */
    public String getProfileImg(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("profileImg", String.class);
    }

    /**
     * 토큰 만료 여부를 확인합니다.
     *
     * @param token JWT 토큰
     * @return 만료되었으면 {@code true}, 아니면 {@code false}
     */
    public Boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    /**
     * 토큰에서 사용자 ID를 추출합니다.
     *
     * @param token JWT 토큰
     * @return 사용자 ID
     */
    public Long getUserId(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userId", Long.class);
    }

    /**
     * JWT 토큰을 생성합니다.
     *
     * @param userName  사용자 이름
     * @param name      이름
     * @param userId    사용자 ID
     * @param expiredMs 토큰 만료 시간(밀리초)
     * @return 생성된 JWT 토큰
     */
    public String createJwt(String userName, String name, Long userId, Long expiredMs) {
        return Jwts.builder()
                .claim("userName", userName)
                .claim("name", name)
                .claim("userId", userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    /**
     * 토큰의 유효성을 검증합니다.
     *
     * @param token JWT 토큰
     * @return 유효하면 {@code true}, 그렇지 않으면 {@code false}
     */
    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("유효하지 않은 토큰: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 토큰의 클레임을 추출합니다.
     *
     * @param token JWT 토큰
     * @return {@link Claims} 객체
     */
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Authorization 헤더에서 토큰을 추출합니다.
     *
     * @param authorizationHeader Authorization 헤더
     * @return 추출된 토큰
     * @throws IllegalArgumentException 유효하지 않은 헤더인 경우
     */
    public String getTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 헤더");
        }
        return authorizationHeader.substring(7);
    }


}
