package com.example.dosirakbe.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserProfileResponse {

    private String nickName;
    private String email;
    private String name;
    private LocalDateTime createdAt;
    private Integer reward;



}
