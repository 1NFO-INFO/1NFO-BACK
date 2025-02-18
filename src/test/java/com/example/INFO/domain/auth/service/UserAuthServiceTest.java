package com.example.INFO.domain.auth.service;

import com.example.INFO.domain.auth.dto.JwtTokenDto;
import com.example.INFO.domain.auth.model.entity.LocalAuthDetailsEntity;
import com.example.INFO.domain.auth.repository.LocalAuthDetailsRepository;
import com.example.INFO.domain.auth.repository.OAuthDetailsRepository;
import com.example.INFO.domain.auth.repository.RefreshTokenRepository;
import com.example.INFO.global.exception.DefaultException;
import com.example.INFO.global.payload.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserAuthServiceTest {

    private final OAuthDetailsRepository oAuthDetailsRepository = mock(OAuthDetailsRepository.class);
    private final JwtTokenService jwtTokenService = mock(JwtTokenService.class);
    private final RefreshTokenRepository refreshTokenRepository = mock(RefreshTokenRepository.class);
    private final LocalAuthDetailsRepository localAuthDetailsRepository = mock(LocalAuthDetailsRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    private final UserAuthService userAuthService = new UserAuthService(
            oAuthDetailsRepository,
            jwtTokenService,
            refreshTokenRepository,
            localAuthDetailsRepository,
            passwordEncoder
    );

    @Test
    void 로그인() {
        String username = "username";
        String password = "password";

        LocalAuthDetailsEntity localAuthDetails = mock(LocalAuthDetailsEntity.class);

        when(localAuthDetailsRepository.findByUsername(username)).thenReturn(Optional.of(localAuthDetails));
        when(passwordEncoder.matches(password, localAuthDetails.getPassword())).thenReturn(true);
        JwtTokenDto jwtToken = mock(JwtTokenDto.class);
        when(jwtToken.getRefreshToken()).thenReturn("refresh-token");
        when(jwtTokenService.generateJwtToken(anyLong())).thenReturn(jwtToken);

        assertThatCode(() -> userAuthService.login(username, password))
                .doesNotThrowAnyException();
    }

    @Test
    void 로그인시_username으로_회원가입된_유저가_없다면_예외가_발생한다() {
        String username = "username";
        String password = "password";

        when(localAuthDetailsRepository.findByUsername(username)).thenReturn(Optional.empty());

        DefaultException e = Assertions.assertThrows(DefaultException.class, () -> {
            userAuthService.login(username, password);
        });
        assertEquals(ErrorCode.NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 로그인시_패스워드가_틀렸다면_예외가_발생한다() {
        String username = "username";
        String wrongPassword = "wrongPassword";

        LocalAuthDetailsEntity localAuthDetails = mock(LocalAuthDetailsEntity.class);
        when(localAuthDetailsRepository.findByUsername(username)).thenReturn(Optional.of(localAuthDetails));
        when(passwordEncoder.matches(wrongPassword, localAuthDetails.getPassword())).thenReturn(false);

        DefaultException e = Assertions.assertThrows(DefaultException.class, () -> {
            userAuthService.login(username, wrongPassword);
        });
        assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());
    }

    @Test
    void 리프레쉬() {
        long userId = 0;
        String refreshToken = "refresh-token";

        setup_리프레쉬_test(userId, refreshToken);

        assertDoesNotThrow(() -> userAuthService.refresh(refreshToken));
    }

    @Test
    void 리프레쉬시_리프레쉬_토큰이_만료되었다면_예외가_발생한다() {
        long userId = 0;
        String refreshToken = "refresh-token";

        setup_리프레쉬_test(userId, refreshToken);
        when(jwtTokenService.isExpired(refreshToken)).thenReturn(true);

        DefaultException e = Assertions.assertThrows(DefaultException.class, () -> {
            userAuthService.refresh(refreshToken);
        });
        assertEquals(ErrorCode.INVALID_AUTHENTICATION, e.getErrorCode());
    }

    @Test
    void 리프레쉬시_저장되어_있지_않은_리프레쉬_토큰이라면_예외가_발생한다() {
        long userId = 0;
        String refreshToken = "refresh-token";

        setup_리프레쉬_test(userId, refreshToken);
        when(refreshTokenRepository.existsByValue(refreshToken)).thenReturn(false);

        DefaultException e = Assertions.assertThrows(DefaultException.class, () -> {
            userAuthService.refresh(refreshToken);
        });
        assertEquals(ErrorCode.INVALID_AUTHENTICATION, e.getErrorCode());
    }

    private void setup_리프레쉬_test(long userId, String refreshToken) {
        when(refreshTokenRepository.existsByValue(refreshToken)).thenReturn(true);
        when(jwtTokenService.getUserId(refreshToken)).thenReturn(userId);
        when(jwtTokenService.isExpired(refreshToken)).thenReturn(false);
        when(jwtTokenService.generateJwtToken(userId)).thenReturn(mock(JwtTokenDto.class));
    }
}
