package com.example.INFO.user.repository;

import com.example.INFO.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLocalAuthDetailsUsername(String username);
}
