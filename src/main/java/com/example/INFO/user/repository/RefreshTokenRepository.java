package com.example.INFO.user.repository;

import com.example.INFO.user.model.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByValue(String value);
    boolean existsByValue(String value);
}
