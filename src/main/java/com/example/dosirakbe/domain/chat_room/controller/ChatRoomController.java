package com.example.dosirakbe.domain.chat_room.controller;

import com.example.dosirakbe.domain.chat_room.dto.request.ChatRoomRegisterRequest;
import com.example.dosirakbe.domain.chat_room.dto.request.ChatRoomSortFilterRequest;
import com.example.dosirakbe.domain.chat_room.dto.response.ChatRoomBriefResponse;
import com.example.dosirakbe.domain.chat_room.dto.response.ChatRoomByUserResponse;
import com.example.dosirakbe.domain.chat_room.dto.response.ChatRoomInformationResponse;
import com.example.dosirakbe.domain.chat_room.dto.response.ChatRoomResponse;
import com.example.dosirakbe.domain.chat_room.service.ChatRoomService;
import com.example.dosirakbe.domain.user.dto.response.UserChatRoomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat-rooms")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<ChatRoomResponse> createChatRoom(@Valid @RequestBody ChatRoomRegisterRequest createRequest,
                                                           BindingResult bindingResult) {

        Long userId = 1L; //TODO userId 인증 인가 끝나면 가져와야함
        ChatRoomResponse chatRoomResponse = chatRoomService.createChatRoom(createRequest, userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(chatRoomResponse);
    }

    @GetMapping("/{id}/information")
    public ResponseEntity<ChatRoomInformationResponse> getChatRoomInformation(@PathVariable Long id) {
        Long userId = 1L; //TODO
        ChatRoomInformationResponse chatRoomInformation = chatRoomService.findMessagesByChatRoom(userId, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(chatRoomInformation);
    }


    @GetMapping("/{id}/user-list")
    public ResponseEntity<List<UserChatRoomResponse>> getChatRoomUsers(@PathVariable Long id) {
        List<UserChatRoomResponse> userList = chatRoomService.findUserChatRooms(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable Long id) {
        Long userId = 1L; //TODO

        chatRoomService.leaveChatRoom(userId, id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/zone-category/{zoneCategoryName}")
    public ResponseEntity<List<ChatRoomBriefResponse>> chatRoomByZoneCategory(@PathVariable String zoneCategoryName) {
        Long userId = 1L;
        List<ChatRoomBriefResponse> chatRoomByZoneCategory = chatRoomService.findChatRoomByZoneCategory(userId, zoneCategoryName);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(chatRoomByZoneCategory);
    }


    @GetMapping
    public ResponseEntity<List<ChatRoomBriefResponse>> getAllChatRooms(@Valid @ModelAttribute ChatRoomSortFilterRequest chatRoomSortFilterRequest,
                                                                       BindingResult bindingResult) {
        return null;
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<ChatRoomByUserResponse>> myChatRoomList() {
        Long userId = 1L; //TODO
        List<ChatRoomByUserResponse> allByUser = chatRoomService.findAllByUser(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allByUser);
    }


}
