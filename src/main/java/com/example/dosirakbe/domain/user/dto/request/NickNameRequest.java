package com.example.dosirakbe.domain.user.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * packageName    : com.example.dosirakbe.domain.user.dto.request<br>
 * fileName       : NickNameRequest<br>
 * author         : yyujin1231<br>
 * date           : 10/20/24<br>
 * description    : 사용자 닉네임 변경 요청 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/20/24        yyujin1231                최초 생성<br>
 */



@Getter
public class NickNameRequest {

    /**
     * 닉네임 요청 필드입니다.
     */

    @Size(max = 30, message = "닉네임은 최대 30자까지 입력 가능합니다.")
    private String nickName;
}
