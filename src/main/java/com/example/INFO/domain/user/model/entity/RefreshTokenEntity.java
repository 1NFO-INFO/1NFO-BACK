package com.example.INFO.domain.user.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "\"refresh_token\"")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class RefreshTokenEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "refresh_token_value", nullable = false)
    private String value;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime issuedAt;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiration;

    private RefreshTokenEntity(UserEntity user, String value, LocalDateTime issuedAt, LocalDateTime expiresAt) {
        this.user = user;
        this.value = value;
        this.issuedAt = issuedAt;
        this.expiration = expiresAt;
    }

    public static RefreshTokenEntity of(
            UserEntity user, String value, LocalDateTime createdAt, LocalDateTime expiresAt
    ) {
        return new RefreshTokenEntity(user, value, createdAt, expiresAt);
    }
}
