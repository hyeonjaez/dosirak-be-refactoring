//package com.example.dosirakbe.domain.auth.controller;
//
//import com.example.dosirakbe.annotations.WithMockCustomUser;
//import com.example.dosirakbe.domain.auth.dto.response.TokenResponse;
//import com.example.dosirakbe.domain.auth.service.TokenService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.restdocs.RestDocumentationContextProvider;
//import org.springframework.restdocs.RestDocumentationExtension;
//import org.springframework.security.oauth2.jwt.JwtException;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
//import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
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
//
//@WebMvcTest(controllers = TokenController.class)
//@WithMockCustomUser
//@ActiveProfiles("test")
//@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
//class TokenControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private TokenService tokenService;
//
//    @MockBean
//    private JwtUtil jwtUtil;
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
//    @DisplayName("토큰 재발급 성공 api test")
//    void reissueAccessToken_Success() throws Exception {
//
//        TokenResponse tokenResponse = new TokenResponse("newAccessToken");
//        when(tokenService.reissueAccessToken()).thenReturn(tokenResponse);
//
//
//        mockMvc.perform(get("/api/tokens"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value(StatusEnum.SUCCESS.toString()))
//                .andExpect(jsonPath("$.message").value("토큰 재발급에 성공하였습니다"))
//                .andExpect(jsonPath("$.data.accessToken").value("newAccessToken"))
//                .andDo(document("reissue-access-token-success",
//                        responseFields(
//                                fieldWithPath("status").description("응답 상태 (SUCCESS)"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("exception").description("에러 메시지"),
//                                fieldWithPath("data.accessToken").description("재발급된 액세스 토큰")
//                        )
//                ));
//
//    }
//
//    @Test
//    @DisplayName("토큰 재발급 실패 api test")
//    void reissueAccessToken_Failure() throws Exception {
//
//        when(tokenService.reissueAccessToken()).thenThrow(new JwtException("토큰 재발급 실패"));
//
//
//        mockMvc.perform(get("/api/tokens"))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.status").value(StatusEnum.FAILURE.toString()))
//                .andExpect(jsonPath("$.message").value("토큰 재발급에 실패하였습니다"))
//                .andDo(document("reissue-access-token-failure",
//                        responseFields(
//                                fieldWithPath("status").description("응답 상태 (FAILURE)"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("exception").description("예외 메시지 (JWT 토큰 관련 오류)"),
//                                fieldWithPath("data").description("null")
//                        )
//                ));
//
//
//    }
//
//
//    @Test
//    @DisplayName("유효한 토큰 검증 api test")
//    void validateToken_Success() throws Exception {
//
//        String validToken = "validToken";
//        when(jwtUtil.getTokenFromHeader("Bearer " + validToken)).thenReturn(validToken);
//        when(jwtUtil.validToken(validToken)).thenReturn(true);
//
//        mockMvc.perform(get("/api/tokens/validate")
//                        .header("Authorization", "Bearer " + validToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value(StatusEnum.SUCCESS.toString()))
//                .andExpect(jsonPath("$.message").value("유효한 토큰입니다."))
//                .andDo(document("validate-token-success",
//                        requestHeaders(
//                                headerWithName("Authorization").description("Bearer 인증 토큰")
//                        ),
//                        responseFields(
//                                fieldWithPath("status").description("응답 상태 (SUCCESS)"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("exception").description("에러 메시지"),
//                                fieldWithPath("data").description("null")
//                        )
//                ));
//
//        verify(jwtUtil, times(1)).getTokenFromHeader("Bearer " + validToken);
//        verify(jwtUtil, times(1)).validToken(validToken);
//    }
//
//    @Test
//    @DisplayName("유효하지 않은 토큰 검증 api test")
//    void validateToken_Failure_InvalidToken() throws Exception {
//
//        String invalidToken = "invalidToken";
//        when(jwtUtil.getTokenFromHeader("Bearer " + invalidToken)).thenReturn(invalidToken);
//        when(jwtUtil.validToken(invalidToken)).thenReturn(false);
//
//        mockMvc.perform(get("/api/tokens/validate")
//                        .header("Authorization", "Bearer " + invalidToken))
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.status").value(StatusEnum.FAILURE.toString()))
//                .andExpect(jsonPath("$.message").value("유효하지 않은 토큰입니다."))
//                .andDo(document("validate-token-failure-invalid-token",
//                        requestHeaders(
//                                headerWithName("Authorization").description("Bearer 인증 토큰")
//                        ),
//                        responseFields(
//                                fieldWithPath("status").description("응답 상태 (FAILURE)"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("exception").description("에러 메시지"),
//                                fieldWithPath("data").description("null")
//                        )
//                ));
//
//        verify(jwtUtil, times(1)).getTokenFromHeader("Bearer " + invalidToken);
//        verify(jwtUtil, times(1)).validToken(invalidToken);
//    }
//
//
//    @Test
//    @DisplayName("토큰 검증 api test - Authorization 헤더가 잘못된 경우")
//    void validateToken_Failure_InvalidHeader() throws Exception {
//
//        when(jwtUtil.getTokenFromHeader(anyString())).thenThrow(new IllegalArgumentException("잘못된 Authorization 헤더"));
//
//        mockMvc.perform(get("/api/tokens/validate")
//                        .header("Authorization", "InvalidHeader"))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.status").value(StatusEnum.FAILURE.toString()))
//                .andExpect(jsonPath("$.message").value("잘못된 요청입니다: 잘못된 Authorization 헤더"))
//                .andDo(document("validate-token-failure-invalid-header",
//                        requestHeaders(
//                                headerWithName("Authorization").description("잘못된 Authorization 헤더 값")
//                        ),
//                        responseFields(
//                                fieldWithPath("status").description("응답 상태 (FAILURE)"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("exception").description("에러 메시지"),
//                                fieldWithPath("data").description("null")
//                        )
//                ));
//
//        verify(jwtUtil, times(1)).getTokenFromHeader(anyString());
//    }
//
//
//
//
//}