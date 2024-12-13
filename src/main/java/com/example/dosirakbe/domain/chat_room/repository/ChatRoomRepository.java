package com.example.dosirakbe.domain.chat_room.repository;

import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.zone_category.entity.ZoneCategory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * packageName    : com.example.dosirakbe.domain.chat_room.repository<br>
 * fileName       : ChatRoomRepository<br>
 * author         : Fiat_lux<br>
 * date           : 10/17/24<br>
 * description    : 채팅방 엔티티에 대한 데이터 접근을 담당하는 리포지토리 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/17/24        Fiat_lux                최초 생성<br>
 */
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    /**
     * 특정 지역 카테고리에 속하며, 지정된 사용자가 아직 참여하지 않은 모든 채팅방을 검색합니다.
     *
     * <p>
     * 이 메서드는 주어진 {@link ZoneCategory}에 속하고, 지정된 {@link User}가 참여하지 않은 채팅방을
     * 검색하며, 선택적인 검색어를 포함하는 채팅방 제목을 기준으로 필터링합니다.
     * 결과는 주어진 {@link Sort} 기준에 따라 정렬됩니다.
     * </p>
     *
     * @param zoneCategory 검색할 채팅방의 지역 카테고리
     * @param user         검색 대상 사용자가 아직 참여하지 않은 채팅방
     * @param search       채팅방 제목에 포함될 검색어 (선택 사항)
     * @param sort         결과 정렬 기준
     * @return 조건에 맞는 {@link ChatRoom} 엔티티의 리스트
     */
    @Query("SELECT cr FROM ChatRoom cr " +
            "LEFT JOIN UserChatRoom ucr ON cr.id = ucr.chatRoom.id AND ucr.user = :user " +
            "WHERE cr.zoneCategory = :zoneCategory AND ucr.id IS NULL " +
            "AND (:search IS NULL OR LOWER(cr.title) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<ChatRoom> findChatRoomsByZoneCategoryAndNotJoinedByUser(@Param("zoneCategory") ZoneCategory zoneCategory,
                                                                 @Param("user") User user,
                                                                 @Param("search") String search,
                                                                 Sort sort);

    /**
     * 모든 채팅방을 지정된 정렬 기준에 따라 조회합니다.
     *
     * <p>
     * 이 메서드는 데이터베이스에 존재하는 모든 {@link ChatRoom} 엔티티를
     * 주어진 {@link Sort} 기준에 따라 정렬하여 반환합니다.
     * </p>
     *
     * @param sort 결과 정렬 기준
     * @return 정렬된 {@link ChatRoom} 엔티티의 리스트
     */
    List<ChatRoom> findAll(Sort sort);
}
