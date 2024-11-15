package com.example.dosirakbe.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.dosirakbe.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUserName(String userName);
    boolean existsByNickName(String nickName);

    List<User> findAllByOrderByRewardDesc();



}
