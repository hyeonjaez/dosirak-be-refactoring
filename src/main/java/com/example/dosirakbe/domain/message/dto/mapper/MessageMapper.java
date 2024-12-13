package com.example.dosirakbe.domain.message.dto.mapper;

import com.example.dosirakbe.domain.message.dto.response.MessageResponse;
import com.example.dosirakbe.domain.message.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * packageName    : com.example.dosirakbe.domain.message.dto.mapper<br>
 * fileName       : MessageMapper<br>
 * author         : Fiat_lux<br>
 * date           : 10/17/24<br>
 * description    : 메시지 엔티티를 DTO 로 변환하는 매퍼 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/17/24        Fiat_lux                최초 생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MessageMapper {

    /**
     * {@link Message} 엔티티를 {@link MessageResponse} Response DTO 로 매핑합니다.
     *
     * <p>
     * 이 메서드는 {@link Message} 엔티티의 필드를 {@link MessageResponse} Response DTO 의 필드로 변환합니다.
     * 특히, {@link Message}의 {@code user}와 {@code chatRoom} 관련 필드를 {@code userChatRoomResponse}와 {@code chatRoomId}에 매핑합니다.
     * </p>
     *
     * @param message 매핑할 {@link Message} 엔티티 객체
     * @return 매핑된 {@link MessageResponse} Response DTO 객체
     */
    @Mapping(source = "user.userId", target = "userChatRoomResponse.userId")
    @Mapping(source = "user.nickName", target = "userChatRoomResponse.nickName")
    @Mapping(source = "user.profileImg", target = "userChatRoomResponse.profileImg")
    @Mapping(source = "chatRoom.id", target = "chatRoomId")
    MessageResponse mapToMessageResponse(Message message);

    /**
     * {@link Message} 엔티티 리스트를 {@link MessageResponse} Response DTO 리스트로 매핑합니다.
     *
     * <p>
     * 이 메서드는 {@link Message} 엔티티 리스트의 각 요소를 {@link MessageResponse} Response DTO 로 변환하여
     * {@link List}&lt;{@link MessageResponse}&gt; 형태로 반환합니다.
     * </p>
     *
     * @param messages 매핑할 {@link Message} 엔티티 리스트
     * @return 매핑된 {@link MessageResponse} Response DTO 리스트
     */
    List<MessageResponse> mapToMessageResponseList(List<Message> messages);
}
