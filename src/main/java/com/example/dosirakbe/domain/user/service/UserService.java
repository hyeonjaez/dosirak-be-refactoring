package com.example.dosirakbe.domain.user.service;

import com.example.dosirakbe.domain.auth.dto.OAuth2UserInfo;
import com.example.dosirakbe.domain.auth.entity.RefreshToken;
import com.example.dosirakbe.domain.auth.repository.RefreshTokenRepository;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.generator.NickNameGenerator;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final NickNameGenerator nickNameGenerator;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public User updateNickname(Long userId, String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            nickname = generateNickname();
        }

        if (userRepository.existsByNickName(nickname)) {
            throw new IllegalArgumentException("닉네임이 이미 존재합니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        user.setNickName(nickname);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return user;
    }

    private String generateNickname() {
        for (int i = 0; i < 5; i++) {
            String generatedNickName = nickNameGenerator.getNickName();
            if (!userRepository.existsByNickName(generatedNickName)) {
                return generatedNickName;
            }
        }
        throw new IllegalArgumentException("중복되지 않는 닉네임을 생성하는데 실패했습니다.");
    }

    public Map<String, String> registerUser(OAuth2UserInfo oAuth2UserInfo) {
        String userName = oAuth2UserInfo.getProvider() + " " + oAuth2UserInfo.getProviderId();
        User user = userRepository.findByUserName(userName).orElseGet(() -> {
            User newUser = new User();
            newUser.setUserName(userName);
            newUser.setName(oAuth2UserInfo.getName());
            newUser.setEmail(oAuth2UserInfo.getEmail());
            newUser.setProfileImg(oAuth2UserInfo.getProfileImg());
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setUpdatedAt(LocalDateTime.now());
            newUser.setUserValid(true);
            return userRepository.save(newUser);
        });

        long accessTokenExpiration = 24 * 60 * 60 * 1000L;
        String accessToken = jwtUtil.createJwt(user.getUserName(), user.getName(), user.getUserId(), accessTokenExpiration);

        long refreshTokenExpiration = 7 * 24 * 60 * 60 * 1000L;
        String refreshToken = jwtUtil.createJwt(user.getUserName(), user.getName(), user.getUserId(), refreshTokenExpiration);

        saveOrUpdateRefreshToken(user, refreshToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    private void saveOrUpdateRefreshToken(User user, String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByUser_UserId(user.getUserId())
                .orElse(new RefreshToken(user, refreshToken));
        token.setRefreshToken(refreshToken);
        refreshTokenRepository.save(token);
    }


}
