package com.example.dosirakbe.domain.chat_room.repository;

import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.zone_category.entity.ZoneCategory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllByZoneCategory(ZoneCategory zoneCategory);

    @Query("SELECT cr FROM ChatRoom cr " +
            "LEFT JOIN UserChatRoom ucr ON cr.id = ucr.chatRoom.id AND ucr.user = :user " +
            "WHERE cr.zoneCategory = :zoneCategory AND ucr.id IS NULL " +
            "AND (:search IS NULL OR LOWER(cr.title) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<ChatRoom> findChatRoomsByZoneCategoryAndNotJoinedByUser(@Param("zoneCategory") ZoneCategory zoneCategory,
                                                                 @Param("user") User user,
                                                                 @Param("search") String search,
                                                                 Sort sort);

    List<ChatRoom> findAll(Sort sort);
}
