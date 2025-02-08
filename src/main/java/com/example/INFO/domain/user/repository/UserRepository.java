package com.example.INFO.domain.user.repository;

import com.example.INFO.domain.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLocalAuthDetailsUsername(String username);
}
