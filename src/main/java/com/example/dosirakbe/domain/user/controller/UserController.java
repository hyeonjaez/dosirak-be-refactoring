package com.example.dosirakbe.domain.user.controller;


import com.example.dosirakbe.domain.auth.entity.RefreshToken;
import com.example.dosirakbe.domain.auth.repository.RefreshTokenRepository;
import com.example.dosirakbe.domain.user.dto.request.NickNameRequest;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.generator.NickNameGenerator;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.JwtUtil;
import com.example.dosirakbe.global.util.StatusEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/api/user/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestParam("nickName") String nickName) {
        boolean exists = userRepository.existsByNickName(nickName);
        if (exists) {
            ApiResult result = ApiResult.builder()
                    .status(StatusEnum.FAILURE)
                    .message("중복된 닉네임입니다.")
                    .exception(null)
                    .build();
            return ResponseEntity.badRequest().body(result);
        }

        ApiResult result = ApiResult.builder()
                .status(StatusEnum.SUCCESS)
                .message("사용 가능한 닉네임입니다.")
                .exception(null)
                .build();
        return ResponseEntity.ok(result);
    }
    @PostMapping("/api/user/register")
    public ResponseEntity<?> completeRegistration(HttpServletRequest request, @RequestBody NickNameRequest nickNameRequest) {
        //String tempAccessToken = nickNameRequest.getTempAccessToken();

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("토큰이 없습니다.");
        }

        String tempAccessToken = jwtUtil.getTokenFromHeader(authorizationHeader);
        String nickName = nickNameRequest.getNickName();

        Map<String, Object> claims = jwtUtil.getClaims(tempAccessToken);
        String userName = (String) claims.get("userName");
        String name = (String) claims.get("name");
        String email = (String) claims.get("email");
        String profileImg = (String) claims.get("profileImg");


        if (nickName == null || nickName.isEmpty()) {
            int attempts = 0;
            boolean nicknameGenerated = false;

            while (attempts < 5) {
                NickNameGenerator generator = new NickNameGenerator();
                nickName = generator.getNickname();
                if (!userRepository.existsByNickName(nickName)) {
                    nicknameGenerated = true;
                    break;
                }
                attempts++;
            }

            if (!nicknameGenerated) {
                return ResponseEntity.badRequest().body("닉네임 생성에 실패했습니다. 다시 시도해 주세요.");
            }
        }


        User user = new User();
        user.setName(name);
        user.setUserName(userName);
        user.setNickName(nickName);
        user.setProfileImg(profileImg);
        user.setEmail(email);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setUserValid(true);
        userRepository.save(user);

        String accessToken = jwtUtil.createJwt(user.getUserName(), user.getName(), user.getUserId(), 60 * 60 * 1000L);
        String refreshToken = jwtUtil.createJwt(user.getUserName(), user.getName(), user.getUserId(), 7 * 24 * 60 * 60 * 1000L);


        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        RefreshToken refreshTokenEntity = new RefreshToken(user, refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);

        ApiResult<Map<String, String>> result = ApiResult.<Map<String, String>>builder()
                .status(StatusEnum.SUCCESS)
                .message("소셜 로그인 완료 후 최종 토큰이 발급")
                .data(tokens)
                .build();

        return ResponseEntity.ok(result);
    }
}
