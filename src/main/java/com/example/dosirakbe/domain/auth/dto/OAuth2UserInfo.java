package com.example.dosirakbe.domain.auth.dto;

/**
 * packageName    : com.example.dosirakbe.domain.auth.dto<br>
 * fileName       : OAuth2UserInfo<br>
 * author         : yyujin1231<br>
 * date           : 10/20/24<br>
 * description    : 다양한 OAuth2 제공자의 사용자 정보를 표준화하여 제공하기 위한 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/20/24        yyujin1231                최초 생성<br>
 */


public interface OAuth2UserInfo {

    /**
     * OAuth2 제공자의 이름을 반환합니다.
     *
     * @return 제공자 이름 (kakao, naver 등)
     */

    String getProvider();


    /**
     * OAuth2 제공자의 사용자 ID를 반환합니다.
     *
     * @return 제공자 사용자 ID
     */



    String getProviderId();

    /**
     * 사용자의 이메일 주소를 반환합니다.
     *
     * @return 사용자 이메일 주소
     */

    String getEmail();

    /**
     * 사용자의 이름을 반환합니다.
     *
     * @return 사용자 이름
     */

    String getName();

    /**
     * 사용자의 프로필 이미지 URL을 반환합니다.
     *
     * @return 사용자 프로필 이미지 URL
     */

    String getProfileImg();



}
