package com.example.dosirakbe.domain.auth.dto.response;

import com.example.dosirakbe.domain.user.dto.response.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * packageName    : com.example.dosirakbe.domain.auth.dto.response<br>
 * fileName       : CustomOAuth2User<br>
 * author         : yyujin1231<br>
 * date           : 10/20/24<br>
 * description    : OAuth2 인증 과정에서 사용자 정보를 제공하는 역할을 수행하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/20/24        yyujin1231                최초 생성<br>
 */



public class CustomOAuth2User implements OAuth2User {

    private final UserDTO userDTO;

    /**
     * CustomOAuth2User 생성자.
     *
     * @param userDTO 사용자 정보가 포함된 UserDTO 객체
     */

    public CustomOAuth2User(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    /**
     * 사용자 속성을 반환합니다.
     *
     * @return 사용자 속성의 Map 객체 (현재 null 반환)
     */

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }


    /**
     * 사용자 권한을 반환합니다.
     *
     * @return GrantedAuthority 컬렉션 (현재 빈 리스트 반환)
     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    /**
     * 사용자 이름을 반환합니다.
     *
     * @return 사용자 이름
     */

    @Override
    public String getName() {
        return userDTO.getName();
    }

    /**
     * 사용자 이름을 반환합니다.
     *
     * @return 사용자고유이름 (provider + providerId)
     */

    public String getUserName() {
        return userDTO.getUserName();
    }

    /**
     * 사용자 프로필 이미지를 반환합니다.
     *
     * @return 사용자 프로필 이미지 URL
     */

    public String getProfileImg() {
        return userDTO.getProfileImg();
    }

    /**
     * UserDTO 객체를 반환합니다.
     *
     * @return UserDTO 객체
     */

    public UserDTO getUserDTO() {
        return this.userDTO;
    }

    /**
     * 사용자 ID를 반환합니다.
     *
     * @return 사용자 ID
     */

    public Long getUserId() {
        return userDTO.getUserId();
    }

}
