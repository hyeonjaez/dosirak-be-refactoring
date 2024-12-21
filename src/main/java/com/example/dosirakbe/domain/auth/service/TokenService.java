package com.example.dosirakbe.domain.auth.service;

import com.example.dosirakbe.domain.auth.dto.response.TokenResponse;

/**
 * packageName    : com.example.dosirakbe.domain.auth.service<br>
 * fileName       : TokenService<br>
 * author         : yyujin1231<br>
 * date           : 10/20/24<br>
 * description    : JWT 액세스 토큰과 관련된 작업을 처리하기 위한 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/20/24        yyujin1231                최초 생성<br>
 */

public interface TokenService {

    /**
     * 만료된 액세스 토큰을 재발급합니다.
     *
     * @return {@link TokenResponse} 새로 발급된 액세스 토큰 정보를 포함하는 객체
     */

    TokenResponse reissueAccessToken();
}
