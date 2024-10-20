package com.example.dosirakbe.domain.auth.jwt;

import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.user.dto.response.UserDTO;
import com.example.dosirakbe.global.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            //System.out.println("헤더에 토큰 없음");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);

        if (jwtUtil.isExpired(token)) {
            //System.out.println("토큰 만료됨");
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtil.getUserName(token);
        String name = jwtUtil.getName(token);

        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(username);
        userDTO.setName(name);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
