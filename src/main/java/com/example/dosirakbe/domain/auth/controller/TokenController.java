package com.example.dosirakbe.domain.auth.controller;


import com.example.dosirakbe.domain.auth.dto.response.TokenResponse;
import com.example.dosirakbe.domain.auth.service.TokenService;
import com.example.dosirakbe.global.util.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @GetMapping("/reissue/access-token")
    public ResponseEntity<ApiResult> reissueAccessToken() {
        TokenResponse accessToken = tokenService.reissueAccessToken();

        ApiResult result = ApiResult.builder()
                .status("200")
                .message("토큰 재발급에 성공하였습니다")
                .exception(null)
                // .result(accessToken)
                .build();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}