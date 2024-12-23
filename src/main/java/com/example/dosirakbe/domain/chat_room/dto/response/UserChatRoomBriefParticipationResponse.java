package com.example.dosirakbe.domain.chat_room.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : com.example.dosirakbe.domain.chat_room.dto.response<br>
 * fileName       : UserChatRoomBriefParticipationResponse<br>
 * author         : Fiat_lux<br>
 * date           : 10/27/24<br>
 * description    : 사용자 참여 채팅방의 간략한 정보를 전달하기 위한 Response DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/27/24        Fiat_lux                최초 생성<br>
 * 12/23/24        Fiat_lux               필드에 json 어노테이션 추가<br>
 */
@Getter
@Setter
@AllArgsConstructor
public class UserChatRoomBriefParticipationResponse {

    /**
     * 채팅방의 고유 식별자입니다.
     *
     * <p>
     * 이 필드는 데이터베이스에서 자동 생성되는 고유한 ID를 나타냅니다.
     * </p>
     */
    private Long id;

    /**
     * 채팅방의 제목입니다.
     *
     * <p>
     * 이 필드는 사용자가 지정한 채팅방의 이름을 나타냅니다.
     * </p>
     */
    private String title;

    /**
     * 채팅방의 대표 이미지 URL 입니다.
     *
     * <p>
     * 채팅방의 시각적 표현을 위한 이미지 URL 을 포함합니다.
     * </p>
     */
    private String image;

    /**
     * 채팅방에서 마지막으로 전송된 메시지입니다.
     *
     * <p>
     * 이 필드는 채팅방에서 마지막 메시지를 나타냅니다.
     * </p>
     */
    @JsonProperty("last_message")
    private String lastMessage;
}
