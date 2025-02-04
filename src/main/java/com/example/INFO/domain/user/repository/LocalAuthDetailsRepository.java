package com.example.INFO.domain.user.repository;

import com.example.INFO.domain.user.model.entity.LocalAuthDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocalAuthDetailsRepository extends JpaRepository<LocalAuthDetailsEntity, Long> {

    boolean existsByUsername(String username);
    Optional<LocalAuthDetailsEntity> findByUsername(String username);
}
