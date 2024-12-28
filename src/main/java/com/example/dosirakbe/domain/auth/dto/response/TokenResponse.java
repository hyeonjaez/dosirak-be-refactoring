package com.example.dosirakbe.domain.auth.dto.response;

import lombok.Builder;
import lombok.Getter;


/**
 * packageName    : com.example.dosirakbe.domain.auth.dto.response<br>
 * fileName       : TokenResponse<br>
 * author         : yyujin1231<br>
 * date           : 10/20/24<br>
 * description    : 액세스 토큰 정보를 전달하기 위한 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/20/24        yyujin1231                최초 생성<br>
 */


@Builder
@Getter
public class TokenResponse {

    /**
     * 클라이언트에 반환될 액세스 토큰입니다.
     */


    private String accessToken;

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

}
