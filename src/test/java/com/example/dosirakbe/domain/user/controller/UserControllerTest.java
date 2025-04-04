package com.example.dosirakbe.domain.user.controller;

import com.example.dosirakbe.annotations.WithMockCustomUser;
import com.example.dosirakbe.domain.auth.dto.KakaoUserInfo;
import com.example.dosirakbe.domain.auth.repository.RefreshTokenRepository;
import com.example.dosirakbe.domain.auth.service.CustomOAuth2UserService;
import com.example.dosirakbe.domain.user.dto.request.NickNameRequest;
import com.example.dosirakbe.domain.user.dto.response.UserProfileResponse;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.mockito.ArgumentMatchers.eq;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@WithMockCustomUser
@ActiveProfiles("test")
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CustomOAuth2UserService customOAuth2UserService;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    private User mockUser;


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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setNickName("환경");
        mockUser.setEmail("leeyujin1231@naver.com");
        mockUser.setName("이유진");
        mockUser.setProfileImg("https://example.com/profile.jpg");
        mockUser.setCreatedAt(LocalDateTime.now());
        mockUser.setUpdatedAt(LocalDateTime.now());
        mockUser.setUserValid(true);
    }


    @Test
    @DisplayName("소셜 인증 회원가입 성공 api test")
    void authenticateUserTest() throws Exception {

        Map<String, Object> mockKakaoUserData = new HashMap<>();
        mockKakaoUserData.put("provider", "Kakao");
        mockKakaoUserData.put("providerId", "3757381275");
        mockKakaoUserData.put("name", "이유진");
        mockKakaoUserData.put("email", "leeyujin1231@naver.com");
        mockKakaoUserData.put("profileImg", "https://profile.kakao.com.png");

        KakaoUserInfo mockKakaoUserInfo = new KakaoUserInfo(mockKakaoUserData);


        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", "mockAccessToken");
        tokens.put("refreshToken", "mockRefreshToken");


        when(customOAuth2UserService.processKakaoToken("mockSocialAccessToken")).thenReturn(mockKakaoUserInfo);
        when(userService.registerUser(any())).thenReturn(tokens);

        mockMvc.perform(post("/api/users")
                        .header("Authorization", "Bearer mockSocialAccessToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("소셜 로그인 성공"))
                .andExpect(jsonPath("$.data.accessToken").value("mockAccessToken"))
                .andExpect(jsonPath("$.data.refreshToken").value("mockRefreshToken"))
                .andDo(document("authenticate-user",
                        requestHeaders(
                                headerWithName("Authorization").description("소셜 인증 토큰")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception").description("예외 메시지"),
                                fieldWithPath("data.accessToken").description("발급된 액세스 토큰"),
                                fieldWithPath("data.refreshToken").description("발급된 리프레시 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("소셜 인증 회원가입 실패 api test - 유효하지 않은 토큰")
    void authenticateUser_Failure_InvalidToken() throws Exception {
        Mockito.when(customOAuth2UserService.processKakaoToken("invalidToken"))
                .thenThrow(new IllegalArgumentException("틀린 혹은 만료된 토큰입니다."));

        mockMvc.perform(post("/api/users")
                        .header("Authorization", "Bearer invalidToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("FAILURE"))
                .andExpect(jsonPath("$.message").value("틀린 혹은 만료된 토큰입니다"))
                .andDo(document("authenticate-user-failure-invalid-token",
                        requestHeaders(
                                headerWithName("Authorization").description("유효하지 않은 소셜 인증 토큰")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (FAILURE)"),
                                fieldWithPath("message").description("에러 메시지"),
                                fieldWithPath("exception").description("예외 메시지"),
                                fieldWithPath("data").description("null")
                        )
                ));
    }


    @Test
    @DisplayName("로그아웃 성공 api test")
    void testLogoutUser_Success() throws Exception {

        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(Map.of(
                "id", "12345",
                "provider", "Kakao"
        ));

        Mockito.when(customOAuth2UserService.processKakaoToken("validToken")).thenReturn(kakaoUserInfo);

        Mockito.when(userRepository.findByUserName("kakao 12345"))
                .thenReturn(Optional.of(mockUser));


        Mockito.doNothing().when(userService).processKakaoLogout("validToken", 1L);


        mockMvc.perform(post("/api/users/logout")
                        .header("Authorization", "Bearer validToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("로그아웃 성공"))
                .andDo(document("logout-success",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 토큰 형식의 소셜 인증 토큰")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception").description("예외 메시지"),
                                fieldWithPath("data").description("null")


                        )
                ));


        Mockito.verify(userService).processKakaoLogout("validToken", 1L);
    }
    @Test
    @DisplayName("로그아웃 실패 api test - 유효하지 않은 토큰")
    void testLogoutUser_Failure_InvalidToken() throws Exception {

        Mockito.when(customOAuth2UserService.processKakaoToken("invalidToken"))
                .thenThrow(new ApiException(ExceptionEnum.INVALID_ACCESS_TOKEN));

        mockMvc.perform(post("/api/users/logout")
                        .header("Authorization", "Bearer invalidToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("FAILURE"))
                .andExpect(jsonPath("$.message").value("유효하지 않은 accessToken입니다."))
                .andDo(document("logout-failure-invalid-token",
                        requestHeaders(
                                headerWithName("Authorization").description("유효하지 않은 소셜 인증 토큰")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (FAILURE)"),
                                fieldWithPath("message").description("에러 메시지"),
                                fieldWithPath("exception").description("예외 메시지"),
                                fieldWithPath("data").description("null")
                        )
                ));
    }

    @Test
    @DisplayName("회원탈퇴 성공 api test")
    void testWithdrawUser_Success() throws Exception {

        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(Map.of(
                "id", "12345",
                "provider", "kakao"
        ));
        Mockito.when(customOAuth2UserService.processKakaoToken("validToken"))
                .thenReturn(kakaoUserInfo);


        Mockito.when(userRepository.findByUserName("kakao 12345"))
                .thenReturn(Optional.of(mockUser));


        Mockito.doNothing().when(userService).processKakaoWithdraw("validToken", 1L);

        mockMvc.perform(post("/api/users/withdraw")
                        .header("Authorization", "Bearer validToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("회원 탈퇴가 완료되었습니다."))
                .andDo(document("withdraw-success",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 토큰 형식의 소셜 인증 토큰")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception").description("예외 메시지"),
                                fieldWithPath("data").description("null")


                        )
                ));



        Mockito.verify(userService).processKakaoWithdraw("validToken", 1L);
    }







    @Test
    @DisplayName("닉네임 생성 성공 api test")
    void testGenerateNickname_Success() throws Exception {
        User mockUser = new User();
        mockUser.setNickName("NewNickname");

        Mockito.when(userService.updateNickName(eq(1L), eq("NewNickname"))).thenReturn(mockUser);


        NickNameRequest nickNameRequest = new NickNameRequest();
        nickNameRequest.setNickName("NewNickname");


        mockMvc.perform(post("/api/users/nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nickNameRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("닉네임이 성공적으로 저장되었습니다."))
                .andExpect(jsonPath("$.data.nickName").value("NewNickname"))
                .andDo(document("generate-nickname-success",
                        requestFields(
                                fieldWithPath("nickName").description("사용자가 설정하려는 새 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception").description("예외 메시지"),
                                fieldWithPath("data.nickName").description("저장된 닉네임")
                        )
                ));
    }

    @Test
    @DisplayName("닉네임 생성 실패 api test - 닉네임이 중복된 경우")
    void testGenerateNickname_Duplicate() throws Exception {
        Mockito.when(userService.updateNickName(eq(1L), eq("DuplicateNickname")))
                .thenThrow(new IllegalArgumentException("닉네임이 중복됩니다."));

        NickNameRequest nickNameRequest = new NickNameRequest();
        nickNameRequest.setNickName("DuplicateNickname");

        mockMvc.perform(post("/api/users/nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nickNameRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("FAILURE"))
                .andExpect(jsonPath("$.message").value("닉네임이 중복됩니다."))
                .andDo(document("generate-nickname-duplicate",
                        requestFields(
                                fieldWithPath("nickName").description("사용자가 설정하려는 닉네임 (중복된 경우)")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (FAILURE)"),
                                fieldWithPath("exception").description("예외 메시지"),
                                fieldWithPath("message").description("응답 메세지"),
                                fieldWithPath("data").description("null")

                        )
                ));
    }


    @Test
    @DisplayName("닉네임 변경 성공 api test")
    void testUpdateNickname_Success() throws Exception {

        User mockUser = new User();
        mockUser.setNickName("UpdatedNickname");

        Mockito.when(userService.updateNickName(eq(1L), eq("UpdatedNickname"))).thenReturn(mockUser);


        NickNameRequest nickNameRequest = new NickNameRequest();
        nickNameRequest.setNickName("UpdatedNickname");

        mockMvc.perform(put("/api/users/nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nickNameRequest))
                        .header("Authorization", "Bearer validToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("닉네임이 성공적으로 변경되었습니다."))
                .andExpect(jsonPath("$.data.nickName").value("UpdatedNickname"))
                .andDo(document("update-nickname-success",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 형식의 인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("nickName").description("사용자가 설정하려는 새로운 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception").description("예외 메시지"),
                                fieldWithPath("data.nickName").description("변경된 닉네임")
                        )
                ));
    }

    @Test
    @DisplayName("닉네임 변경 실패 api test - 중복된 닉네임")
    void testUpdateNickname_Failure_Duplicate() throws Exception {

        Mockito.when(userService.updateNickName(eq(1L), eq("DuplicateNickname")))
                .thenThrow(new IllegalArgumentException("중복된 닉네임입니다."));


        NickNameRequest nickNameRequest = new NickNameRequest();
        nickNameRequest.setNickName("DuplicateNickname");


        mockMvc.perform(put("/api/users/nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nickNameRequest))
                        .header("Authorization", "Bearer validToken"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("FAILURE"))
                .andExpect(jsonPath("$.message").value("중복된 닉네임입니다."))
                .andDo(document("update-nickname-failure-duplicate",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 형식의 인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("nickName").description("중복된 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (FAILURE)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception").description("예외 메시지"),
                                fieldWithPath("data").description("null")
                        )
                ));
    }

    @Test
    @DisplayName("유저 프로필 반환 성공 api test")
    void testGetUserProfile_Success() throws Exception {
        UserProfileResponse mockProfile = new UserProfileResponse(
                "NewNickname",
                "leeyuin1231@naver.com",
                "이유진",
                LocalDateTime.parse("2024-12-28T00:00:00"),
                100
        );

        Mockito.when(userService.getUserProfile(eq(1L))).thenReturn(mockProfile);

        mockMvc.perform(get("/api/users/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer validToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("프로필 정보를 반환하였습니다"))
                .andExpect(jsonPath("$.data.nickName").value("NewNickname"))
                .andExpect(jsonPath("$.data.email").value("leeyuin1231@naver.com"))
                .andExpect(jsonPath("$.data.name").value("이유진"))
                .andExpect(jsonPath("$.data.createdAt").value("2024-12-28T00:00:00"))
                .andExpect(jsonPath("$.data.reward").value(100))
                .andDo(document("get-user-profile-success",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 형식의 인증 토큰")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception").description("예외 메시지"),
                                fieldWithPath("data.nickName").description("유저의 닉네임"),
                                fieldWithPath("data.email").description("유저의 이메일"),
                                fieldWithPath("data.name").description("유저의 이름"),
                                fieldWithPath("data.createdAt").description("유저의 계정 생성일"),
                                fieldWithPath("data.reward").description("유저의 보유 포인트")
                        )
                ));
    }

}
