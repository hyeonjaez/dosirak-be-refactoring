package com.example.dosirakbe.domain.auth.dto;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        // Long 타입이기 때문에 toString으로 변환
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getName() {
        // kakao_account라는 Map에서 추출
        return (String) ((Map) attributes.get("properties")).get("nickname");
    }

    @Override
    public String getEmail() {
        // email is inside the "kakao_account" map
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        return (String) kakaoAccount.get("email");
    }

    @Override
    public String getProfileImg() {
        // profile image is inside the "profile" map within the "kakao_account" map
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        // profile_image_needs_agreement 확인
        boolean needsAgreement = (boolean) kakaoAccount.get("profile_image_needs_agreement");
        if (needsAgreement) {
            // 동의가 필요한 경우 기본 이미지를 반환하거나 동의 요청을 처리
            return "default_image_url"; // 기본 이미지 URL 반환
        }

        return (String) profile.get("profile_image_url");
    }

}
