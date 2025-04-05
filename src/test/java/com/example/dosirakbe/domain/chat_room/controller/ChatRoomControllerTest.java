//package com.example.dosirakbe.domain.chat_room.controller;
//
//import com.example.dosirakbe.annotations.WithMockCustomUser;
//import com.example.dosirakbe.domain.chat_room.dto.request.ChatRoomRegisterRequest;
//import com.example.dosirakbe.domain.chat_room.dto.response.*;
//import com.example.dosirakbe.domain.chat_room.service.ChatRoomService;
//import com.example.dosirakbe.domain.message.dto.response.MessageResponse;
//import com.example.dosirakbe.domain.message.entity.MessageType;
//import com.example.dosirakbe.domain.user.dto.response.UserChatRoomResponse;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.restdocs.RestDocumentationContextProvider;
//import org.springframework.restdocs.RestDocumentationExtension;
//import org.springframework.restdocs.payload.JsonFieldType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
//import static org.springframework.restdocs.payload.PayloadDocumentation.*;
//import static org.springframework.restdocs.request.RequestDocumentation.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(controllers = ChatRoomController.class)
//@WithMockCustomUser
//@ActiveProfiles("test")
//@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
//class ChatRoomControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ChatRoomService chatRoomService;
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
//    @DisplayName("createChatRoom - 성공")
//    @Test
//    void createChatRoomTest() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        ChatRoomRegisterRequest chatRoomRegisterRequest = new ChatRoomRegisterRequest();
//        ReflectionTestUtils.setField(chatRoomRegisterRequest, "title", "제목");
//        ReflectionTestUtils.setField(chatRoomRegisterRequest, "explanation", "설명");
//        ReflectionTestUtils.setField(chatRoomRegisterRequest, "zoneCategoryName", "역삼동");
//        ReflectionTestUtils.setField(chatRoomRegisterRequest, "defaultImage", "이미지");
//
//        MockMultipartFile chatRoomPart = new MockMultipartFile(
//                "chatRoom",
//                "",
//                "application/json",
//                objectMapper.writeValueAsBytes(chatRoomRegisterRequest)
//        );
//
//        MockMultipartFile filePart = new MockMultipartFile(
//                "file",
//                "image.png",
//                MediaType.IMAGE_PNG_VALUE,
//                "fake-image-content".getBytes()
//        );
//
//        ChatRoomResponse chatRoomResponse = new ChatRoomResponse(1L, "제목", 1L);
//
//        when(chatRoomService.createChatRoom(any(MultipartFile.class), any(ChatRoomRegisterRequest.class), any(Long.class)))
//                .thenReturn(chatRoomResponse);
//
//        mockMvc.perform(multipart("/api/chat-rooms")
//                        .file(chatRoomPart)
//                        .file(filePart)
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.status").value(StatusEnum.SUCCESS.name()))
//                .andExpect(jsonPath("$.message").value("Chat room created successfully"))
//                .andExpect(jsonPath("$.data").isMap())
//                .andExpect(jsonPath("$.data.id").value(chatRoomResponse.getId()))
//                .andExpect(jsonPath("$.data.title").value(chatRoomResponse.getTitle()))
//                .andExpect(jsonPath("$.data.person_count").value(chatRoomResponse.getPersonCount()))
//                .andDo(document("create-chatroom-success",
//                        requestParts(
//                                partWithName("chatRoom").description("채팅방 생성 요청 데이터"),
//                                partWithName("file").description("채팅방 대표 이미지 파일 (선택 사항)").optional()
//                        ),
//                        responseFields(
//                                fieldWithPath("status").description("응답 상태 (SUCCESS 또는 FAILURE)"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("exception").description("에러 메시지"),
//                                fieldWithPath("data").description("생성된 채팅방 정보"),
//                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("채팅방의 고유 ID"),
//                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("채팅방의 제목"),
//                                fieldWithPath("data.person_count").type(JsonFieldType.NUMBER).description("채팅방의 현재 인원")
//                        )
//                ));
//    }
//
//
//    @DisplayName("getChatRoomInformation - 성공")
//    @Test
//    void getChatRoomInformationTest() throws Exception {
//        Long chatRoomId = 1L;
//        ChatRoomInformationResponse chatRoomInformationResponse = new ChatRoomInformationResponse(
//                5L,
//                "chat room explanation",
//                List.of(
//                        new MessageResponse(10L, "안녕하세요", MessageType.CHAT,
//                                LocalDateTime.of(2024, 6, 27, 12, 0, 0),
//                                new UserChatRoomResponse(1L, "jaehyeon", "profileImage1"),
//                                1L),
//                        new MessageResponse(11L, "haein님이 들어왔습니다", MessageType.JOIN,
//                                LocalDateTime.of(2024, 8, 8, 12, 0, 0),
//                                new UserChatRoomResponse(2L, "haein", "profileImage2"),
//                                1L)
//                ),
//                List.of(
//                        new UserChatRoomResponse(10L, "jaehyeon", "profileImage1"),
//                        new UserChatRoomResponse(11L, "haein", "profileImage2"),
//                        new UserChatRoomResponse(12L, "yujin", "profileImage3")
//                )
//        );
//
//        when(chatRoomService.findMessagesByChatRoom(any(Long.class), any(Long.class))).thenReturn(chatRoomInformationResponse);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
//        mockMvc.perform(get("/api/chat-rooms/{chatRoomId}", chatRoomId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.status").value(StatusEnum.SUCCESS.name()))
//                .andExpect(jsonPath("$.message").value("Chat room information retrieved successfully"))
//                .andExpect(jsonPath("$.data.person_count").value(chatRoomInformationResponse.getPersonCount()))
//                .andExpect(jsonPath("$.data.explanation").value(chatRoomInformationResponse.getExplanation()))
//                .andExpect(jsonPath("$.data.message_list").isArray())
//                .andExpect(jsonPath("$.data.message_list[0].id").value(chatRoomInformationResponse.getMessageList().get(0).getId()))
//                .andExpect(jsonPath("$.data.message_list[0].content").value(chatRoomInformationResponse.getMessageList().get(0).getContent()))
//                .andExpect(jsonPath("$.data.message_list[0].message_type").value(chatRoomInformationResponse.getMessageList().get(0).getMessageType().toString()))
//                .andExpect(jsonPath("$.data.message_list[0].created_at").value(chatRoomInformationResponse.getMessageList().get(0).getCreatedAt().format(formatter)))
//                .andExpect(jsonPath("$.data.message_list[0].user_chat_room_response").isMap())
//                .andExpect(jsonPath("$.data.message_list[0].user_chat_room_response.userId").value(chatRoomInformationResponse.getMessageList().get(0).getUserChatRoomResponse().getUserId()))
//                .andExpect(jsonPath("$.data.message_list[0].user_chat_room_response.nickName").value(chatRoomInformationResponse.getMessageList().get(0).getUserChatRoomResponse().getNickName()))
//                .andExpect(jsonPath("$.data.message_list[0].user_chat_room_response.profileImg").value(chatRoomInformationResponse.getMessageList().get(0).getUserChatRoomResponse().getProfileImg()))
//                .andExpect(jsonPath("$.data.message_list[0].chat_room_id").value(chatRoomInformationResponse.getMessageList().get(0).getChatRoomId()))
//                .andExpect(jsonPath("$.data.message_list[1].id").value(chatRoomInformationResponse.getMessageList().get(1).getId()))
//                .andExpect(jsonPath("$.data.message_list[1].content").value(chatRoomInformationResponse.getMessageList().get(1).getContent()))
//                .andExpect(jsonPath("$.data.message_list[1].message_type").value(chatRoomInformationResponse.getMessageList().get(1).getMessageType().toString()))
//                .andExpect(jsonPath("$.data.message_list[1].created_at").value(chatRoomInformationResponse.getMessageList().get(1).getCreatedAt().format(formatter)))
//                .andExpect(jsonPath("$.data.message_list[1].user_chat_room_response").isMap())
//                .andExpect(jsonPath("$.data.message_list[1].user_chat_room_response.userId").value(chatRoomInformationResponse.getMessageList().get(1).getUserChatRoomResponse().getUserId()))
//                .andExpect(jsonPath("$.data.message_list[1].user_chat_room_response.nickName").value(chatRoomInformationResponse.getMessageList().get(1).getUserChatRoomResponse().getNickName()))
//                .andExpect(jsonPath("$.data.message_list[1].user_chat_room_response.profileImg").value(chatRoomInformationResponse.getMessageList().get(1).getUserChatRoomResponse().getProfileImg()))
//                .andExpect(jsonPath("$.data.message_list[1].chat_room_id").value(chatRoomInformationResponse.getMessageList().get(1).getChatRoomId()))
//                .andExpect(jsonPath("$.data.user_list").isArray())
//                .andExpect(jsonPath("$.data.user_list[0].userId").value(chatRoomInformationResponse.getUserList().get(0).getUserId()))
//                .andExpect(jsonPath("$.data.user_list[0].nickName").value(chatRoomInformationResponse.getUserList().get(0).getNickName()))
//                .andExpect(jsonPath("$.data.user_list[0].profileImg").value(chatRoomInformationResponse.getUserList().get(0).getProfileImg()))
//                .andExpect(jsonPath("$.data.user_list[1].userId").value(chatRoomInformationResponse.getUserList().get(1).getUserId()))
//                .andExpect(jsonPath("$.data.user_list[1].nickName").value(chatRoomInformationResponse.getUserList().get(1).getNickName()))
//                .andExpect(jsonPath("$.data.user_list[1].profileImg").value(chatRoomInformationResponse.getUserList().get(1).getProfileImg()))
//                .andExpect(jsonPath("$.data.user_list[2].userId").value(chatRoomInformationResponse.getUserList().get(2).getUserId()))
//                .andExpect(jsonPath("$.data.user_list[2].nickName").value(chatRoomInformationResponse.getUserList().get(2).getNickName()))
//                .andExpect(jsonPath("$.data.user_list[2].profileImg").value(chatRoomInformationResponse.getUserList().get(2).getProfileImg()))
//                .andDo(print())
//                .andDo(document("get-chatroom-information-success",
//                        pathParameters(
//                                parameterWithName("chatRoomId").description("조회할 채팅방의 고유 ID")
//                        ),
//                        responseFields(
//                                fieldWithPath("status").description("응답 상태 (SUCCESS 또는 FAILURE)"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("exception").description("에러 메시지"),
//                                fieldWithPath("data.person_count").type(JsonFieldType.NUMBER).description("채팅방에 현재 참여하고 있는 사람의 수"),
//                                fieldWithPath("data.explanation").type(JsonFieldType.STRING).description("채팅방의 설명"),
//                                fieldWithPath("data.message_list").type(JsonFieldType.ARRAY).description("채팅방에서 주고받은 메시지 목록"),
//                                fieldWithPath("data.message_list[].id").type(JsonFieldType.NUMBER).description("메시지의 고유 식별자"),
//                                fieldWithPath("data.message_list[].content").type(JsonFieldType.STRING).description("메시지의 내용"),
//                                fieldWithPath("data.message_list[].message_type").type(JsonFieldType.STRING).description("메시지의 유형"),
//                                fieldWithPath("data.message_list[].created_at").type(JsonFieldType.STRING).description("메시지가 생성된 시각"),
//                                fieldWithPath("data.message_list[].user_chat_room_response.userId").type(JsonFieldType.NUMBER).description("메시지를 보낸 사용자의 ID"),
//                                fieldWithPath("data.message_list[].user_chat_room_response.nickName").type(JsonFieldType.STRING).description("메시지를 보낸 사용자의 닉네임"),
//                                fieldWithPath("data.message_list[].user_chat_room_response.profileImg").type(JsonFieldType.STRING).description("메시지를 보낸 사용자의 프로필 이미지"),
//                                fieldWithPath("data.message_list[].chat_room_id").type(JsonFieldType.NUMBER).description("메시지가 속한 채팅 방의 ID"),
//                                fieldWithPath("data.user_list").type(JsonFieldType.ARRAY).description("채팅방에 참여하고 있는 사용자 목록"),
//                                fieldWithPath("data.user_list[].userId").type(JsonFieldType.NUMBER).description("사용자의 ID"),
//                                fieldWithPath("data.user_list[].nickName").type(JsonFieldType.STRING).description("사용자의 닉네임"),
//                                fieldWithPath("data.user_list[].profileImg").type(JsonFieldType.STRING).description("사용자의 프로필 이미지")
//                        )
//                ));
//    }
//
//    @DisplayName("deleteChatRoom - 성공")
//    @Test
//    void deleteChatRoomTest() throws Exception {
//
//        doNothing().when(chatRoomService).leaveChatRoom(any(Long.class), any(Long.class));
//
//        mockMvc.perform(delete("/api/chat-rooms/{chatRoomId}", 1L)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.status").value(StatusEnum.SUCCESS.name()))
//                .andExpect(jsonPath("$.message").value("Chat room deleted successfully"))
//                .andDo(document("delete-chatroom-success",
//                        pathParameters(
//                                parameterWithName("chatRoomId").description("삭제할 채팅방의 고유 ID")
//                        ),
//                        responseFields(
//                                fieldWithPath("status").description("응답 상태 (SUCCESS 또는 FAILURE)"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("exception").description("에러 메시지"),
//                                fieldWithPath("data").ignored()
//                        )
//                ));
//    }
//
//    @DisplayName("지정된 zoneCategoryName 에 해당하는 모든 채팅방을 정상적으로 반환하는지 테스트합니다.")
//    @Test
//    void getAllChatRoomsTest() throws Exception {
//        String zoneCategoryName = "역삼동";
//        String sort = "recent";
//        String search = "초보";
//
//        List<ChatRoomBriefResponse> chatRoomBriefResponseList = List.of(
//                new ChatRoomBriefResponse(1L, "초보 제목1", "image1", 3L, "채팅방 설명입니다"),
//                new ChatRoomBriefResponse(2L, "초보 제목2", "image2", 5L, "채팅방 설명입니다")
//        );
//
//        when(chatRoomService.findAllChatRoomBySearchAndSort(anyLong(), any(String.class), any(String.class), any(String.class)))
//                .thenReturn(chatRoomBriefResponseList);
//
//        mockMvc.perform(get("/api/chat-rooms/zone-category/{zoneCategoryName}", zoneCategoryName)
//                        .param("sort", sort)
//                        .param("search", search)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.status").value(StatusEnum.SUCCESS.name()))
//                .andExpect(jsonPath("$.message").value("All chat rooms retrieved successfully"))
//                .andExpect(jsonPath("$.data").isArray())
//                .andExpect(jsonPath("$.data.length()").value(chatRoomBriefResponseList.size()))
//                .andExpect(jsonPath("$.data[0].id").value(chatRoomBriefResponseList.get(0).getId()))
//                .andExpect(jsonPath("$.data[0].title").value(chatRoomBriefResponseList.get(0).getTitle()))
//                .andExpect(jsonPath("$.data[0].image").value(chatRoomBriefResponseList.get(0).getImage()))
//                .andExpect(jsonPath("$.data[0].person_count").value(chatRoomBriefResponseList.get(0).getPersonCount()))
//                .andExpect(jsonPath("$.data[0].explanation").value(chatRoomBriefResponseList.get(0).getExplanation()))
//                .andExpect(jsonPath("$.data[1].id").value(chatRoomBriefResponseList.get(1).getId()))
//                .andExpect(jsonPath("$.data[1].title").value(chatRoomBriefResponseList.get(1).getTitle()))
//                .andExpect(jsonPath("$.data[1].image").value(chatRoomBriefResponseList.get(1).getImage()))
//                .andExpect(jsonPath("$.data[1].person_count").value(chatRoomBriefResponseList.get(1).getPersonCount()))
//                .andExpect(jsonPath("$.data[1].explanation").value(chatRoomBriefResponseList.get(1).getExplanation()))
//                .andDo(document("get-all-chat-rooms-success",
//                        pathParameters(
//                                parameterWithName("zoneCategoryName").description("조회할 지역 카테고리 이름")
//                        ),
//                        queryParameters(
//                                parameterWithName("sort").description("정렬 기준 (예: recent, popular) default : recent").optional(),
//                                parameterWithName("search").description("검색어").optional()
//                        ),
//                        responseFields(
//                                fieldWithPath("status").description("응답 상태 (SUCCESS 또는 FAILURE)"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("exception").description("에러 메세지"),
//                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("채팅방 목록"),
//                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("채팅방의 고유 ID"),
//                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("채팅방의 이름"),
//                                fieldWithPath("data[].image").type(JsonFieldType.STRING).description("채팅방의 대표 이미지 URL"),
//                                fieldWithPath("data[].person_count").type(JsonFieldType.NUMBER).description("채팅방에 현재 참여하고 있는 사람의 수"),
//                                fieldWithPath("data[].explanation").type(JsonFieldType.STRING).description("채팅방의 간단한 설명")
//                        )
//                ));
//
//
//    }
//
//    @DisplayName("myChatRoomList - 성공")
//    @Test
//    void myChatRoomListTest() throws Exception {
//        List<UserChatRoomParticipationResponse> chatRoomBriefResponseList = List.of(
//                new UserChatRoomParticipationResponse(1L, "제목1", "이미지1", "설명", LocalDateTime.of(2024, 6, 27, 12, 0)),
//                new UserChatRoomParticipationResponse(2L, "제목2", "이미지2", "설명", LocalDateTime.of(2024, 8, 8, 12, 0))
//        );
//
//        when(chatRoomService.findAllByUser(any(Long.class))).thenReturn(chatRoomBriefResponseList);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
//        mockMvc.perform(get("/api/chat-rooms/me")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.status").value(StatusEnum.SUCCESS.name()))
//                .andExpect(jsonPath("$.message").value("Chat rooms by user retrieved successfully"))
//                .andExpect(jsonPath("$.data").isArray())
//                .andExpect(jsonPath("$.data.length()").value(chatRoomBriefResponseList.size()))
//                .andExpect(jsonPath("$.data[0].id").value(chatRoomBriefResponseList.get(0).getId()))
//                .andExpect(jsonPath("$.data[0].title").value(chatRoomBriefResponseList.get(0).getTitle()))
//                .andExpect(jsonPath("$.data[0].image").value(chatRoomBriefResponseList.get(0).getImage()))
//                .andExpect(jsonPath("$.data[0].explanation").value(chatRoomBriefResponseList.get(0).getExplanation()))
//                .andExpect(jsonPath("$.data[0].last_message_time").value(chatRoomBriefResponseList.get(0).getLastMessageTime().format(formatter)))
//                .andExpect(jsonPath("$.data[1].id").value(chatRoomBriefResponseList.get(1).getId()))
//                .andExpect(jsonPath("$.data[1].title").value(chatRoomBriefResponseList.get(1).getTitle()))
//                .andExpect(jsonPath("$.data[1].image").value(chatRoomBriefResponseList.get(1).getImage()))
//                .andExpect(jsonPath("$.data[1].explanation").value(chatRoomBriefResponseList.get(1).getExplanation()))
//                .andExpect(jsonPath("$.data[1].last_message_time").value(chatRoomBriefResponseList.get(1).getLastMessageTime().format(formatter)))
//                .andDo(document("get-my-chat-rooms-success",
//                        responseFields(
//                                fieldWithPath("status").description("응답 상태 (SUCCESS 또는 FAILURE)"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("exception").description("에러 메세지"),
//                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("사용자가 참여하고 있는 채팅방 목록"),
//                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("채팅방의 고유 ID"),
//                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("채팅방의 제목"),
//                                fieldWithPath("data[].image").type(JsonFieldType.STRING).description("채팅방의 대표 이미지 URL"),
//                                fieldWithPath("data[].explanation").type(JsonFieldType.STRING).description("채팅방의 설명"),
//                                fieldWithPath("data[].last_message_time").type(JsonFieldType.STRING).description("채팅방에서 마지막으로 전송된 메시지의 시각")
//                        )
//                ));
//    }
//
//    @DisplayName("mainChatRoomByUser - 성공")
//    @Test
//    void mainChatRoomByUserTest() throws Exception {
//        List<UserChatRoomBriefParticipationResponse> chatRoomBriefResponseList = List.of(
//                new UserChatRoomBriefParticipationResponse(1L, "제목1", "이미지1", "마지막 메세지1"),
//                new UserChatRoomBriefParticipationResponse(2L, "제목2", "이미지2", "마지막 메세지2")
//        );
//
//        when(chatRoomService.findAllBriefByUser(any(Long.class))).thenReturn(chatRoomBriefResponseList);
//
//        mockMvc.perform(get("/api/chat-rooms/me/main")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.status").value(StatusEnum.SUCCESS.name()))
//                .andExpect(jsonPath("$.message").value("chatting main page user participate chatting room list successfully"))
//                .andExpect(jsonPath("$.data").isArray())
//                .andExpect(jsonPath("$.data.length()").value(chatRoomBriefResponseList.size()))
//                .andExpect(jsonPath("$.data[0].id").value(chatRoomBriefResponseList.get(0).getId()))
//                .andExpect(jsonPath("$.data[0].title").value(chatRoomBriefResponseList.get(0).getTitle()))
//                .andExpect(jsonPath("$.data[0].image").value(chatRoomBriefResponseList.get(0).getImage()))
//                .andExpect(jsonPath("$.data[0].last_message").value(chatRoomBriefResponseList.get(0).getLastMessage()))
//                .andExpect(jsonPath("$.data[1].id").value(chatRoomBriefResponseList.get(1).getId()))
//                .andExpect(jsonPath("$.data[1].title").value(chatRoomBriefResponseList.get(1).getTitle()))
//                .andExpect(jsonPath("$.data[1].image").value(chatRoomBriefResponseList.get(1).getImage()))
//                .andExpect(jsonPath("$.data[1].last_message").value(chatRoomBriefResponseList.get(1).getLastMessage()))
//                .andDo(document("get-my-chat-rooms-brief-success",
//                        responseFields(
//                                fieldWithPath("status").description("응답 상태 (SUCCESS 또는 FAILURE)"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("exception").description("에러 메세지"),
//                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("사용자가 참여하고 있는 채팅방 목록"),
//                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("채팅방의 고유 ID"),
//                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("채팅방의 제목"),
//                                fieldWithPath("data[].image").type(JsonFieldType.STRING).description("채팅방의 대표 이미지 URL"),
//                                fieldWithPath("data[].last_message").type(JsonFieldType.STRING).description("채팅방에서 마지막으로 전송된 메시지")
//                        )
//                ));
//
//    }
//
//
//}