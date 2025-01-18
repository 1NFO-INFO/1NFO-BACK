package com.example.INFO.user.repository;

import com.example.INFO.user.model.entity.LocalAuthDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalAuthDetailsRepository extends JpaRepository<LocalAuthDetailsEntity, Long> {

    boolean existsByUsername(String username);
}
