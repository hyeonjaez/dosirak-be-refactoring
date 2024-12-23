package com.example.dosirakbe.domain.user.controller;

import com.example.dosirakbe.annotations.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockCustomUser
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("회원 인증 상태에서 사용자 ID와 이름 반환 테스트")
    public void testGetUserIdWithAuthentication() throws Exception {
        mockMvc.perform(get("/api/test/user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("현재 사용자 ID: 1, 이름: 이유진"));
    }
}
