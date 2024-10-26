package com.example.dosirakbe.domain.auth.dto.response;

import com.example.dosirakbe.domain.user.dto.response.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


public class CustomOAuth2User implements OAuth2User {

    private final UserDTO userDTO;

    public CustomOAuth2User(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null; // Attributes는 필요에 따라 설정
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>(); // 권한 리스트를 설정할 수 있음
    }

    @Override
    public String getName() {
        return userDTO.getName();
    }

    public String getUserName() {
        return userDTO.getUserName();
    }

    public String getEmail() {
        return userDTO.getEmail();
    }

    public String getProfileImg() {
        return userDTO.getProfileImg();
    }

    public UserDTO getUserDTO() {
        return this.userDTO;
    }

    public Long getUserId() {
        return userDTO.getUserId();
    }
}
