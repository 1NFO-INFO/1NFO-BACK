package com.example.INFO.domain.auth.service;

import com.example.INFO.domain.auth.model.entity.LocalAuthDetailsEntity;
import com.example.INFO.domain.user.model.entity.UserEntity;
import com.example.INFO.domain.user.repository.UserRepository;
import com.example.INFO.global.exception.DefaultException;
import com.example.INFO.global.payload.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDetailsImplServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CustomUserDetailsService userDetailsService = new CustomUserDetailsService(userRepository);

    @Test
    void loadUserByUsername() {
        long userId = 0;
        String password = "password";

        UserEntity user = mock(UserEntity.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getLocalAuthDetails()).thenReturn(LocalAuthDetailsEntity.of(user, "username", password));
        when(user.getDeletedAt()).thenReturn(null);

        assertThatCode(() -> userDetailsService.loadUserByUserId(userId))
                .doesNotThrowAnyException();
    }

    @Test
    void loadUserByUsername_존재하지_않는_유저에_대해_예외가_발생한다() {
        long userId = 0;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        DefaultException e = Assertions.assertThrows(DefaultException.class, () -> {
            userDetailsService.loadUserByUserId(userId);
        });
        Assertions.assertEquals(ErrorCode.NOT_FOUND, e.getErrorCode());
    }
}
