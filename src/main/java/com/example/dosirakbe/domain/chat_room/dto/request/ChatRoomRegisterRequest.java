package com.example.dosirakbe.domain.chat_room.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomRegisterRequest {
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;

    @NotBlank
    @Size(min = 1)
    private String explanation;

    @NotBlank
    @Size(min = 1, max = 20)
    private String zoneCategoryName;
}
