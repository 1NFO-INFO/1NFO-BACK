package com.example.INFO.user.service;

import com.example.INFO.user.exception.UserException;
import com.example.INFO.user.exception.UserExceptionType;
import com.example.INFO.user.model.entity.LocalAuthDetailsEntity;
import com.example.INFO.user.repository.LocalAuthDetailsRepository;
import com.example.INFO.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final LocalAuthDetailsRepository localAuthDetailsRepository = mock(LocalAuthDetailsRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final JwtTokenService jwtTokenService = mock(JwtTokenService.class);

    private final UserService userService = new UserService(
            userRepository, localAuthDetailsRepository, passwordEncoder, jwtTokenService
    );

    @Test
    void 로컬회원_생성() {
        String username = "username";
        String password = "password";

        when(localAuthDetailsRepository.existsByUsername(username)).thenReturn(false);

        assertThatCode(() -> userService.createUser(username, password))
                .doesNotThrowAnyException();
    }

    @Test
    void 로컬회원_생성시_이미_존재하는_username이_있다면_예외가_발생한다() {
        String username = "username";
        String password = "password";

        when(localAuthDetailsRepository.existsByUsername(username)).thenReturn(true);

        UserException e = assertThrows(UserException.class, () -> userService.createUser(username, password));
        assertThat(e.getType()).isEqualTo(UserExceptionType.DUPLICATED_USERNAME);
    }

    @Test
    void 로그인() {
        String username = "username";
        String password = "password";

        LocalAuthDetailsEntity localAuthDetails = mock(LocalAuthDetailsEntity.class);

        when(localAuthDetailsRepository.findByUsername(username)).thenReturn(Optional.of(localAuthDetails));
        when(passwordEncoder.matches(password, localAuthDetails.getPassword())).thenReturn(true);

        assertThatCode(() -> userService.login(username, password))
                .doesNotThrowAnyException();
    }

    @Test
    void 로그인시_username으로_회원가입된_유저가_없다면_예외가_발생한다() {
        String username = "username";
        String password = "password";

        when(localAuthDetailsRepository.findByUsername(username)).thenReturn(Optional.empty());

        UserException e = Assertions.assertThrows(UserException.class, () -> {
            userService.login(username, password);
        });
        assertEquals(UserExceptionType.USER_NOT_FOUND, e.getType());
    }

    @Test
    void 로그인시_패스워드가_틀렸다면_예외가_발생한다() {
        String username = "username";
        String wrongPassword = "wrongPassword";

        LocalAuthDetailsEntity localAuthDetails = mock(LocalAuthDetailsEntity.class);
        when(localAuthDetailsRepository.findByUsername(username)).thenReturn(Optional.of(localAuthDetails));
        when(passwordEncoder.matches(wrongPassword, localAuthDetails.getPassword())).thenReturn(false);

        UserException e = Assertions.assertThrows(UserException.class, () -> {
            userService.login(username, wrongPassword);
        });
        assertEquals(UserExceptionType.INVALID_PASSWORD, e.getType());
    }
}
