package com.example.dosirakbe.domain.message.controller;

import com.example.dosirakbe.domain.message.dto.request.MessageRegisterRequest;
import com.example.dosirakbe.domain.message.dto.response.MessageResponse;
import com.example.dosirakbe.domain.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final MessageService messageService;


    @MessageMapping("/chat-room/{chatRoomId}/sendMessage")
    @SendTo("/topic/chat-room/{chatRoomId}")
    public MessageResponse sendMessage(@DestinationVariable Long chatRoomId, MessageRegisterRequest messageRegisterRequest) {
        MessageResponse message = messageService.createMessage(chatRoomId, messageRegisterRequest);

        return message;
    }
}
