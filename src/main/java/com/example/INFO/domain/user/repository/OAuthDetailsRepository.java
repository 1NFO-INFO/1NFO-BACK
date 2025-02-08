package com.example.INFO.domain.user.repository;

import com.example.INFO.domain.user.model.entity.OAuthDetailsEntity;
import com.example.INFO.domain.user.model.constant.OAuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthDetailsRepository extends JpaRepository<OAuthDetailsEntity, Long> {

    boolean existsByEmailAndProvider(String email, OAuthProvider provider);

    Optional<OAuthDetailsEntity> findByEmailAndProvider(String email, OAuthProvider provider);
}
