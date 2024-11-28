package com.example.dosirakbe.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.dosirakbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUserName(String userName);
    boolean existsByNickName(String nickName);

    List<User> findAllByOrderByRewardDesc();

    @Modifying
    @Query("UPDATE User u SET u.reward = u.reward + :reward WHERE u.userId = :userId")
    void updateReward(@Param("userId") Long userId, @Param("reward") int reward);



}
