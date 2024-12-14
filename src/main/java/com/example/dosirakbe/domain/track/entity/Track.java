package com.example.dosirakbe.domain.track.entity;

import com.example.dosirakbe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * packageName    : com.example.dosirakbe.domain.track.entity<br>
 * fileName       : Track<br>
 * author         : Fiat_lux<br>
 * date           : 11/24/24<br>
 * description    : 사용자의 이동 기록을 저장하는 엔티티 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/24/24        Fiat_lux                최초 생성<br>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "track_record")
@EntityListeners(AuditingEntityListener.class)
public class Track {

    /**
     * 트랙의 고유 식별자입니다.
     *
     * <p>
     * 이 필드는 트랙을 유일하게 식별하기 위한 ID를 나타냅니다.
     * </p>
     */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 이동한 판매점의 이름입니다.
     *
     * <p>
     * 이 필드는 사용자가 이동한 판매점의 이름을 나타내며, 사용자의 이동 기록을 식별하는 데 사용됩니다.
     * </p>
     */
    @Column(name = "store_name")
    private String storeName;

    /**
     * 이동한 판매점의 주소입니다.
     *
     * <p>
     * 이 필드는 사용자가 이동한 판매점의 상세 주소를 나타내며, 위치 기반 서비스를 지원하는 데 사용됩니다.
     * </p>
     */
    @Column(name = "store_address")
    private String storeAddress;

    /**
     * 이동한 거리입니다.
     *
     * <p>
     * 이 필드는 사용자가 이동한 거리를 킬로미터(KM) 단위로 나타내며, 소수점 이하 두 자리까지의 정밀도를 가집니다.
     * </p>
     */
    @Column(name = "distance")
    private BigDecimal distance;

    /**
     * 트랙이 기록된 시각입니다.
     *
     * <p>
     * 이 필드는 트랙이 생성된 날짜와 시간을 {@link LocalDateTime} 형식으로 나타냅니다.
     * 자동으로 생성되며, 변경되지 않습니다.
     * </p>
     */
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * 트랙을 기록한 사용자입니다.
     *
     * <p>
     * 이 필드는 {@link User} 엔티티와의 다대일(N:1) 관계를 나타내며, 사용자가 여러 개의 트랙을 가질 수 있습니다.
     * 지연 로딩(LAZY) 전략을 사용하여 성능을 최적화합니다.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 트랙 엔티티의 생성자입니다.
     *
     * <p>
     * 이 생성자는 판매점 이름, 판매점 주소, 이동 거리, 사용자 정보를 기반으로 새로운 트랙 엔티티를 생성합니다.
     * </p>
     *
     * @param storeName    이동한 판매점의 이름 {@link String}
     * @param storeAddress 이동한 판매점의 주소 {@link String}
     * @param distance     이동한 거리 {@link BigDecimal}
     * @param user         트랙을 기록한 사용자 {@link User}
     */
    public Track(String storeName, String storeAddress, BigDecimal distance, User user) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.distance = distance;
        this.user = user;
    }
}
