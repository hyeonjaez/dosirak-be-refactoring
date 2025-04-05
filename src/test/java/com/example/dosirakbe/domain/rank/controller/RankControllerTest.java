//package com.example.dosirakbe.domain.rank.controller;
//import com.example.dosirakbe.annotations.WithMockCustomUser;
//import com.example.dosirakbe.domain.rank.dto.response.RankResponse;
//import com.example.dosirakbe.domain.rank.service.RankService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.restdocs.RestDocumentationContextProvider;
//import org.springframework.restdocs.RestDocumentationExtension;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
//import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
//import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(controllers = RankController.class)
//@WithMockCustomUser
//@ActiveProfiles("test")
//@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
//class RankControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private RankService rankService;
//
//    @BeforeEach
//    void setUp(WebApplicationContext webApplicationContext,
//               RestDocumentationContextProvider restDocumentation) {
//
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .apply(documentationConfiguration(restDocumentation)
//                        .operationPreprocessors()
//                        .withRequestDefaults(modifyUris(), prettyPrint())
//                        .withResponseDefaults(prettyPrint()))
//                .build();
//    }
//
//    @Test
//    @DisplayName("전체 사용자 랭킹 반환 성공 api test")
//    void getAllRanks_Success() throws Exception {
//
//        List<RankResponse> mockRanks = Arrays.asList(
//                new RankResponse(1L, "userProfileImg1.png", 1, "nickName1", 100),
//                new RankResponse(2L, "userProfileImg2.png", 2, "nickName2", 90)
//        );
//
//        when(rankService.getRankedUsers()).thenReturn(mockRanks);
//
//        mockMvc.perform(get("/api/users/rank")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value(StatusEnum.SUCCESS.name()))
//                .andExpect(jsonPath("$.message").value("전체 랭킹 반환"))
//                .andExpect(jsonPath("$.data[0].userId").value(1))
//                .andExpect(jsonPath("$.data[0].rank").value(1))
//                .andExpect(jsonPath("$.data[0].nickName").value("nickName1"))
//                .andDo(document("get-all-ranks-success",
//                        responseFields(
//                                fieldWithPath("status").description("응답 상태 (SUCCESS)"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("exception").description("에러 메시지"),
//                                fieldWithPath("data[].userId").description("사용자의 고유 ID"),
//                                fieldWithPath("data[].profileImg").description("사용자의 프로필 이미지 URL"),
//                                fieldWithPath("data[].rank").description("사용자의 랭킹 순위"),
//                                fieldWithPath("data[].nickName").description("사용자의 닉네임"),
//                                fieldWithPath("data[].reward").description("사용자의 리워드")
//                        )
//                ));
//    }
//
//    @Test
//    @DisplayName("특정 사용자 랭킹 반환 성공 api test")
//    void getRankByUserId_Success() throws Exception {
//
//        RankResponse mockRank = new RankResponse(1L, "userProfileImg1.png", 1, "nickName1", 100);
//        when(rankService.getRankByUserId(1L)).thenReturn(mockRank);
//
//
//        mockMvc.perform(get("/api/users/me/rank")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value(StatusEnum.SUCCESS.name()))
//                .andExpect(jsonPath("$.message").value("사용자의 랭킹 반환"))
//                .andExpect(jsonPath("$.data.userId").value(1))
//                .andExpect(jsonPath("$.data.rank").value(1))
//                .andExpect(jsonPath("$.data.nickName").value("nickName1"))
//                .andDo(document("get-user-rank-success",
//                        responseFields(
//                                fieldWithPath("status").description("응답 상태 (SUCCESS)"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("exception").description("에러 메시지"),
//                                fieldWithPath("data.userId").description("사용자의 고유 ID"),
//                                fieldWithPath("data.profileImg").description("사용자의 프로필 이미지 URL"),
//                                fieldWithPath("data.rank").description("사용자의 랭킹 순위"),
//                                fieldWithPath("data.nickName").description("사용자의 닉네임"),
//                                fieldWithPath("data.reward").description("사용자의 리워드")
//                        )
//                ));
//    }
//}
