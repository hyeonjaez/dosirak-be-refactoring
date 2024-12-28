package com.example.dosirakbe.domain.auth.entity;


import com.example.dosirakbe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * packageName    : com.example.dosirakbe.domain.auth.entity<br>
 * fileName       : RefreshToken<br>
 * author         : yyujin1231<br>
 * date           : 10/20/24<br>
 * description    : 사용자 인증을 위한 리프레시 토큰 엔티티 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/20/24        yyujin1231                최초 생성<br>
 */


@Entity
@Getter
@NoArgsConstructor()
@Builder
@AllArgsConstructor
@Table(name = "tokens")
public class RefreshToken {

    /**
     * 리프레시 토큰의 고유 식별자입니다.
     *
     * <p>
     * 이 필드는 데이터베이스에서 자동 생성되는 고유한 ID를 나타냅니다.
     * </p>
     */

    @Id
    @Column(name = "token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 리프레시 토큰과 연결된 사용자 정보입니다.
     * <p>
     * {@link User} 엔티티와 1:1 관계를 가집니다.
     * </p>
     */

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 리프레시 토큰 값입니다.
     */

    @Column(nullable = false, name = "refresh_token")
    private String refreshToken;


    /**
     * 새로운 RefreshToken을 생성합니다.
     *
     * @param user         리프레시 토큰과 연결된 사용자
     * @param refreshToken 리프레시 토큰
     */

    public RefreshToken(User user, String refreshToken) {
        this.user = user;
        this.refreshToken = refreshToken;
    }


   public void setRefreshToken(String refreshToken) {
       this.refreshToken = refreshToken;
    }

    public void setUser(User user) {
        this.user = user;
    }



}
