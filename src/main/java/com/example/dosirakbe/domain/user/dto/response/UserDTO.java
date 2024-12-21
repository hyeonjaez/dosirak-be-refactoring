package com.example.dosirakbe.domain.user.dto.response;


import lombok.Getter;
import lombok.Setter;


/**
 * packageName    : com.example.dosirakbe.domain.user.dto.response<br>
 * fileName       : UserDTO<br>
 * author         : yyujin1231<br>
 * date           : 10/20/24<br>
 * description    : 사용자 정보를 반환하는 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/20/24        yyujin1231                최초 생성<br>
 */


@Getter
@Setter
public class UserDTO {

    /**
     * 사용자 ID.
     */

    private Long userId;

    /**
     * 사용자의 고유 사용자 이름.
     * <p>
     * User 엔티티의 userName 필드에 매핑됩니다.
     * </p>
     */

    private String userName;


    /**
     * 사용자의 실제 이름.
     * <p>
     * User 엔티티의 name 필드에 매핑됩니다.
     * </p>
     */


    private String name;

    /**
     * 사용자의 이메일 주소.
     * <p>
     * User 엔티티의 email 필드에 매핑됩니다.
     * </p>
     */

    private String email;

    /**
     * 사용자의 프로필 이미지 URL.
     * <p>
     * User 엔티티의 profileImg 필드에 매핑됩니다.
     * </p>
     */

    private String profileImg;

    /**
     * 기본 생성자.
     * <p>
     * 기본적으로 비어 있는 UserDTO 객체를 생성합니다.
     * </p>
     */
    public UserDTO() {
    }



}
