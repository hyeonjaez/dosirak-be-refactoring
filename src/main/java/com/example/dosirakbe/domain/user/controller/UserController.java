package com.example.dosirakbe.domain.user.controller;

import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.auth.entity.RefreshToken;
import com.example.dosirakbe.domain.auth.repository.RefreshTokenRepository;
import com.example.dosirakbe.domain.user.dto.request.NickNameRequest;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.generator.NickNameGenerator;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.domain.user.service.UserService;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.JwtUtil;
import com.example.dosirakbe.global.util.StatusEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {


    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;


    @PostMapping("/api/user/register")
    public ResponseEntity<?> completeRegistration(@RequestBody NickNameRequest nickNameRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomOAuth2User customUser = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = customUser.getUserDTO().getUserId();

        try {
            User updatedUser = userService.updateNickname(userId, nickNameRequest.getNickName());

            Map<String, String> responseData = new HashMap<>();
            responseData.put("nickName", updatedUser.getNickName());

            ApiResult result = ApiResult.builder()
                    .status(StatusEnum.SUCCESS)
                    .message("닉네임이 성공적으로 저장되었습니다.")
                    .data(responseData)
                    .exception(null)
                    .build();
            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {

            ApiResult result = ApiResult.builder()
                    .status(StatusEnum.FAILURE)
                    .message(e.getMessage())
                    .exception(null)
                    .build();
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("/api/test")
    public String test(){
        return "테스트 성공";
    }
}
