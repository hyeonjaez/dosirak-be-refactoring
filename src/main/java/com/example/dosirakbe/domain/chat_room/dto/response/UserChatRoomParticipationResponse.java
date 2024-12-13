package com.example.dosirakbe.domain.chat_room.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * packageName    : com.example.dosirakbe.domain.chat_room.dto.response<br>
 * fileName       : UserChatRoomParticipationResponse<br>
 * author         : Fiat_lux<br>
 * date           : 10/27/24<br>
 * description    : 사용자가 참여하고 있는 채팅방의 상세 정보를 전달하기 위한 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/27/24        Fiat_lux                최초 생성<br>
 */
@Getter
@Setter
@AllArgsConstructor
public class UserChatRoomParticipationResponse {

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
     * 채팅방의 설명입니다.
     *
     * <p>
     * 이 필드는 채팅방에 대한 간단한 설명을 제공합니다.
     * </p>
     */
    private String explanation;

    /**
     * 채팅방에서 마지막으로 전송된 메시지의 시각입니다.
     *
     * <p>
     * 이 필드는 {@link LocalDateTime} 형식으로 마지막 메시지가 전송된 시간을 나타냅니다.
     * </p>
     *
     * @see LocalDateTime
     */
    private LocalDateTime lastMessageTime;
}
