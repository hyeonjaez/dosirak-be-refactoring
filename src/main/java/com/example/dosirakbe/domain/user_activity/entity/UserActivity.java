package com.example.dosirakbe.domain.user_activity.entity;

import com.example.dosirakbe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Objects;

/**
 * packageName    : com.example.dosirakbe.domain.user_activity.entity<br>
 * fileName       : UserActivity<br>
 * author         : Fiat_lux<br>
 * date           : 10/31/24<br>
 * description    : 사용자의 활동 기록을 저장하는 엔티티 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/31/24        Fiat_lux                최초 생성<br>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_activites")
@EntityListeners(AuditingEntityListener.class)
public class UserActivity {

    /**
     * 사용자 활동의 고유 식별자입니다.
     *
     * <p>
     * 이 필드는 사용자 활동을 유일하게 식별하기 위한 ID를 나타냅니다.
     * </p>
     */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 활동이 기록된 날짜입니다.
     *
     * <p>
     * 이 필드는 사용자의 활동이 발생한 날짜를 {@link LocalDate} 형식으로 나타냅니다.
     * 자동으로 생성되며, 변경되지 않습니다.
     * </p>
     */
    @CreatedDate
    @Column(name = "created_at")
    private LocalDate createdAt;

    /**
     * 해당 날짜에 사용자가 수행한 커밋 수입니다.
     *
     * <p>
     * 이 필드는 특정 날짜에 사용자가 수행한 커밋의 총 개수를 {@link Integer} 형식으로 나타냅니다.
     * </p>
     */
    @Column(name = "commit_count")
    private Integer commitCount;

    /**
     * 활동을 기록한 사용자입니다.
     *
     * <p>
     * 이 필드는 {@link User} 엔티티와의 다대일(N:1) 관계를 나타내며, 사용자가 여러 개의 활동 기록을 가질 수 있습니다.
     * 지연 로딩(LAZY) 전략을 사용하여 성능을 최적화합니다.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 사용자 활동 엔티티의 생성자입니다.
     *
     * <p>
     * 이 생성자는 활동을 기록할 사용자를 기반으로 새로운 활동 엔티티를 생성합니다.
     * </p>
     *
     * @param user 활동을 기록할 {@link User} 엔티티 객체
     */
    public UserActivity(User user) {
        this.user = user;
        this.commitCount = 1;
    }

    /**
     * 엔티티가 영속화되기 전에 호출되는 메서드입니다.
     *
     * <p>
     * 이 메서드는 {@link #commitCount} 필드가 {@code null}인 경우 기본값으로 1을 설정합니다.
     * </p>
     */
    @PrePersist
    public void prePersist() {
        if (Objects.isNull(this.commitCount)) {
            this.commitCount = 1;
        }

        if (Objects.isNull(this.createdAt)) {
            this.createdAt = LocalDate.now();
        }
    }

    /**
     * 활동의 커밋 수를 증가시킵니다.
     *
     * <p>
     * 이 메서드는 {@link #commitCount} 필드의 값을 1씩 증가시킵니다.
     * </p>
     */
    public void addCommitCount() {
        this.commitCount++;
    }
}
