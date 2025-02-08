package com.example.INFO.user.service;

import com.example.INFO.domain.user.exception.UserException;
import com.example.INFO.domain.user.exception.UserExceptionType;
import com.example.INFO.domain.user.model.entity.LocalAuthDetailsEntity;
import com.example.INFO.domain.user.model.entity.UserEntity;
import com.example.INFO.domain.user.repository.UserRepository;
import com.example.INFO.domain.user.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepository);

    @Test
    void loadUserByUsername() {
        String username = "username";
        String password = "password";

        UserEntity user = mock(UserEntity.class);
        when(userRepository.findByLocalAuthDetailsUsername(username)).thenReturn(Optional.of(user));
        when(user.getLocalAuthDetails()).thenReturn(LocalAuthDetailsEntity.of(user, username, password));
        when(user.getDeletedAt()).thenReturn(null);

        assertThatCode(() -> userDetailsService.loadUserByUsername(username))
                .doesNotThrowAnyException();
    }

    @Test
    void loadUserByUsername_존재하지_않는_유저에_대해_예외가_발생한다() {
        String username = "username";

        when(userRepository.findByLocalAuthDetailsUsername(username)).thenReturn(Optional.empty());

        UserException e = Assertions.assertThrows(UserException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });
        Assertions.assertEquals(UserExceptionType.USER_NOT_FOUND, e.getType());
    }
}
