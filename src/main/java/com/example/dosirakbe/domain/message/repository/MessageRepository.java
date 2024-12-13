package com.example.dosirakbe.domain.message.repository;

import com.example.dosirakbe.domain.message.entity.Message;
import com.example.dosirakbe.domain.message.entity.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.example.dosirakbe.domain.message.repository<br>
 * fileName       : MessageRepository<br>
 * author         : Fiat_lux<br>
 * date           : 10/17/24<br>
 * description    : 메시지 엔티티에 대한 데이터 접근을 담당하는 리포지토리 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/17/24        Fiat_lux                최초 생성<br>
 */
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * 특정 채팅 방에서 지정된 시각 이후에 생성된 모든 메시지를 생성 시각 오름차순으로 조회합니다.
     *
     * <p>
     * 이 메서드는 주어진 {@code chatRoomId}에 속하며, {@code createdAt} 시각 이후에 생성된 모든 {@link Message} 엔티티를
     * 생성 시각 오름차순으로 정렬하여 반환합니다.
     * </p>
     *
     * @param chatRoomId 채팅 방의 고유 식별자
     * @param createdAt  조회할 메시지의 생성 시각 기준
     * @return 주어진 조건에 맞는 {@link Message} 엔티티 리스트
     */
    List<Message> findByChatRoomIdAndCreatedAtAfterOrderByCreatedAtAsc(Long chatRoomId, LocalDateTime createdAt);

    /**
     * 특정 채팅 방에서 가장 최근에 생성된 특정 유형의 메시지를 조회합니다.
     *
     * <p>
     * 이 메서드는 주어진 {@code chatRoomId}에 속하며, {@code messageType}과 일치하는
     * 가장 최근에 생성된 {@link Message} 엔티티를 반환합니다.
     * 해당 조건에 맞는 메시지가 없을 경우 {@link Optional#empty()}를 반환합니다.
     * </p>
     *
     * @param chatRoomId  채팅 방의 고유 식별자
     * @param messageType 조회할 메시지의 유형 {@link MessageType}
     * @return 조건에 맞는 가장 최근의 {@link Message} 엔티티를 포함하는 {@link Optional}, 없을 경우 {@link Optional#empty()}
     */
    Optional<Message> findFirstByChatRoomIdAndMessageTypeOrderByCreatedAtDesc(Long chatRoomId, MessageType messageType);
}
