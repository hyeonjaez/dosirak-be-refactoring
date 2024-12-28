package com.example.dosirakbe.domain.auth.repository;

import com.example.dosirakbe.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

/**
 * packageName    : com.example.dosirakbe.domain.auth.repository<br>
 * fileName       : RefreshTokenRepository<br>
 * author         : yyujin1231<br>
 * date           : 10/20/24<br>
 * description    : RefreshToken 엔티티에 접근하기 위한 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/20/24        yyujin1231                최초 생성<br>
 */


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * 사용자의 ID를 기반으로 리프레시 토큰을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 사용자 ID에 해당하는 리프레시 토큰을 포함한 Optional 객체
     */

    Optional<RefreshToken> findByUser_UserId(Long userId);

    /**
     * 사용자의 ID를 기반으로 리프레시 토큰을 삭제합니다.
     *
     * @param userId 사용자 ID
     */

    void deleteByUser_UserId(Long userId);


}
