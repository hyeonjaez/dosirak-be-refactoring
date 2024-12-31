package com.example.dosirakbe.domain.elite.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.dosirakbe.annotations.WithMockCustomUser;
import com.example.dosirakbe.domain.elite.dto.EliteHistoryResponseDto;
import com.example.dosirakbe.domain.elite.service.EliteHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@WebMvcTest(controllers = EliteHistoryController.class)
@WithMockCustomUser
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class EliteHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EliteHistoryService eliteHistoryService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(modifyUris(), prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    @Test
    @DisplayName("사용자의 전체 히스토리를 조회 api test")
    void getEliteHistoryByUserIdTest() throws Exception {
        EliteHistoryResponseDto history1 = new EliteHistoryResponseDto(1L, 101L, 1001L, true, "문제1 설명", "문제1 정답");
        EliteHistoryResponseDto history2 = new EliteHistoryResponseDto(2L, 102L, 1001L, false, "문제2 설명", "문제2 정답");

        List<EliteHistoryResponseDto> historyList = List.of(history1, history2);

        when(eliteHistoryService.findEliteHistoryWithProblemByUserId(any(Long.class))).thenReturn(historyList);

        mockMvc.perform(get("/api/elite-histories/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("사용자의 히스토리 조회 성공"))
                .andExpect(jsonPath("$.data[0].problemId").value(101L))
                .andExpect(jsonPath("$.data[0].correct").value(true))
                .andExpect(jsonPath("$.data[0].problemDesc").value("문제1 설명"))
                .andExpect(jsonPath("$.data[0].problemAns").value("문제1 정답"))
                .andExpect(jsonPath("$.data[1].problemId").value(102L))
                .andExpect(jsonPath("$.data[1].correct").value(false))
                .andExpect(jsonPath("$.data[1].problemDesc").value("문제2 설명"))
                .andExpect(jsonPath("$.data[1].problemAns").value("문제2 정답"))
                .andDo(print())
                .andDo(document("elite-history-get-list",
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS 또는 FAIL)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception").description("에러 메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("사용자 히스토리 목록"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("히스토리 ID"),  // id 추가
                                fieldWithPath("data[].userId").type(JsonFieldType.NUMBER).description("사용자 ID"),  // userId 추가
                                fieldWithPath("data[].problemId").type(JsonFieldType.NUMBER).description("문제 ID"),
                                fieldWithPath("data[].correct").type(JsonFieldType.BOOLEAN).description("정답 여부"),
                                fieldWithPath("data[].problemDesc").type(JsonFieldType.STRING).description("문제 설명"),
                                fieldWithPath("data[].problemAns").type(JsonFieldType.STRING).description("문제 정답")
                        )
                ));
    }
}
