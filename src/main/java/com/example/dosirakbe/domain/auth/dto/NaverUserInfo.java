package com.example.dosirakbe.domain.auth.dto;

import java.util.Map;


/**
 * packageName    : com.example.dosirakbe.domain.auth.dto<br>
 * fileName       : NaverUserInfor<br>
 * author         : yyujin1231<br>
 * date           : 10/20/24<br>
 * description    : Naver 인증 사용자 정보를 캡슐화하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/20/24        yyujin1231                최초 생성<br>
 */


public class NaverUserInfo implements OAuth2UserInfo {
    private Map<String, Object> attributes;


    /**
     * NaverUserInfo 생성자.
     *
     * @param attributes Naver 사용자 정보
     */


    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    /**
     * 제공자 이름을 반환합니다.
     *
     * @return 제공자 이름 (naver)
     */

    @Override
    public String getProvider() {
        return "naver";
    }


    /**
     * 제공자의 ID를 반환합니다.
     *
     * @return 제공자 ID
     */

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    /**
     * 사용자 이메일을 반환합니다.
     *
     * @return 사용자 이메일
     */

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }


    /**
     * 사용자 이름을 반환합니다.
     *
     * @return 사용자 이름
     */

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }


    /**
     * 사용자 프로필 이미지를 반환합니다.
     *
     * @return 사용자 프로필 이미지 URL
     */

    @Override
    public String getProfileImg() {
        return (String) attributes.get("profile_image");
    }
}
