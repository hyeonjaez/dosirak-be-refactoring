package com.example.dosirakbe.domain.user.implement;

import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReader {
    private final UserRepository userRepository;

    public User findByUserIdButThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.USER_NOT_FOUND));
    }
}
