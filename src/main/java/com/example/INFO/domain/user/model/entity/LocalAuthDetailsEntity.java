package com.example.INFO.domain.user.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "\"local_auth_details\"")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class LocalAuthDetailsEntity {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    private LocalAuthDetailsEntity(UserEntity user, String username, String password) {
        this.user = user;
        this.username = username;
        this.password = password;
    }

    public static LocalAuthDetailsEntity of(UserEntity user, String username, String password) {
        return new LocalAuthDetailsEntity(user, username, password);
    }
}
