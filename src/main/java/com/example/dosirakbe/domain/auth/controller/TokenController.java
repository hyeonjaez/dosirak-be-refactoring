package com.example.dosirakbe.domain.auth.controller;


import com.example.dosirakbe.domain.auth.dto.response.TokenResponse;
import com.example.dosirakbe.domain.auth.service.TokenService;
import com.example.dosirakbe.global.util.ApiExceptionEntity;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

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

        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResult.builder()
                            .status(StatusEnum.FAILURE)
                            .message("토큰 재발급에 실패하였습니다")
                            .build()
            );

        }
    }
}