package com.example.dosirakbe.domain.chat_room.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : com.example.dosirakbe.domain.chat_room.dto.response<br>
 * fileName       : ChatRoomByUserResponse<br>
 * author         : Fiat_lux<br>
 * date           : 10/23/24<br>
 * description    : 사용자 관련 채팅방 정보를 전달하기 위한 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/23/24        Fiat_lux                최초 생성<br>
 */
@Getter
@Setter
@AllArgsConstructor
public class ChatRoomByUserResponse {

    /**
     * 채팅방의 고유 식별자입니다.
     */
    private Long id;

    /**
     * 채팅방의 제목입니다.
     */
    private String title;

    /**
     * 채팅방의 대표 이미지 URL입니다.
     *
     * <p>
     * 채팅방의 시각적 표현을 위한 이미지 URL을 포함합니다.
     * </p>
     */
    private String image;

    /**
     * 채팅방에 현재 참여하고 있는 사람의 수입니다.
     */
    private Long personCount;

    /**
     * 채팅방에서 마지막으로 전송된 메시지입니다.
     */
    private String lastMessage;
}
