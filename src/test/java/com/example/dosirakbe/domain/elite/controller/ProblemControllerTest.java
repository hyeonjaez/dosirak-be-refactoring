package com.example.dosirakbe.domain.elite.controller;

import com.example.dosirakbe.annotations.WithMockCustomUser;
import com.example.dosirakbe.domain.elite.dto.ProblemDto;
import com.example.dosirakbe.domain.elite.service.ProblemService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProblemController.class)
@WithMockCustomUser
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class ProblemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProblemService problemService;

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
    @DisplayName("사용자가 풀지 않은 문제 랜덤 조회 성공")
    void testGetRandomProblemNotSolvedByUser_success() throws Exception {
        ProblemDto problemDto = ProblemDto.builder()
                .id(1L)
                .description("문제 설명")
                .answer("정답")
                .build();

        when(problemService.getRandomProblemNotSolvedByUser(any(Long.class))).thenReturn(problemDto);

        mockMvc.perform(get("/api/problems/random")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("사용자가 풀지 않은 문제 랜덤 조회 성공"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.description").value("문제 설명"))
                .andExpect(jsonPath("$.data.answer").value("정답"))
                .andDo(print())
                .andDo(document("problem-random-get",
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS 또는 FAIL)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception").type(JsonFieldType.STRING).optional().description("에러 메시지 (없을 경우 null)"),
                                fieldWithPath("data").description("문제 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("문제 ID"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("문제 설명"),
                                fieldWithPath("data.answer").type(JsonFieldType.STRING).description("문제 정답")
                        )
                ));
    }

    @Test
    @DisplayName("사용자가 풀지 않은 문제가 없는 경우")
    void testGetRandomProblemNotSolvedByUser_noProblem() throws Exception {
        when(problemService.getRandomProblemNotSolvedByUser(any(Long.class))).thenReturn(null);

        mockMvc.perform(get("/api/problems/random")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("사용자가 풀지 않은 문제가 없습니다."))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(print())
                .andDo(document("problem-random-get-empty",
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS 또는 FAIL)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception").type(JsonFieldType.STRING).optional().description("에러 메시지 (없을 경우 null)"),
                                fieldWithPath("data").description("데이터 없음 (null)").optional()
                        )
                ));
    }

    @Test
    @DisplayName("문제 ID로 문제 조회 성공")
    void testGetProblemById_success() throws Exception {
        ProblemDto problemDto = ProblemDto.builder()
                .id(1L)
                .description("문제 설명")
                .answer("정답")
                .build();

        when(problemService.findProblemById(any(Long.class))).thenReturn(problemDto);

        mockMvc.perform(get("/api/problems/{problemId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("문제 조회 성공"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.description").value("문제 설명"))
                .andExpect(jsonPath("$.data.answer").value("정답"))
                .andDo(print())
                .andDo(document("problem-by-id-get",
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS 또는 FAIL)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception").type(JsonFieldType.STRING).optional().description("에러 메시지 (없을 경우 null)"),
                                fieldWithPath("data").description("문제 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("문제 ID"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("문제 설명"),
                                fieldWithPath("data.answer").type(JsonFieldType.STRING).description("문제 정답")
                        )
                ));
    }

    @Test
    @DisplayName("문제 ID로 문제 조회 실패")
    void testGetProblemById_notFound() throws Exception {
        when(problemService.findProblemById(any(Long.class))).thenReturn(null);

        mockMvc.perform(get("/api/problems/{problemId}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("FAILURE"))
                .andExpect(jsonPath("$.message").value("해당 문제를 찾을 수 없습니다."))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(print())
                .andDo(document("problem-by-id-get-not-found",
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS 또는 FAIL)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception").type(JsonFieldType.STRING).optional().description("에러 메시지 (없을 경우 null)"),
                                fieldWithPath("data").description("데이터 없음 (null)").optional()
                        )
                ));
    }
}
