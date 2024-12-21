package com.example.dosirakbe.domain.user.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * packageName    : com.example.dosirakbe.domain.user.entity<br>
 * fileName       : User<br>
 * author         : yyujin1231<br>
 * date           : 10/20/24<br>
 * description    : 사용자 정보를 저장하는 엔티티 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/20/24        yyujin1231                최초 생성<br>
 */


@Table(name = "users")
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {


    /**
     * 사용자 ID
     * <p>
     * 데이터베이스에서 자동으로 생성되는 고유 ID입니다.
     * </p>
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, updatable = false)
    private Long userId;

    /**
     * 사용자의 이름.
     * <p>
     * 생성 후 변경할 수 없으며 필수 값입니다.
     * </p>
     */

    @Column(name = "name", updatable = false, nullable = false)
    private String name;

    /**
     * 사용자의 고유 사용자 이름.
     * <p>
     * 필수 값이며 수정 불가능합니다. (소셜로그인 제공자 + 소셜로그인 고유ID)
     * </p>
     */

    @Column(name = "user_name", updatable = false, nullable = false)
    private String userName;


    /**
     * 사용자의 닉네임.
     * <p>
     * 선택적으로 설정할 수 있으며 수정 가능합니다.
     * </p>
     */

    @Column(name = "nick_name")
    private String nickName;

    /**
     * 사용자의 이메일 주소.
     * <p>
     * 생성 후 변경할 수 없으며 필수 값입니다.
     * </p>
     */

    @Column(name = "email", updatable = false, nullable = false)
    private String email;

    /**
     * 사용자 계정 생성 시간.
     * <p>
     * 계정 생성 시 자동으로 설정되며 수정할 수 없습니다.
     * </p>
     */

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 사용자 계정 수정 시간.
     * <p>
     * 마지막으로 수정된 시간을 저장합니다.
     * </p>
     */

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 사용자의 프로필 이미지 URL.
     * <p>
     * 필수 값이며 수정 가능합니다.
     * </p>
     */

    @Column(name = "profile_img", nullable = false)
    private String profileImg;

    /**
     * 사용자 계정의 유효성 상태.
     * <p>
     * true이면 계정이 활성 상태, false이면 비활성 상태를 나타냅니다.
     * </p>
     */

    @Column(name = "user_valid")
    private boolean userValid;

    /**
     * 사용자가 보유한 리워드 포인트.
     * <p>
     * 기본 값은 0이며 수정 가능합니다.
     * </p>
     */

    @Column(name = "reward")
    private Integer reward;

    /**
     * 사용자가 이동한 총 거리.
     * <p>
     * 기본 값은 0이며 BigDecimal 형식으로 저장됩니다.
     * </p>
     */

    @Column(name = "track_distance")
    private BigDecimal trackDistance;

    /**
     * 엔티티가 저장되기 전에 기본 값을 설정합니다.
     * <p>
     * 이동 거리(trackDistance)와 리워드 포인트(reward)가 null일 경우 각각 0으로 초기화됩니다.
     * </p>
     */

    @PrePersist
    public void prePersist() {
        if (Objects.isNull(this.trackDistance)) {
            this.trackDistance = BigDecimal.ZERO;
        }

        if (Objects.isNull(this.reward)) {
            this.reward = 0;
        }
    }


    /**
     * 사용자의 이동 거리를 추가합니다.
     * <p>
     * 입력된 거리가 null이 아닌 경우 현재 거리에 더합니다.
     * </p>
     *
     * @param moveTrackDistance 추가할 이동 거리
     */

    public void addTrackDistance(BigDecimal moveTrackDistance) {
        if (Objects.nonNull(moveTrackDistance)) {
            this.trackDistance = this.trackDistance.add(moveTrackDistance);
        }
    }
}