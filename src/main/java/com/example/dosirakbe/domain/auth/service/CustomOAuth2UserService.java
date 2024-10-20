package com.example.dosirakbe.domain.auth.service;

import com.example.dosirakbe.domain.auth.dto.GoogleUserInfo;
import com.example.dosirakbe.domain.auth.dto.KakaoUserInfo;
import com.example.dosirakbe.domain.auth.dto.NaverUserInfo;
import com.example.dosirakbe.domain.auth.dto.OAuth2UserInfo;
import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.user.dto.response.UserDTO;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        System.out.println("getAccessToken: " + userRequest.getAccessToken());
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = null;
        if (registrationId.equals("naver")) {

            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {

            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (registrationId.equals("kakao")) {

            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }


        String username = oAuth2UserInfo.getProvider() + " " + oAuth2UserInfo.getProviderId();

        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(username);
        userDTO.setName(oAuth2UserInfo.getName());
        userDTO.setEmail(oAuth2UserInfo.getEmail());
        userDTO.setProfileImg(oAuth2UserInfo.getProfileImg());


        return new CustomOAuth2User(userDTO);
    }





}
