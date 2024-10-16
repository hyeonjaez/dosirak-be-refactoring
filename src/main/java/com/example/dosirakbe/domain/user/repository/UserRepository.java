package com.example.dosirakbe.domain.user.repository;

import com.example.dosirakbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
