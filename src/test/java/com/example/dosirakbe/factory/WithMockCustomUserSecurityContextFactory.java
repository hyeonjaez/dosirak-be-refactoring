package com.example.dosirakbe.factory;

import com.example.dosirakbe.annotations.WithMockCustomUser;
import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.user.dto.response.UserDTO;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1L);
        userDTO.setUserName(annotation.username());
        userDTO.setName(annotation.name());
        userDTO.setEmail(annotation.email());
        userDTO.setProfileImg("https://example.com/profile.jpg");

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customOAuth2User, null, customOAuth2User.getAuthorities()
        );

        context.setAuthentication(authentication);
        return context;
    }


}
