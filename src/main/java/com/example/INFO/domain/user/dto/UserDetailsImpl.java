package com.example.INFO.domain.user.dto;

import com.example.INFO.domain.user.model.entity.LocalAuthDetailsEntity;
import com.example.INFO.domain.user.model.entity.UserEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDetailsImpl implements UserDetails {

    long id;
    String username;
    String password;
    boolean isEnabled;

    public static UserDetails fromEntity(UserEntity user) {
        LocalAuthDetailsEntity localAuthDetails = user.getLocalAuthDetails();

        return new UserDetailsImpl(
                user.getId(),
                localAuthDetails.getUsername(),
                localAuthDetails.getPassword(),
                user.getDeletedAt() != null
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
