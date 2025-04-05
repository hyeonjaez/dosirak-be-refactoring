//package com.example.dosirakbe.domain.elite.controller;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
//import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
//import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import com.example.dosirakbe.annotations.WithMockCustomUser;
//import com.example.dosirakbe.domain.elite.dto.EliteInfoDto;
//import com.example.dosirakbe.domain.elite.service.EliteInfoService;
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
//import org.springframework.restdocs.payload.JsonFieldType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//@WebMvcTest(controllers = EliteInfoController.class)
//@WithMockCustomUser
//@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
//class EliteInfoControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private EliteInfoService eliteInfoService;
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
//    @DisplayName("사용자의 엘리트 정보를 조회 api test")
//    void getEliteInfoByUserIdTest() throws Exception {
//        EliteInfoDto eliteInfoDto = new EliteInfoDto(
//                1001L,                          // id
//                1001L,                          // userId
//                5,                              // correctAnswers
//                3,                              // incorrectAnswers
//                10,                             // totalAnswers
//                LocalDateTime.now(),            // lastSolvedDate (현재 시간)
//                "사용자 엘리트 정보 설명"           // description
//        );
//        // EliteInfoService의 findEliteInfoByUserId 메서드를 모킹하여 반환 값 설정
//        when(eliteInfoService.findEliteInfoByUserId(any(Long.class))).thenReturn(Optional.of(eliteInfoDto));
//
//        mockMvc.perform(get("/api/elite-infos/user")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("SUCCESS"))
//                .andExpect(jsonPath("$.message").value("사용자 정보 조회 성공"))
//                .andExpect(jsonPath("$.data.userId").value(1001L))
//                .andExpect(jsonPath("$.data.totalAnswers").value(10))
//                .andExpect(jsonPath("$.data.correctAnswers").value(5))
//                .andExpect(jsonPath("$.data.incorrectAnswers").value(3))
//                .andDo(print())
//                .andDo(document("elite-info-get",
//                        responseFields(
//                                fieldWithPath("status").description("응답 상태 (SUCCESS 또는 FAIL)"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("exception").description("에러 메시지"),
//                                fieldWithPath("data").description("사용자 엘리트 정보"),
//                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("엘리트 정보 ID"),  // id 추가
//                                fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("사용자 ID"),
//                                fieldWithPath("data.totalAnswers").type(JsonFieldType.NUMBER).description("총 문제 수"),
//                                fieldWithPath("data.correctAnswers").type(JsonFieldType.NUMBER).description("정답 문제 수"),
//                                fieldWithPath("data.incorrectAnswers").type(JsonFieldType.NUMBER).description("오답 문제 수"),
//                                fieldWithPath("data.lastSolvedDate").type(JsonFieldType.STRING).description("마지막 풀이 날짜"),  // lastSolvedDate 추가
//                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("사용자 엘리트 정보 설명")  // description 추가
//                        )
//                ));
//    }
//
//    @Test
//    @DisplayName("사용자의 엘리트 정보를 조회할 수 없는 경우 api test")
//    void getEliteInfoByUserIdNotFoundTest() throws Exception {
//        // 사용자 정보를 찾을 수 없을 경우
//        when(eliteInfoService.findEliteInfoByUserId(any(Long.class))).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/api/elite-infos/user")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.status").value("ERROR"))
//                .andExpect(jsonPath("$.message").value("해당 사용자 정보를 찾을 수 없습니다"))
//                .andDo(print())
//                .andDo(document("elite-info-get-not-found",
//                        responseFields(
//                                fieldWithPath("status").description("응답 상태 (SUCCESS 또는 FAIL)"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("exception").description("에러 메시지"),
//                                fieldWithPath("data").description("사용자 엘리트 정보 (없을 경우 null)")
//                        )
//                ));
//    }
//}
