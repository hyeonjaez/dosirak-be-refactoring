package com.example.dosirakbe.domain.auth.dto;

import com.example.dosirakbe.domain.user.entity.User;

import java.util.Map;

public interface OAuth2UserInfo {

    //제공자
    String getProvider();

    //제공자에서 제공해주는 id
    String getProviderId();

    //이메일
    String getEmail();

    //이름(실명)
    String getName();

    //프로필이미지
    String getProfileImg();



}
