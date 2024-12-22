package com.example.dosirakbe.domain.message.dto.response;

import com.example.dosirakbe.domain.message.entity.MessageType;
import com.example.dosirakbe.domain.user.dto.response.UserChatRoomResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * packageName    : com.example.dosirakbe.domain.message.dto.response<br>
 * fileName       : MessageResponse<br>
 * author         : Fiat_lux<br>
 * date           : 10/17/24<br>
 * description    : 메시지 정보를 클라이언트에 전달하기 위한 응답 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/17/24        Fiat_lux                최초 생성<br>
 * 12/23/24        Fiat_lux               필드에 json 어노테이션 추가<br>
 */
@Getter
@AllArgsConstructor
public class MessageResponse {

    /**
     * 메시지의 고유 식별자입니다.
     *
     * <p>
     * 이 필드는 메시지의 고유 ID를 나타내며, 데이터베이스에서 자동 생성됩니다.
     * </p>
     */
    private Long id;

    /**
     * 메시지의 내용입니다.
     *
     * <p>
     * 이 필드는 사용자가 전송한 실제 메시지 내용을 담고 있습니다.
     * </p>
     */
    private String content;

    /**
     * 메시지의 유형을 나타냅니다.
     *
     * <p>
     * 이 필드는 {@link MessageType} 열거형을 사용하여 메시지의 종류를 정의합니다.
     * 예를 들어, 텍스트 메시지, 이미지 메시지 등이 될 수 있습니다.
     * </p>
     */
    @JsonProperty("message_type")
    private MessageType messageType;

    /**
     * 메시지가 생성된 시각입니다.
     *
     * <p>
     * 이 필드는 메시지가 전송된 정확한 시각을 {@link LocalDateTime} 형식으로 나타냅니다.
     * </p>
     */
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    /**
     * 메시지를 보낸 사용자의 정보입니다.
     *
     * <p>
     * 이 필드는 {@link UserChatRoomResponse} Response DTO 를 사용하여 메시지를 보낸 사용자의 ID, 닉네임, 프로필 이미지를 포함합니다.
     * </p>
     */
    @JsonProperty("user_chat_room_response")
    private UserChatRoomResponse userChatRoomResponse;

    /**
     * 메시지가 속한 채팅 방의 고유 식별자입니다.
     *
     * <p>
     * 이 필드는 메시지가 전송된 특정 채팅 방의 ID를 나타냅니다.
     * </p>
     */
    @JsonProperty("chat_room_id")
    private Long chatRoomId;
}
