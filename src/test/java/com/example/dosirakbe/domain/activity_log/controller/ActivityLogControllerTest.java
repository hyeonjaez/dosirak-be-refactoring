package com.example.dosirakbe.domain.activity_log.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.example.dosirakbe.annotations.WithMockCustomUser;
import com.example.dosirakbe.domain.activity_log.dto.response.ActivityLogResponse;
import com.example.dosirakbe.domain.activity_log.service.ActivityLogService;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@WebMvcTest(controllers = ActivityLogController.class)
@WithMockCustomUser
@ActiveProfiles("test")
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class ActivityLogControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private ActivityLogService activityLogService;

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
    @DisplayName("특정 날짜의 활동 로그를 조회 api test")
    void getTodayActivityLogTest() throws Exception {
        LocalDateTime fixedDateTime1 = LocalDateTime.of(2024, 12, 24, 13, 50, 14, 79487);
        LocalDateTime fixedDateTime2 = LocalDateTime.of(2024, 12, 24, 13, 50, 14, 79488);

        ActivityLogResponse activityLogResponse1 = new ActivityLogResponse(
                fixedDateTime1,
                "다회용기 포장 인증",
                "01시 01분",
                "https://dosirakbucket.s3.ap-northeast-2.amazonaws.com/green_commit/commit_dosirak.png"
        );

        ActivityLogResponse activityLogResponse2 = new ActivityLogResponse(
                fixedDateTime2,
                "저탄소 이동수단 인증 - 1.12km",
                "01시 02분",
                "https://dosirakbucket.s3.ap-northeast-2.amazonaws.com/green_commit/commit_foot.png"
        );

        List<ActivityLogResponse> list = List.of(activityLogResponse1, activityLogResponse2);
        when(activityLogService.getThatDateActivityLog(any(Long.class), any(LocalDate.class))).thenReturn(list);

        mockMvc.perform(get("/api/activity-logs")
                        .param("date", "2024-12-23")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Activity history for today retrieved successfully"))
                .andExpect(jsonPath("$.data[0].created_at").value(activityLogResponse1.getCreatedAt().toString()))
                .andExpect(jsonPath("$.data[0].activity_message").value(activityLogResponse1.getActivityMessage()))
                .andExpect(jsonPath("$.data[0].create_at_time").value(activityLogResponse1.getCreateAtTime()))
                .andExpect(jsonPath("$.data[0].icon_image_url").value(activityLogResponse1.getIconImageUrl()))
                .andExpect(jsonPath("$.data[1].created_at").value(activityLogResponse2.getCreatedAt().toString()))
                .andExpect(jsonPath("$.data[1].activity_message").value(activityLogResponse2.getActivityMessage()))
                .andExpect(jsonPath("$.data[1].create_at_time").value(activityLogResponse2.getCreateAtTime()))
                .andExpect(jsonPath("$.data[1].icon_image_url").value(activityLogResponse2.getIconImageUrl()))
                .andDo(print())
                .andDo(document("activity-log-today-get-list",
                        queryParameters(
                                parameterWithName("date").description("조회할 날짜 (yyyy-MM-dd 형식) - 값이 없으면 오늘 날짜로 처리됩니다.")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS 또는 FAIL)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception").description("에러 메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("활동 로그 목록"),
                                fieldWithPath("data[].created_at").type(JsonFieldType.STRING).description("활동 로그 생성 날짜"),
                                fieldWithPath("data[].activity_message").type(JsonFieldType.STRING).description("활동 로그 메시지 (예: 다회용기 포장 인증, 저탄소 이동수단 인증 - (거리수)km)"),
                                fieldWithPath("data[].create_at_time").type(JsonFieldType.STRING).description("생성 시간 (예: 00시 00분)"),
                                fieldWithPath("data[].icon_image_url").type(JsonFieldType.STRING).description("활동 로그 아이콘 이미지 URL")
                        )
                ));
    }

    @Test
    @DisplayName("특정 월의 첫째 날 활동 로그 조회 api test")
    void getActivityLogForFirstDayOfMonthTest() throws Exception {
        LocalDateTime fixedDateTime1 = LocalDateTime.of(2024, 12, 24, 13, 50, 14, 79487);
        LocalDateTime fixedDateTime2 = LocalDateTime.of(2024, 12, 24, 13, 50, 14, 79488);
        ActivityLogResponse log1 = new ActivityLogResponse(
                fixedDateTime1,
                "다회용기 포장 인증",
                "10시 00분",
                "https://dosirakbucket.s3.ap-northeast-2.amazonaws.com/green_commit/commit_dosirak.png"
        );

        ActivityLogResponse log2 = new ActivityLogResponse(
                fixedDateTime2,
                "저탄소 이동수단 인증 - 1.12km",
                "11시 00분",
                "https://dosirakbucket.s3.ap-northeast-2.amazonaws.com/green_commit/commit_foot.png"
        );

        List<ActivityLogResponse> logs = List.of(log1, log2);

        when(activityLogService.getActivityLogForFirstDayOfMonth(any(Long.class), any(YearMonth.class))).thenReturn(logs);

        mockMvc.perform(get("/api/activity-logs/first-day/{month}", "2024-12")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Activity history for first date retrieved successfully"))
                .andExpect(jsonPath("$.data[0].created_at").value(log1.getCreatedAt().toString()))
                .andExpect(jsonPath("$.data[0].activity_message").value(log1.getActivityMessage()))
                .andExpect(jsonPath("$.data[0].create_at_time").value(log1.getCreateAtTime()))
                .andExpect(jsonPath("$.data[0].icon_image_url").value(log1.getIconImageUrl()))
                .andExpect(jsonPath("$.data[1].created_at").value(log2.getCreatedAt().toString()))
                .andExpect(jsonPath("$.data[1].activity_message").value(log2.getActivityMessage()))
                .andExpect(jsonPath("$.data[1].create_at_time").value(log2.getCreateAtTime()))
                .andExpect(jsonPath("$.data[1].icon_image_url").value(log2.getIconImageUrl()))
                .andDo(print())
                .andDo(document("activity-log-first-day",
                        pathParameters(
                                parameterWithName("month").description("조회할 월 (형식: YYYY-MM)")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS 또는 FAIL)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception").description("에러 메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("활동 로그 목록"),
                                fieldWithPath("data[].created_at").type(JsonFieldType.STRING).description("활동 로그 생성 날짜"),
                                fieldWithPath("data[].activity_message").type(JsonFieldType.STRING).description("활동 로그 메시지 (예: 다회용기 포장 인증, 저탄소 이동수단 인증 - (거리수)km)"),
                                fieldWithPath("data[].create_at_time").type(JsonFieldType.STRING).description("생성 시간 (예: 00시 00분)"),
                                fieldWithPath("data[].icon_image_url").type(JsonFieldType.STRING).description("활동 로그 아이콘 이미지 URL")
                        )
                ));
    }
}


