//package com.example.dosirakbe.domain.user_activity.controller;
//
//import com.example.dosirakbe.annotations.WithMockCustomUser;
//import com.example.dosirakbe.domain.user_activity.dto.response.UserActivityResponse;
//import com.example.dosirakbe.domain.user_activity.service.UserActivityService;
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
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.time.LocalDate;
//import java.time.YearMonth;
//import java.util.List;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
//import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
//import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
//import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
//import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(controllers = UserActivityController.class)
//@WithMockCustomUser
//@ActiveProfiles("test")
//@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
//class UserActivityControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserActivityService userActivityService;
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
//
//    @Test
//    @DisplayName("사용자의 월간 활동 요약을 조회 하는 api 테스트")
//    void getMonthlyActivitySummaryTest_monthly() throws Exception {
//        Long userId = 1L;
//        YearMonth yearMonth = YearMonth.of(2024, 8);
//        LocalDate localDate1 = LocalDate.of(2024, 8, 1);
//        LocalDate localDate2 = LocalDate.of(2024, 8, 31);
//
//        UserActivityResponse userActivityResponse1 = new UserActivityResponse(localDate1, 3);
//        UserActivityResponse userActivityResponse2 = new UserActivityResponse(localDate2, 2);
//
//        List<UserActivityResponse> activityResponseList = List.of(userActivityResponse1, userActivityResponse2);
//
//        when(userActivityService.getUserActivityList(userId, yearMonth)).thenReturn(activityResponseList);
//
//        mockMvc.perform(get("/api/user-activities/monthly")
//                        .param("month", "2024-08")
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.status").value("SUCCESS"))
//                .andExpect(jsonPath("$.message").value("Monthly Activity summary retrieved successfully"))
//                .andExpect(jsonPath("$.data[0].created_at").value(userActivityResponse1.getCreatedAt().toString()))
//                .andExpect(jsonPath("$.data[0].commit_count").value(userActivityResponse1.getCommitCount()))
//                .andExpect(jsonPath("$.data[1].created_at").value(userActivityResponse2.getCreatedAt().toString()))
//                .andExpect(jsonPath("$.data[1].commit_count").value(userActivityResponse2.getCommitCount()))
//                .andDo(print())
//                .andDo(document("user-activity-monthly-summary",
//                        queryParameters(
//                                parameterWithName("month").description("조회할 월을 나타내는 yyyy-MM 형식의 문자열 (선택 사항) - 값이 없으면 현재 월로 처리됩니다.")
//                        ),
//                        responseFields(
//                                fieldWithPath("status").description("응답 상태 (SUCCESS 또는 FAIL)"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("exception").description("에러 메시지"),
//                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("해당 월의 일별로 활동 수 목록 (1일 ~ 30,31 일)"),
//                                fieldWithPath("data[].created_at").type(JsonFieldType.STRING).description("활동이 기록된 날짜 (yyyy-MM-dd 형식)"),
//                                fieldWithPath("data[].commit_count").type(JsonFieldType.NUMBER).description("해당 날짜의 커밋 수")
//                        )
//                ));
//    }
//
//
//}