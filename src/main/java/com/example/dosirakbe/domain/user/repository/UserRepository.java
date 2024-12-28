package com.example.dosirakbe.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.dosirakbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


/**
 * packageName    : com.example.dosirakbe.domain.user.repository<br>
 * fileName       : UserRepository<br>
 * author         : yyujin1231<br>
 * date           : 10/20/24<br>
 * description    : 사용자 정보를 저장하고 관리하는 리포지토리 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/20/24        yyujin1231                최초 생성<br>
 */



public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 사용자 고유이름으로 사용자를 조회합니다.
     *
     * @param userName 조회할 사용자의 고유이름
     * @return 사용자 객체를 포함한 Optional 객체
     */


    Optional<User> findByUserName(String userName);

    /**
     * 닉네임이 이미 사용 중인지 확인합니다.
     *
     * @param nickName 확인할 닉네임
     * @return 닉네임이 존재하면 true, 그렇지 않으면 false
     */
    boolean existsByNickName(String nickName);

    /**
     * 특정 사용자에게 리워드 포인트를 추가합니다.
     *
     * @param userId 리워드 포인트를 추가할 사용자의 ID
     * @param reward 추가할 리워드 포인트
     */


    @Modifying
    @Query("UPDATE User u SET u.reward = u.reward + :reward WHERE u.userId = :userId")
    void updateReward(@Param("userId") Long userId, @Param("reward") int reward);



}
