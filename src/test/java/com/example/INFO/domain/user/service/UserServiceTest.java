package com.example.INFO.domain.user.service;

import com.example.INFO.domain.auth.dto.KakaoOAuthUserInfoDto;
import com.example.INFO.domain.auth.model.constant.OAuthProvider;
import com.example.INFO.domain.auth.model.entity.LocalAuthDetailsEntity;
import com.example.INFO.domain.auth.repository.LocalAuthDetailsRepository;
import com.example.INFO.domain.auth.repository.OAuthDetailsRepository;
import com.example.INFO.domain.auth.service.AuthUserService;
import com.example.INFO.domain.user.dto.LocalUserCreateDto;
import com.example.INFO.domain.user.dto.UserUpdateDto;
import com.example.INFO.domain.user.dto.request.UserUpdateRequest;
import com.example.INFO.domain.user.model.entity.UserEntity;
import com.example.INFO.domain.user.repository.UserRepository;
import com.example.INFO.domain.auth.service.JwtTokenService;
import com.example.INFO.global.exception.CustomException;
import com.example.INFO.global.exception.DefaultException;
import com.example.INFO.global.payload.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.*;

class UserServiceTest {

    private final AuthUserService authUserService = mock(AuthUserService.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final LocalAuthDetailsRepository localAuthDetailsRepository = mock(LocalAuthDetailsRepository.class);
    private final OAuthDetailsRepository oAuthDetailsRepository = mock(OAuthDetailsRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final JwtTokenService jwtTokenService = mock(JwtTokenService.class);

    private final UserService userService = new UserService(
            authUserService,
            userRepository,
            localAuthDetailsRepository,
            oAuthDetailsRepository,
            passwordEncoder
    );

    @Test
    void 로컬회원_생성() {
        String username = "username";
        String password = "password";
        String phoneNumber = "010-1234-5678";
        LocalUserCreateDto localUserCreateDto = LocalUserCreateDto.of(username, password, phoneNumber);

        when(localAuthDetailsRepository.existsByUsername(username)).thenReturn(false);

        assertThatCode(() -> userService.createUser(localUserCreateDto))
                .doesNotThrowAnyException();
    }

    @Test
    void 로컬회원_생성시_이미_존재하는_username이_있다면_예외가_발생한다() {
        String username = "username";
        String password = "password";
        String phoneNumber = "010-1234-5678";
        LocalUserCreateDto localUserCreateDto = LocalUserCreateDto.of(username, password, phoneNumber);

        when(localAuthDetailsRepository.existsByUsername(username)).thenReturn(true);

        DefaultException e = assertThrows(DefaultException.class, () -> userService.createUser(localUserCreateDto));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_ERROR);
    }

    @Test
    void 회원_생성_카카오_OAuth() {
        String email = "email";
        String phoneNumber = "010-1234-5678";

        OAuthProvider provider  = OAuthProvider.KAKAO;
        KakaoOAuthUserInfoDto userInfo = KakaoOAuthUserInfoDto.of(email, phoneNumber);

        when(oAuthDetailsRepository.existsByEmailAndProvider(email, provider)).thenReturn(false);

        assertThatCode(() -> userService.createUser(userInfo))
                .doesNotThrowAnyException();
    }

    @Test
    void 카카오_OAuth_회원_생성시_이미_존재하는_username이_있다면_예외가_발생한다() {
        String email = "email";
        String phoneNumber = "010-1234-5678";

        OAuthProvider provider  = OAuthProvider.KAKAO;
        KakaoOAuthUserInfoDto userInfo = KakaoOAuthUserInfoDto.of(email, "010-1234-5678");

        when(oAuthDetailsRepository.existsByEmailAndProvider(email, provider)).thenReturn(true);

        DefaultException e = assertThrows(DefaultException.class, () -> userService.createUser(userInfo));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_ERROR);
    }

    @Test
    void 회원_정보_가져오기_성공() {
        long userId = 1L;

        given(authUserService.getAuthenticatedUserId()).willReturn(userId);
        given(userRepository.findById(userId)).willReturn(Optional.of(mock(UserEntity.class)));

        assertThatCode(userService::getUserInfo)
                .doesNotThrowAnyException();
    }

    @Test
    void 회원_정보_가져오기_실패_존재하지_않는_유저() {
        long userId = 1L;

        given(authUserService.getAuthenticatedUserId()).willReturn(userId);
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        CustomException e = assertThrows(CustomException.class, userService::getUserInfo);
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    void 닉네임_초기화_가져오기_성공() {
        long userId = 1L;
        String nickname = "nickname";

        given(authUserService.getAuthenticatedUserId()).willReturn(userId);
        given(userRepository.findById(userId)).willReturn(Optional.of(mock(UserEntity.class)));

        assertThatCode(() -> userService.updateNickname(nickname))
                .doesNotThrowAnyException();
    }

    @Test
    void 닉네임_초기화_실패_존재하지_않는_유저() {
        long userId = 1L;
        String nickname = "nickname";

        given(authUserService.getAuthenticatedUserId()).willReturn(userId);
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        CustomException e = assertThrows(CustomException.class, () -> userService.updateNickname(nickname));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    void 회원_정보_수정_성공_로컬_유저() {
        long userId = 1L;
        String nickname = "nickname";
        String password = "password";
        String phoneNumber = "010-1234-5678";
        UserUpdateDto userUpdateDto = UserUpdateDto.fromRequest(new UserUpdateRequest(nickname, password, phoneNumber));
        boolean isLocalUser = false;

        given(authUserService.getAuthenticatedUserId()).willReturn(userId);
        given(userRepository.findById(userId)).willReturn(Optional.of(mock(UserEntity.class)));
        given(localAuthDetailsRepository.findById(userId)).willReturn(Optional.of(mock(LocalAuthDetailsEntity.class)));

        assertThatCode(() -> userService.updateUserInfo(userUpdateDto, isLocalUser))
                .doesNotThrowAnyException();
    }

    @Test
    void 회원_정보_수정_성공_OAuth_유저() {
        long userId = 1L;
        String nickname = "nickname";
        String password = "password";
        String phoneNumber = "010-1234-5678";

        UserUpdateDto userUpdateDto = UserUpdateDto.fromRequest(new UserUpdateRequest(nickname, password, phoneNumber));
        boolean isLocalUser = false;

        given(authUserService.getAuthenticatedUserId()).willReturn(userId);
        given(userRepository.findById(userId)).willReturn(Optional.of(mock(UserEntity.class)));

        assertThatCode(() -> userService.updateUserInfo(userUpdateDto, isLocalUser))
                .doesNotThrowAnyException();
    }
}
