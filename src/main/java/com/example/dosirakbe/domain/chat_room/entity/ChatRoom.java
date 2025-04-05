package com.example.dosirakbe.domain.chat_room.entity;

import com.example.dosirakbe.domain.zone_category.entity.ZoneCategory;
import com.example.dosirakbe.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

/**
 * packageName    : com.example.dosirakbe.domain.chat_room.entity<br>
 * fileName       : ChatRoom<br>
 * author         : Fiat_lux<br>
 * date           : 10/17/24<br>
 * description    : 채팅방 엔티티 클래스입니다. 채팅방의 기본 정보와 관련된 데이터를 관리합니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/17/24        Fiat_lux             최초 생성<br>
 */
@Entity
@Getter
@Setter
@Table(name = "chat_rooms")
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom extends BaseEntity {

    /**
     * 채팅방의 고유 식별자입니다.
     *
     * <p>
     * 이 필드는 데이터베이스에서 자동 생성되는 고유한 ID를 나타냅니다.
     * </p>
     */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 채팅방의 제목입니다.
     *
     * <p>
     * 이 필드는 사용자가 지정한 채팅방의 이름을 나타냅니다.
     * </p>
     */
    @Column
    private String title;

    /**
     * 채팅방에 현재 참여하고 있는 사람의 수입니다.
     *
     * <p>
     * 이 필드는 채팅방에 참여 중인 사용자들의 수를 나타냅니다.
     * </p>
     */
    @Column(name = "person_count")
    private Long personCount;

    /**
     * 채팅방의 설명입니다.
     *
     * <p>
     * 이 필드는 채팅방에 대한 간단한 설명을 제공합니다.
     * </p>
     */
    @Column(columnDefinition = "TEXT")
    private String explanation;

    /**
     * 채팅방이 속한 지역 카테고리입니다.
     *
     * <p>
     * 이 필드는 {@link ZoneCategory} 엔티티와의 다대일 관계를 나타내며,
     * 채팅방이 속한 지역 카테고리를 참조합니다.
     * </p>
     */
    @JoinColumn(name = "zone_category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ZoneCategory zoneCategory;

    /**
     * 채팅방의 대표 이미지 URL 입니다.
     *
     * <p>
     * 이 필드는 채팅방의 시각적 표현을 위한 이미지 URL 을 포함합니다.
     * </p>
     */
    @Column
    private String image;

    /**
     * 새로운 채팅방을 생성합니다.
     *
     * <p>
     * 이 생성자는 채팅방의 제목, 설명, 지역 카테고리, 이미지 URL 을 초기화합니다.
     * </p>
     *
     * @param title        채팅방의 제목
     * @param explanation  채팅방의 설명
     * @param zoneCategory 채팅방이 속한 지역 카테고리
     * @param image        채팅방의 대표 이미지 URL
     */
    public ChatRoom(String title, String explanation, ZoneCategory zoneCategory, String image) {
        this.title = title;
        this.explanation = explanation;
        this.zoneCategory = zoneCategory;
        this.image = image;
    }

    /**
     * 엔티티가 처음 영속화될 때 실행되는 메서드입니다.
     *
     * <p>
     * 이 메서드는 {@link #personCount} 필드가 {@code null}일 경우 기본값을 {@code 1L}로 설정합니다.
     * </p>
     */
    @PrePersist
    public void prePersist() {
        if (Objects.isNull(this.personCount)) {
            this.personCount = 1L;
        }
    }

    /**
     * 채팅방의 인원 수를 증가시킵니다.
     *
     * <p>
     * 이 메서드는 채팅방에 사용자가 추가될 때 호출되어 {@link #personCount}를 1 증가시킵니다.
     * </p>
     */
    public void upPersonCount() {
        this.personCount++;
    }

    /**
     * 채팅방의 인원 수를 감소시킵니다.
     *
     * <p>
     * 이 메서드는 채팅방에서 사용자가 떠날 때 호출되어 {@link #personCount}를 1 감소시킵니다.
     * </p>
     */
    public void downPersonCount() {
        this.personCount--;
    }
}