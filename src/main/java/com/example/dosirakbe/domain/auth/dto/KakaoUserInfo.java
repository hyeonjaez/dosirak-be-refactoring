package com.example.dosirakbe.domain.auth.dto;

import lombok.AllArgsConstructor;

import java.util.Map;


/**
 * packageName    : com.example.dosirakbe.domain.auth.dto<br>
 * fileName       : KakaoUserInfo<br>
 * author         : yyujin1231<br>
 * date           : 10/20/24<br>
 * description    : Kakao 인증 사용자 정보를 캡슐화하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/20/24        yyujin1231                최초 생성<br>
 */

@AllArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes;

    /**
     * 제공자의 ID를 반환합니다.
     *
     * @return 제공자 ID (카카오에서 제공하는 id)
     */

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    /**
     * 제공자 이름을 반환합니다.
     *
     * @return 제공자 이름 (kakao)
     */

    @Override
    public String getProvider() {
        return "kakao";
    }

    /**
     * 사용자 이름을 반환합니다.
     *
     * @return 사용자 이름 (카카오에서 제공하는 nickname)
     */

    @Override
    public String getName() {
        return (String) ((Map) attributes.get("properties")).get("nickname");
    }

    /**
     * 사용자 이메일을 반환합니다.
     *
     * @return 사용자 이름 (카카오에서 제공하는 email)
     */

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        return (String) kakaoAccount.get("email");
    }

    /**
     * 사용자 이메일을 반환합니다.
     *
     * @return 사용자 프로필 이미지 (카카오에서 제공하는 profile_image_url)
     */


    @Override
    public String getProfileImg() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        boolean needsAgreement = (boolean) kakaoAccount.get("profile_image_needs_agreement");
        if (needsAgreement) {
            return "default_image_url";}
        return (String) profile.get("profile_image_url");
    }

}