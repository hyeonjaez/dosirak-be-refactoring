//package com.example.dosirakbe.domain.chat_room.controller;
//
//import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
//import com.example.dosirakbe.domain.chat_room.dto.request.ChatRoomRegisterRequest;
//import com.example.dosirakbe.domain.chat_room.dto.response.*;
//import com.example.dosirakbe.domain.chat_room.service.ChatRoomService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
///**
// * packageName    : com.example.dosirakbe.domain.chat_room.controller<br>
// * fileName       : ChatRoomController<br>
// * author         : Fiat_lux<br>
// * date           : 10/19/24<br>
// * description    : 채팅방 관련 crud controller 클래스 입니다.<br>
// * ===========================================================<br>
// * DATE              AUTHOR             NOTE<br>
// * -----------------------------------------------------------<br>
// * 10/19/24        Fiat_lux                최초 생성<br>
// */
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/chat-rooms")
//public class ChatRoomController {
//    private final ChatRoomService chatRoomService;
//
//
//    private static final String REQUEST_PART_CHATROOM = "chatRoom";
//    private static final String REQUEST_PART_FILE = "file";
//
//    /**
//     * 새로운 채팅방을 생성합니다.
//     *
//     * <p>
//     * 이 메서드는 인증된 사용자의 ID를 기반으로 새로운 채팅방을 생성하며,
//     * 선택적으로 파일을 업로드할 수 있습니다.
//     * {@link ApiResult} 형태로 생성된 채팅방 정보를 반환합니다.
//     * </p>
//     *
//     * @param customOAuth2User 인증된 사용자 정보
//     * @param createRequest    채팅방 생성 요청 데이터
//     * @param file             업로드된 파일 (선택 사항)
//     * @param bindingResult    유효성 검사 결과
//     * @return {@link ResponseEntity}에 {@link ApiResult} 형태로 래핑된 생성된 채팅방 정보를 반환합니다.
//     *         성공 시 HTTP 상태 코드 201(CREATED)을 반환합니다.
//     */
//    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ResponseEntity<ApiResult<ChatRoomResponse>> createChatRoom(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
//                                                                      @Valid @RequestPart(REQUEST_PART_CHATROOM) ChatRoomRegisterRequest createRequest,
//                                                                      @RequestPart(value = REQUEST_PART_FILE, required = false) MultipartFile file,
//                                                                      BindingResult bindingResult) {
//        ValidationUtils.validationRequest(bindingResult);
//
//
//        Long userId = getUserIdByOAuth(customOAuth2User);
//        ChatRoomResponse chatRoomResponse = chatRoomService.createChatRoom(file, createRequest, userId);
//
//        ApiResult<ChatRoomResponse> result = ApiResult.<ChatRoomResponse>builder()
//                .status(StatusEnum.SUCCESS)
//                .message("Chat room created successfully")
//                .data(chatRoomResponse)
//                .build();
//
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(result);
//    }
//
//    /**
//     * 특정 채팅방의 상세 정보를 조회합니다.
//     *
//     * <p>
//     * 이 메서드는 인증된 사용자의 ID를 기반으로 특정 채팅방의 상세 정보를 조회하며,
//     * {@link ApiResult} 형태로 반환합니다.
//     * </p>
//     *
//     * @param customOAuth2User 인증된 사용자 정보
//     * @param chatRoomId       조회할 채팅방의 ID
//     * @return {@link ResponseEntity}에 {@link ApiResult} 형태로 래핑된 채팅방의 상세 정보를 반환합니다.
//     *         성공 시 HTTP 상태 코드 200(OK)을 반환합니다.
//     */
//    @GetMapping("/{chatRoomId}")
//    public ResponseEntity<ApiResult<ChatRoomInformationResponse>> getChatRoomInformation(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
//                                                                                         @PathVariable Long chatRoomId) {
//        Long userId = getUserIdByOAuth(customOAuth2User);
//        ChatRoomInformationResponse chatRoomInformation = chatRoomService.findMessagesByChatRoom(userId, chatRoomId);
//
//        ApiResult<ChatRoomInformationResponse> result = ApiResult.<ChatRoomInformationResponse>builder()
//                .status(StatusEnum.SUCCESS)
//                .message("Chat room information retrieved successfully")
//                .data(chatRoomInformation)
//                .build();
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(result);
//    }
//
//    /**
//     * 특정 채팅방을 삭제하거나, 사용자가 해당 채팅방을 떠납니다.
//     *
//     * <p>
//     * 이 메서드는 인증된 사용자의 ID를 기반으로 지정된 채팅방에서 사용자를 제거하며,
//     * {@link ApiResult} 형태로 응답을 반환합니다.
//     * </p>
//     *
//     * @param customOAuth2User 인증된 사용자 정보
//     * @param chatRoomId       삭제 또는 떠날 채팅방의 ID
//     * @return {@link ResponseEntity}에 {@link ApiResult} 형태로 응답 메시지를 반환합니다.
//     *         성공 시 HTTP 상태 코드 204(NO CONTENT)를 반환합니다.
//     */
//    @DeleteMapping("/{chatRoomId}")
//    public ResponseEntity<ApiResult<Void>> deleteChatRoom(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
//                                                          @PathVariable Long chatRoomId) {
//        Long userId = getUserIdByOAuth(customOAuth2User);
//        chatRoomService.leaveChatRoom(userId, chatRoomId);
//
//        ApiResult<Void> result = ApiResult.<Void>builder()
//                .status(StatusEnum.SUCCESS)
//                .message("Chat room deleted successfully")
//                .build();
//
//        return ResponseEntity
//                .status(HttpStatus.NO_CONTENT)
//                .body(result);
//    }
//
//    /**
//     * 특정 지역 카테고리에 속한 모든 채팅방을 조회합니다.
//     *
//     * <p>
//     * 이 메서드는 인증된 사용자의 ID를 기반으로 지정된 지역 카테고리에 속한 채팅방 목록을 조회하며,
//     * {@link ApiResult} 형태로 반환합니다.
//     * </p>
//     *
//     * @param customOAuth2User  인증된 OAuth2 사용자 정보
//     * @param zoneCategoryName 조회할 지역 카테고리 이름
//     * @param sort              정렬 방식 (기본값: recent)
//     * @param search            검색어 (선택 사항)
//     * @return {@link ResponseEntity}에 {@link ApiResult} 형태로 래핑된 채팅방 목록을 반환합니다.
//     *         성공 시 HTTP 상태 코드 200(OK)을 반환합니다.
//     */
//    @GetMapping("/zone-category/{zoneCategoryName}")
//    public ResponseEntity<ApiResult<List<ChatRoomBriefResponse>>> getAllChatRooms(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
//                                                                                  @PathVariable String zoneCategoryName,
//                                                                                  @RequestParam(required = false, defaultValue = "recent") String sort,
//                                                                                  @RequestParam(required = false) String search) {
//        Long userId = getUserIdByOAuth(customOAuth2User);
//        List<ChatRoomBriefResponse> allChatRoomBySearchAndSort = chatRoomService.findAllChatRoomBySearchAndSort(userId, zoneCategoryName, sort, search);
//
//        ApiResult<List<ChatRoomBriefResponse>> result = ApiResult.<List<ChatRoomBriefResponse>>builder()
//                .status(StatusEnum.SUCCESS)
//                .message("All chat rooms retrieved successfully")
//                .data(allChatRoomBySearchAndSort)
//                .build();
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(result);
//    }
//
//    /**
//     * 현재 사용자가 참여하고 있는 모든 채팅방 목록을 조회합니다.
//     *
//     * <p>
//     * 이 메서드는 인증된 사용자의 ID를 기반으로 현재 사용자가 참여하고 있는 모든 채팅방 목록을 조회하며,
//     * {@link ApiResult} 형태로 반환합니다.
//     * </p>
//     *
//     * @param customOAuth2User 인증된 사용자 정보
//     * @return {@link ResponseEntity}에 {@link ApiResult} 형태로 래핑된 사용자 참여 채팅방 목록을 반환합니다.
//     *         성공 시 HTTP 상태 코드 200(OK)을 반환합니다.
//     */
//    @GetMapping("/me")
//    public ResponseEntity<ApiResult<List<UserChatRoomParticipationResponse>>> myChatRoomList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
//        Long userId = getUserIdByOAuth(customOAuth2User);
//        List<UserChatRoomParticipationResponse> allByUser = chatRoomService.findAllByUser(userId);
//
//        ApiResult<List<UserChatRoomParticipationResponse>> result = ApiResult.<List<UserChatRoomParticipationResponse>>builder()
//                .status(StatusEnum.SUCCESS)
//                .message("Chat rooms by user retrieved successfully")
//                .data(allByUser)
//                .build();
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(result);
//    }
//
//    /**
//     * 현재 사용자가 메인으로 참여하고 있는 채팅방 목록을 조회합니다.
//     *
//     * <p>
//     * 이 메서드는 인증된 사용자의 ID를 기반으로 현재 사용자가 메인으로 참여하고 있는 채팅방 목록을 조회하며,
//     * {@link ApiResult} 형태로 반환합니다.
//     * </p>
//     *
//     * @param customOAuth2User 인증된 OAuth2 사용자 정보
//     * @return {@link ResponseEntity}에 {@link ApiResult} 형태로 래핑된 메인 채팅방 목록을 반환합니다.
//     *         성공 시 HTTP 상태 코드 200(OK)을 반환합니다.
//     */
//    @GetMapping("/me/main")
//    public ResponseEntity<ApiResult<List<UserChatRoomBriefParticipationResponse>>> mainChatRoomByUser(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
//        Long userId = getUserIdByOAuth(customOAuth2User);
//        List<UserChatRoomBriefParticipationResponse> allBriefByUser = chatRoomService.findAllBriefByUser(userId);
//
//        ApiResult<List<UserChatRoomBriefParticipationResponse>> result = ApiResult.<List<UserChatRoomBriefParticipationResponse>>builder()
//                .status(StatusEnum.SUCCESS)
//                .message("chatting main page user participate chatting room list successfully")
//                .data(allBriefByUser)
//                .build();
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(result);
//    }
//
//    /**
//     * 인증된 사용자로부터 사용자 ID를 추출합니다.
//     *
//     * @param customOAuth2User 인증된 사용자 정보
//     * @return 사용자 ID
//     */
//    private Long getUserIdByOAuth(CustomOAuth2User customOAuth2User) {
//        return customOAuth2User.getUserDTO().getUserId();
//    }
//}