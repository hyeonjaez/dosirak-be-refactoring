package com.example.dosirakbe.domain.chat_room.dto.mapper;

import com.example.dosirakbe.domain.chat_room.dto.response.*;
import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * packageName    : com.example.dosirakbe.domain.chat_room.dto.mapper <br>
 * fileName       : ChatRoomMapper<br>
 * author         : Fiat_lux<br>
 * date           : 10/17/24<br>
 * description    : 채팅방 엔티티와 다양한 DTO 간의 매핑을 담당하는 매퍼 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/17/24        Fiat_lux                최초 생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ChatRoomMapper {
    /**
     * {@link ChatRoom} 엔티티를 {@link ChatRoomResponse} Response DTO 로 매핑합니다.
     *
     * <p>
     * 이 메서드는 {@link ChatRoom} 엔티티의 모든 필드를 {@link ChatRoomResponse} Response DTO 로 변환합니다.
     * </p>
     *
     * @param chatRoom 매핑할 {@link ChatRoom} 엔티티
     * @return 매핑된 {@link ChatRoomResponse} Response DTO
     */
    ChatRoomResponse mapToChatRoomResponse(ChatRoom chatRoom);

    /**
     * {@link ChatRoom} 엔티티를 {@link ChatRoomByUserResponse} Response DTO 로 매핑합니다.
     *
     * <p>
     * 이 메서드는 {@link ChatRoom} 엔티티의 일부 필드를 {@link ChatRoomByUserResponse} Response DTO 로 변환하며,
     * {@code lastMessage} 필드는 무시됩니다.
     * </p>
     *
     * @param chatRoom 매핑할 {@link ChatRoom} 엔티티
     * @return 매핑된 {@link ChatRoomByUserResponse} Response DTO
     */
    @Mapping(target = "lastMessage", ignore = true)
    ChatRoomByUserResponse mapToChatRoomByUserResponse(ChatRoom chatRoom);

    /**
     * {@link ChatRoom} 엔티티를 {@link ChatRoomBriefResponse} Response DTO 로 매핑합니다.
     *
     * <p>
     * 이 메서드는 {@link ChatRoom} 엔티티의 일부 필드를 {@link ChatRoomBriefResponse} Response DTO 로 변환합니다.
     * </p>
     *
     * @param chatRoom 매핑할 {@link ChatRoom} 엔티티
     * @return 매핑된 {@link ChatRoomBriefResponse} Response DTO
     */
    ChatRoomBriefResponse mapToChatRoomBriefResponse(ChatRoom chatRoom);

    /**
     * {@link ChatRoom} 엔티티 리스트를 {@link ChatRoomBriefResponse} Response DTO 리스트로 매핑합니다.
     *
     * <p>
     * 이 메서드는 {@link ChatRoom} 엔티티의 리스트를 {@link ChatRoomBriefResponse} Response DTO 의 리스트로 변환합니다.
     * </p>
     *
     * @param chatRooms 매핑할 {@link ChatRoom} 엔티티 리스트
     * @return 매핑된 {@link ChatRoomBriefResponse} Response DTO 리스트
     */
    List<ChatRoomBriefResponse> mapToChatRoomBriefResponseList(List<ChatRoom> chatRooms);

    /**
     * {@link ChatRoom} 엔티티를 {@link UserChatRoomParticipationResponse} Response DTO 로 매핑합니다.
     *
     * <p>
     * 이 메서드는 {@link ChatRoom} 엔티티의 일부 필드를 {@link UserChatRoomParticipationResponse} Response DTO 로 변환하며,
     * {@code lastMessageTime} 필드는 무시됩니다.
     * </p>
     *
     * @param chatRoom 매핑할 {@link ChatRoom} 엔티티
     * @return 매핑된 {@link UserChatRoomParticipationResponse} Response DTO
     */
    @Mapping(target = "lastMessageTime", ignore = true)
    UserChatRoomParticipationResponse mapToUserChatRoomParticipationResponse(ChatRoom chatRoom);

    /**
     * {@link ChatRoom} 엔티티를 {@link UserChatRoomBriefParticipationResponse} Response DTO 로 매핑합니다.
     *
     * <p>
     * 이 메서드는 {@link ChatRoom} 엔티티의 일부 필드를 {@link UserChatRoomBriefParticipationResponse} Response DTO 로 변환하며,
     * {@code lastMessage} 필드는 무시됩니다.
     * </p>
     *
     * @param chatRoom 매핑할 {@link ChatRoom} 엔티티
     * @return 매핑된 {@link UserChatRoomBriefParticipationResponse} Response DTO
     */
    @Mapping(target = "lastMessage", ignore = true)
    UserChatRoomBriefParticipationResponse mapToUserChatRoomBriefParticipationResponse(ChatRoom chatRoom);
}
