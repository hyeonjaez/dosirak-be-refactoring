package com.example.dosirakbe.domain.auth.controller;


import com.example.dosirakbe.domain.auth.dto.response.TokenResponse;
import com.example.dosirakbe.domain.auth.service.TokenService;
import com.example.dosirakbe.global.util.ApiExceptionEntity;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.example.dosirakbe.global.util.JwtUtil;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;
    private final JwtUtil jwtUtil;

    @GetMapping("/api/token/reissue/access-token")
    public ResponseEntity<ApiResult> reissueAccessToken() {
        try {

            TokenResponse accessToken = tokenService.reissueAccessToken();

            ApiResult<TokenResponse> result = ApiResult.<TokenResponse>builder()
                    .status(StatusEnum.SUCCESS)
                    .message("토큰 재발급에 성공하였습니다")
                    .data(accessToken)
                    .exception(null)
                    .build();

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (JwtException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResult.builder()
                            .status(StatusEnum.FAILURE)
                            .message("토큰 재발급에 실패하였습니다")
                            .build()
            );

        }
    }

    @GetMapping("/api/valid-token")
    public ResponseEntity<ApiResult> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {

            String token = jwtUtil.getTokenFromHeader(authorizationHeader);

            if (jwtUtil.validToken(token)) {
                ApiResult result = ApiResult.builder()
                        .status(StatusEnum.SUCCESS)
                        .message("유효한 토큰입니다.")
                        .build();
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                ApiResult result = ApiResult.builder()
                        .status(StatusEnum.FAILURE)
                        .message("유효하지 않은 토큰입니다.")
                        .build();
                return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
            }
        } catch (IllegalArgumentException e) {
            ApiResult result = ApiResult.builder()
                    .status(StatusEnum.FAILURE)
                    .message("잘못된 요청입니다: " + e.getMessage())
                    .build();
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }


    }


}