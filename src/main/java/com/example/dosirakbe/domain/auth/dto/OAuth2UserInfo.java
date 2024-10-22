package com.example.dosirakbe.domain.auth.dto;

import com.example.dosirakbe.domain.user.entity.User;

import java.util.Map;

public interface OAuth2UserInfo {

    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();

    String getProfileImg();



}
