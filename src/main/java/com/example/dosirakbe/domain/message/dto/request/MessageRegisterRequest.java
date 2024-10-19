package com.example.dosirakbe.domain.message.dto.request;

import com.example.dosirakbe.domain.message.entity.MessageType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageRegisterRequest {

    @NotBlank
    private String content;

    @NotNull
    private MessageType messageType;

    @NotNull
    @Min(1)
    private Long userId;

    @NotNull
    @Min(1)
    private Long chatRoomId;
}
