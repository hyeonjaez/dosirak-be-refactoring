package com.example.dosirakbe.domain.chat_room.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : com.example.dosirakbe.domain.chat_room.dto.response<br>
 * fileName       : ChatRoomResponse<br>
 * author         : Fiat_lux<br>
 * date           : 10/17/24<br>
 * description    : 채팅방 생성 및 조회 시 응답으로 사용되는 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/17/24        Fiat_lux                최초 생성<br>
 * 12/23/24        Fiat_lux               필드에 json 어노테이션 추가<br>
 */
@Getter
@AllArgsConstructor
public class ChatRoomResponse {

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
     * 채팅방에 현재 참여하고 있는 사람의 수입니다.
     *
     * <p>
     * 이 필드는 채팅방에 참여 중인 사용자들의 수를 나타냅니다.
     * </p>
     */
    @JsonProperty("person_count")
    private Long personCount;
}
