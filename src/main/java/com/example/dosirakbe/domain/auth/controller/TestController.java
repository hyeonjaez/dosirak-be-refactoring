package com.example.dosirakbe.domain.auth.controller;


import com.example.dosirakbe.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final JwtUtil jwtUtil;

    @GetMapping("/verify/access-token")
    public ResponseEntity<String> verifyAccessToken(
            @RequestHeader("Authorization") String authorizationHeader) {

        // Authorization 헤더에서 토큰 추출
        String accessToken = jwtUtil.getTokenFromHeader(authorizationHeader);
        System.out.println(accessToken);

        // 토큰 유효성 검사
        boolean isValid = jwtUtil.validToken(accessToken);

        if (!isValid) {
            return ResponseEntity.status(401).body("Invalid or expired access token.");
        }

        // 토큰이 유효한 경우, 사용자 정보를 추출
        String userName = jwtUtil.getUserName(accessToken);

        // 사용자에게 리소스 제공
        return ResponseEntity.ok("Access granted. Welcome, " + userName + "!");
    }

    @GetMapping("/")
    public String mainPage(){
        return "메인페이지임";
    }


}
