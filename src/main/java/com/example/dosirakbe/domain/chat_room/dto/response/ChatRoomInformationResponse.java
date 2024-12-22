package com.example.dosirakbe.domain.chat_room.dto.response;

import com.example.dosirakbe.domain.message.dto.response.MessageResponse;
import com.example.dosirakbe.domain.user.dto.response.UserChatRoomResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * packageName    : com.example.dosirakbe.domain.chat_room.dto.response<br>
 * fileName       : ChatRoomInformationResponse<br>
 * author         : Fiat_lux<br>
 * date           : 10/19/24<br>
 * description    : 특정 채팅방의 상세 정보를 전달하기 위한 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/19/24        Fiat_lux                최초 생성<br>
 * 12/23/24        Fiat_lux               필드에 json 어노테이션 추가<br>
 */
@Getter
@AllArgsConstructor
public class ChatRoomInformationResponse {

    /**
     * 채팅방에 현재 참여하고 있는 사람의 수입니다.
     */
    @JsonProperty("person_count")
    private Long personCount;

    /**
     * 채팅방의 설명입니다.
     */
    private String explanation;

    /**
     * 채팅방에서 주고받은 메시지 목록입니다.
     *
     * <p>
     * 각 메시지는 {@link MessageResponse} Response DTO 로 표현됩니다.
     * </p>
     *
     * @see MessageResponse
     */
    @JsonProperty("message_list")
    private List<MessageResponse> messageList;

    /**
     * 채팅방에 참여하고 있는 사용자 목록입니다.
     *
     * <p>
     * 각 사용자는 {@link UserChatRoomResponse} Response DTO 로 표현됩니다.
     * </p>
     *
     * @see UserChatRoomResponse
     */
    @JsonProperty("user_list")
    private List<UserChatRoomResponse> userList;
}
