package com.example.dosirakbe.domain.rank.controller;
import com.example.dosirakbe.annotations.WithMockCustomUser;
import com.example.dosirakbe.domain.rank.dto.response.RankResponse;
import com.example.dosirakbe.domain.rank.service.RankService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("RankController 테스트")
@WithMockCustomUser
class RankControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RankService rankService;

    @Test
    @DisplayName("회원 인증 상태에서 사용자 랭킹 반환 테스트")
    public void testGetRankByUserId() throws Exception {

        RankResponse rankResponse = new RankResponse(1L, "https://example.com/profile.jpg", 10, "테스트닉네임", 100);
        given(rankService.getRankByUserId(anyLong())).willReturn(rankResponse);

        mockMvc.perform(get("/api/users/me/rank")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("사용자의 랭킹 반환"))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.profileImg").value("https://example.com/profile.jpg"))
                .andExpect(jsonPath("$.data.rank").value(10))
                .andExpect(jsonPath("$.data.nickName").value("테스트닉네임"))
                .andExpect(jsonPath("$.data.reward").value(100));
    }
}
