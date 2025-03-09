package com.example.INFO.domain.cheongyak.repository;

import com.example.INFO.domain.cheongyak.model.entity.CheongyakDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CheongyakDetailsRepository extends JpaRepository<CheongyakDetailsEntity, String>, JpaSpecificationExecutor<CheongyakDetailsEntity> {
}
