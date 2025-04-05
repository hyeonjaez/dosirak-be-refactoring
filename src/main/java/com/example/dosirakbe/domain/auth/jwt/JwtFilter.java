package com.example.dosirakbe.domain.auth.jwt;

import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.user.dto.response.UserDTO;
import com.example.dosirakbe.global.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * packageName    : com.example.dosirakbe.domain.auth.jwt<br>
 * fileName       : JwtFilter<br>
 * author         : yyujin1231<br>
 * date           : 10/20/24<br>
 * description    : JWT를 기반으로 요청을 필터링하여 인증을 처리하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/20/24        yyujin1231                최초 생성<br>
 */


@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    /**
     * JwtFilter 생성자.
     *
     * @param jwtUtil JWT 유틸리티 클래스
     */

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * <p>요청을 필터링하여 JWT를 검증하고, 인증 정보를 설정합니다.</p>
     *
     * <p>매 요청마다 JWT를 확인하고, 유효한 경우 사용자 정보를 추출하여 SecurityContext에 설정합니다.
     * Spring Security의 필터 체인 내에서 동작하며, {@link OncePerRequestFilter}를 상속받아 구현되었습니다.</p>
     *
     * <p>JWT가 만료되었거나 유효하지 않은 경우, 적절한 상태 코드와 메시지를 클라이언트에 반환합니다.</p>
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param filterChain 필터 체인 객체
     * @throws ServletException 필터 처리 중 예외 발생 시
     * @throws IOException 입출력 예외 발생 시
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();


        if (requestURI.equals("/api/users/withdraw") ||
                requestURI.equals("/api/users") ||
                requestURI.equals("/api/users/logout") )  {
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