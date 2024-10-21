package com.example.dosirakbe.domain.user.service;

import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.generator.NickNameGenerator;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final NickNameGenerator nickNameGenerator;

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
        return userRepository.save(user);
    }

    private String generateNickname() {
        for (int i = 0; i < 5; i++) {
            String generatedNickName = new NickNameGenerator().getNickName();
            if (!userRepository.existsByNickName(generatedNickName)) {
                return generatedNickName;
            }
        }
        throw new IllegalArgumentException("중복되지 않는 닉네임을 생성하는데 실패했습니다.");
    }
}
