package com.example.dosirakbe.domain.rank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : com.example.dosirakbe.domain.rank.dto.response<br>
 * fileName       : RankController<br>
 * author         : yyujin1231<br>
 * date           : 11/15/24<br>
 * description    : 사용자 랭킹 제공 관련 CRUD controller 클래스 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/15/24        yyujin1231                최초 생성<br>
 */


@Getter
@AllArgsConstructor
public class RankResponse {

    /**
     * 사용자의 고유 식별자입니다.
     *
     * <p>
     * 이 필드는 사용자를 유일하게 식별하기 위한 ID를 나타냅니다.
     * </p>
     */

    private Long userId;

    /**
     * 사용자의 프로필 이미지 URL입니다.
     *
     * <p>
     * 이 필드는 사용자의 프로필 이미지를 나타내는 URL을 포함합니다.
     * </p>
     */


    private String profileImg;

    /**
     * 사용자의 랭킹입니다.
     *
     * <p>
     * 이 필드는 사용자의 순위를 나타냅니다.
     * </p>
     */
    private Integer rank;

    /**
     * 사용자의 닉네임입니다.
     *
     * <p>
     * 이 필드는 사용자의 닉네임을 나타냅니다.
     * </p>
     */


    private String nickName;

    /**
     * 사용자가 받은 리워드 입니다.
     *
     * <p>
     * 이 필드는 사용자가 현재까지 받은 리워드를 나타냅니다.
     * </p>
     */


    private Integer reward;
}
