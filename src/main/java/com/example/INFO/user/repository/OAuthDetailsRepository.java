package com.example.INFO.user.repository;

import com.example.INFO.user.model.constant.OAuthProvider;
import com.example.INFO.user.model.entity.OAuthDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthDetailsRepository extends JpaRepository<OAuthDetailsEntity, Long> {

    boolean existsByEmailAndProvider(String email, OAuthProvider provider);

    Optional<OAuthDetailsEntity> findByEmailAndProvider(String email, OAuthProvider provider);
}
