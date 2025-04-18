package com.example.INFO.domain.user.service;

import com.example.INFO.domain.auth.dto.KakaoOAuthUserInfoDto;
import com.example.INFO.domain.auth.model.constant.OAuthProvider;
import com.example.INFO.domain.auth.repository.LocalAuthDetailsRepository;
import com.example.INFO.domain.auth.repository.OAuthDetailsRepository;
import com.example.INFO.domain.user.repository.UserRepository;
import com.example.INFO.domain.auth.service.JwtTokenService;
import com.example.INFO.global.exception.DefaultException;
import com.example.INFO.global.payload.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final LocalAuthDetailsRepository localAuthDetailsRepository = mock(LocalAuthDetailsRepository.class);
    private final OAuthDetailsRepository oAuthDetailsRepository = mock(OAuthDetailsRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final JwtTokenService jwtTokenService = mock(JwtTokenService.class);

    private final UserService userService = new UserService(
            userRepository,
            localAuthDetailsRepository,
            oAuthDetailsRepository,
            passwordEncoder
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

        DefaultException e = assertThrows(DefaultException.class, () -> userService.createUser(username, password));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_ERROR);
    }

    @Test
    void 회원_생성_카카오_OAuth() {
        String email = "email";
        OAuthProvider provider  = OAuthProvider.KAKAO;
        KakaoOAuthUserInfoDto userInfo = KakaoOAuthUserInfoDto.of(email);

        when(oAuthDetailsRepository.existsByEmailAndProvider(email, provider)).thenReturn(false);

        assertThatCode(() -> userService.createUser(userInfo))
                .doesNotThrowAnyException();
    }

    @Test
    void 카카오_OAuth_회원_생성시_이미_존재하는_username이_있다면_예외가_발생한다() {
        String email = "email";
        OAuthProvider provider  = OAuthProvider.KAKAO;
        KakaoOAuthUserInfoDto userInfo = KakaoOAuthUserInfoDto.of(email);

        when(oAuthDetailsRepository.existsByEmailAndProvider(email, provider)).thenReturn(true);

        DefaultException e = assertThrows(DefaultException.class, () -> userService.createUser(userInfo));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_ERROR);
    }


}
