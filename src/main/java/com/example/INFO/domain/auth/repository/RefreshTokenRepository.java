package com.example.INFO.domain.auth.repository;

import com.example.INFO.domain.auth.model.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    boolean existsByValue(String value);

    void deleteByExpirationBefore(LocalDateTime now);
}
