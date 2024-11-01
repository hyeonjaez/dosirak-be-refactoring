package com.example.dosirakbe.domain.auth.jwt;

import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.user.dto.response.UserDTO;
import com.example.dosirakbe.global.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();


        if (requestURI.equals("/api/user/withdraw") ||
                requestURI.equals("/api/user/register") ||
                requestURI.equals("/api/user/logout") )  {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);

        try {
            if (jwtUtil.isExpired(token)) {

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                String jsonErrorResponse = "{\"status\": false, \"message\": \"JWT 토큰이 만료되었습니다.\"}";
                response.getWriter().write(jsonErrorResponse);
                return;
            }


            Long userId = jwtUtil.getUserId(token);
            String userName = jwtUtil.getUserName(token);
            String name = jwtUtil.getName(token);
            String email = jwtUtil.getEmail(token);
            String profileImg = jwtUtil.getProfileImg(token);

            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(userId);
            userDTO.setUserName(userName);
            userDTO.setName(name);
            userDTO.setEmail(email);
            userDTO.setProfileImg(profileImg);

            CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);
            Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String jsonErrorResponse = "{\"status\": false, \"message\": \"JWT 토큰이 만료되었습니다.\"}";
            response.getWriter().write(jsonErrorResponse);
            return;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String jsonErrorResponse = "{\"status\": false, \"message\": \"JWT 토큰이 만료되었습니다.\"}";
            response.getWriter().write(jsonErrorResponse);
            return;
        }

        filterChain.doFilter(request, response);
    }



}
