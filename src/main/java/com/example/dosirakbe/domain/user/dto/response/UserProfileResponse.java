package com.example.dosirakbe.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;


import java.time.LocalDateTime;

/**
 * packageName    : com.example.dosirakbe.domain.user.dto.response<br>
 * fileName       : UserProfileResponse<br>
 * author         : yyujin1231<br>
 * date           : 10/20/24<br>
 * description    : 사용자 프로필 정보를 반환하는 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/20/24        yyujin1231                최초 생성<br>
 */


@Getter
@AllArgsConstructor
public class UserProfileResponse {

    /**
     * 사용자의 닉네임.
     * <p>
     * 사용자 프로필에서 표시되는 닉네임입니다.
     * </p>
     */

    private String nickName;

    /**
     * 사용자의 이메일 주소.
     */

    private String email;

    /**
     * 사용자의 실제 이름.
     * <p>
     * 사용자 계정을 생성할 때 제공된 이름입니다.
     * </p>
     */


    private String name;

    /**
     * 사용자 계정의 생성 시간.
     * <p>
     * 계정이 생성된 날짜와 시간을 나타냅니다.
     * </p>
     */

    private LocalDateTime createdAt;

    /**
     * 사용자가 보유한 리워드 포인트.
     * <p>
     * 서비스 이용을 통해 얻은 포인트입니다.
     * </p>
     */

    private Integer reward;



}
