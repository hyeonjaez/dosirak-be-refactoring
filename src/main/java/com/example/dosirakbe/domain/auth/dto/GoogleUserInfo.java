package com.example.dosirakbe.domain.auth.dto;


import com.example.dosirakbe.domain.user.entity.User;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class GoogleUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes;

    @Override
    public String getProvider() {

        return "google";
    }

    @Override
    public String getProviderId() {

        return attributes.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getProfileImg() {
        return (String) attributes.get("picture");
    }



}
