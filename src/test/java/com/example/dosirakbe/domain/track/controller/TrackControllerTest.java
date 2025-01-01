package com.example.dosirakbe.domain.track.controller;

import com.example.dosirakbe.annotations.WithMockCustomUser;
import com.example.dosirakbe.domain.track.dto.request.TrackMoveRequest;
import com.example.dosirakbe.domain.track.dto.response.TrackMoveResponse;
import com.example.dosirakbe.domain.track.service.TrackService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TrackController.class)
@WithMockCustomUser
@ActiveProfiles("test")
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class TrackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrackService trackService;

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

    @DisplayName("사용자의 이동 거리를 기록하고 저장하는 api 테스트 - 성공 shortestDistance < moveDistance 일때")
    @Test
    void recordMovingDistanceTest() throws Exception {
        TrackMoveRequest trackMoveRequest = new TrackMoveRequest();

        ObjectMapper objectMapper = new ObjectMapper();
        ReflectionTestUtils.setField(trackMoveRequest, "shortestDistance", new BigDecimal("1.1"));
        ReflectionTestUtils.setField(trackMoveRequest, "moveDistance", new BigDecimal("1.2"));
        ReflectionTestUtils.setField(trackMoveRequest, "saleStoreName", "한솥");

        TrackMoveResponse trackMoveResponse = new TrackMoveResponse(new BigDecimal("1.2"));

        when(trackService.recordTrackDistance(any(Long.class), any(TrackMoveRequest.class))).thenReturn(trackMoveResponse);

        String requestJson = objectMapper.writeValueAsString(trackMoveRequest);

        mockMvc.perform(post("/api/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("track move distance successful"))
                .andExpect(jsonPath("$.data.move_track_distance").value(trackMoveResponse.getMoveTrackDistance()))
                .andDo(document("track-record-move-distance-success",
                        requestFields(
                                fieldWithPath("shortest_distance").type(JsonFieldType.NUMBER).description("사용자의 최단 이동 거리 (최대 10자리 정수, 소수점 이하 2자리)"),
                                fieldWithPath("move_distance").type(JsonFieldType.NUMBER).description("사용자의 실제 이동 거리 (최대 10자리 정수, 소수점 이하 2자리)"),
                                fieldWithPath("sale_store_name").type(JsonFieldType.STRING).description("사용자가 이동한 판매점의 이름")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS 또는 FAILURE)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception").description("에러 메세지"),
                                fieldWithPath("data").description("기록된 이동 거리 정보"),
                                fieldWithPath("data.move_track_distance").description("사용자가 실제 이동한 거리")
                        )
                ));
    }

    @DisplayName("이동 거리 차이가 GAP_DISTANCE를 초과할 경우 실패 테스트")
    @Test
    void recordMovingDistance_GapDistanceExceeded_Fail() throws Exception {
        TrackMoveRequest trackMoveRequest = new TrackMoveRequest();

        ObjectMapper objectMapper = new ObjectMapper();
        ReflectionTestUtils.setField(trackMoveRequest, "shortestDistance", new BigDecimal("1.0"));
        ReflectionTestUtils.setField(trackMoveRequest, "moveDistance", new BigDecimal("2.2"));
        ReflectionTestUtils.setField(trackMoveRequest, "saleStoreName", "한솥");

        String requestJson = objectMapper.writeValueAsString(trackMoveRequest);

        mockMvc.perform(post("/api/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value(""))
                .andExpect(jsonPath("$.exception.errorCode").value("CE0004"))
                .andExpect(jsonPath("$.exception.errorMessage").value("잘못된 요청입니다"))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(document("track-record-move-distance-gap-distance-exceeded-failure",
                        requestFields(
                                fieldWithPath("shortest_distance").type(JsonFieldType.NUMBER).description("사용자의 최단 이동 거리 (최대 10자리 정수, 소수점 이하 2자리)"),
                                fieldWithPath("move_distance").type(JsonFieldType.NUMBER).description("사용자의 실제 이동 거리 (최대 10자리 정수, 소수점 이하 2자리)"),
                                fieldWithPath("sale_store_name").type(JsonFieldType.STRING).description("사용자가 이동한 판매점의 이름")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS 또는 FAILURE)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception.errorCode").description("에러 코드"),
                                fieldWithPath("exception.errorMessage").description("에러 메시지"),
                                fieldWithPath("data").description("기록된 이동 거리 정보").optional()
                        )
                ));
    }

    @DisplayName("필수 필드가 누락된 경우 실패 테스트")
    @Test
    void recordMovingDistance_MissingRequiredField_Failure() throws Exception {
        TrackMoveRequest trackMoveRequest = new TrackMoveRequest();

        ObjectMapper objectMapper = new ObjectMapper();
        ReflectionTestUtils.setField(trackMoveRequest, "moveDistance", new BigDecimal("1.2"));
        ReflectionTestUtils.setField(trackMoveRequest, "saleStoreName", "한솥");

        String requestJson = objectMapper.writeValueAsString(trackMoveRequest);

        mockMvc.perform(post("/api/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.exception.errorCode").value("E0003"))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(document("track-record-move-distance-missing-required-field-failure",
                        requestFields(
                                fieldWithPath("shortest_distance").type(JsonFieldType.NUMBER).description("사용자의 최단 이동 거리 (필수)").optional(),
                                fieldWithPath("move_distance").type(JsonFieldType.NUMBER).description("사용자의 실제 이동 거리 (최대 10자리 정수, 소수점 이하 2자리)"),
                                fieldWithPath("sale_store_name").type(JsonFieldType.STRING).description("사용자가 이동한 판매점의 이름")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS 또는 FAILURE)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception.errorCode").description("에러 코드"),
                                fieldWithPath("exception.errorMessage").description("에러 메시지"),
                                fieldWithPath("data").description("기록된 이동 거리 정보").optional()
                        )
                ));
    }

    @DisplayName("잘못된 데이터 형식의 필드가 전달된 경우 실패 테스트")
    @Test
    void recordMovingDistance_InvalidFieldFormat_Fail() throws Exception {
        String invalidRequestJson = "{"
                + "\"shortest_distance\": 1.0,"
                + "\"move_distance\": \"invalid_number\","
                + "\"sale_store_name\": \"한솥\""
                + "}";

        mockMvc.perform(post("/api/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value(""))
                .andExpect(jsonPath("$.exception.errorCode").value("E0001"))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(document("track-record-move-distance-invalid-field-format-failure",
                        requestFields(
                                fieldWithPath("shortest_distance").type(JsonFieldType.NUMBER).description("사용자의 최단 이동 거리 (최대 10자리 정수, 소수점 이하 2자리)"),
                                fieldWithPath("move_distance").type(JsonFieldType.STRING).description("사용자의 실제 이동 거리 (숫자 형식이어야 함)"),
                                fieldWithPath("sale_store_name").type(JsonFieldType.STRING).description("사용자가 이동한 판매점의 이름")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS 또는 FAILURE)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception.errorCode").description("에러 코드"),
                                fieldWithPath("exception.errorMessage").description("에러 메시지"),
                                fieldWithPath("data").description("기록된 이동 거리 정보").optional()
                        )
                ));
    }




}