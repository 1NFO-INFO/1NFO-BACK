package com.example.INFO.domain.favorite.domain;

import com.example.INFO.domain.user.model.entity.UserEntity;
import com.example.INFO.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "favorite")
public class Favorite extends BaseEntity { // ✅ BaseEntity 상속

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "entity_id", nullable = false)
    private String entityId; // 좋아요 대상의 ID

    @Column(name = "entity_type", nullable = false)
    private String entityType; // 좋아요 대상의 Type
}
