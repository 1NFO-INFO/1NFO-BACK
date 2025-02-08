package com.example.INFO.domain.user.model.entity;

import com.example.INFO.domain.user.model.constant.OAuthProvider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "\"oauth_details\"")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class OAuthDetailsEntity {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "oauth_provider", nullable = false)
    private OAuthProvider provider;

    public OAuthDetailsEntity(UserEntity user, String email, OAuthProvider provider) {
        this.user = user;
        this.email = email;
        this.provider = provider;
    }

    public static OAuthDetailsEntity of(UserEntity user, String email, OAuthProvider provider) {
        return new OAuthDetailsEntity(user, email, provider);
    }
}
