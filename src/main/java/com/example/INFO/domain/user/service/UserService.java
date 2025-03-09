package com.example.INFO.domain.user.service;

import com.example.INFO.domain.auth.dto.KakaoOAuthUserInfoDto;
import com.example.INFO.domain.auth.model.constant.OAuthProvider;
import com.example.INFO.domain.auth.model.entity.LocalAuthDetailsEntity;
import com.example.INFO.domain.auth.model.entity.OAuthDetailsEntity;
import com.example.INFO.domain.auth.service.AuthUserService;
import com.example.INFO.domain.user.dto.LocalUserCreateDto;
import com.example.INFO.domain.user.dto.UserInfoMeDto;
import com.example.INFO.domain.user.dto.UserUpdateDto;
import com.example.INFO.domain.user.model.entity.UserEntity;
import com.example.INFO.domain.auth.repository.LocalAuthDetailsRepository;
import com.example.INFO.domain.auth.repository.OAuthDetailsRepository;
import com.example.INFO.domain.user.repository.UserRepository;
import com.example.INFO.global.exception.CustomException;
import com.example.INFO.global.exception.DefaultException;
import com.example.INFO.global.payload.ErrorCode;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class UserService {

    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private final AuthUserService authUserService;
    private final UserRepository userRepository;
    private final LocalAuthDetailsRepository localAuthDetailsRepository;
    private final OAuthDetailsRepository oAuthDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(LocalUserCreateDto localUserCreateDto) {
        final String username = localUserCreateDto.getUsername();
        final String password = localUserCreateDto.getPassword();
        final String phoneNumber = localUserCreateDto.getPhoneNumber();

        if (localAuthDetailsRepository.existsByUsername(username)) {
            log.debug("username: {} is duplicated", username);
            throw new DefaultException(ErrorCode.DUPLICATE_ERROR);
        }

        UserEntity user = userRepository.save(UserEntity.of(username, phoneNumber));
        log.info("user: {} is created", user);

        LocalAuthDetailsEntity localAuthDetails = localAuthDetailsRepository.save(
                LocalAuthDetailsEntity.of(user, username, passwordEncoder.encode(password))
        );
        log.info("localAuthDetails: {} is created", localAuthDetails);
    }

    public void tryCreateUser(KakaoOAuthUserInfoDto kakaoOAuthUserInfoDto) {
        try {
            createUser(kakaoOAuthUserInfoDto);
        } catch (DefaultException ignored) {
        }
    }

    public void createUser(KakaoOAuthUserInfoDto kakaoOAuthUserInfoDto) {
        String email = kakaoOAuthUserInfoDto.getEmail();
        String phoneNumber = kakaoOAuthUserInfoDto.getPhoneNumber();

        Phonenumber.PhoneNumber parsedNumber;
        try {
            parsedNumber = phoneNumberUtil.parse(phoneNumber, "KR");
        } catch (Exception e) {
            throw new DefaultException(ErrorCode.INVALID_PHONE_NUMBER);
        }
        String nationalFormatPhoneNumber = phoneNumberUtil.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        String nationalFormatPhoneNumberWithoutHyphen = nationalFormatPhoneNumber.replaceAll("-", "");

        if (oAuthDetailsRepository.existsByEmailAndProvider(email, OAuthProvider.KAKAO)) {
            throw new DefaultException(ErrorCode.DUPLICATE_ERROR);
        }

        UserEntity user = userRepository.save(UserEntity.of(email, nationalFormatPhoneNumberWithoutHyphen));

        oAuthDetailsRepository.save(OAuthDetailsEntity.of(user, email, OAuthProvider.KAKAO));
    }

    public UserInfoMeDto getUserInfo() {
        long userId = authUserService.getAuthenticatedUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        return UserInfoMeDto.fromEntity(user);
    }

    public void updateNickname(String nickname) {
        long userId = authUserService.getAuthenticatedUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        user.updateNickname(nickname);
    }

    public void updateUserInfo(UserUpdateDto userUpdateDto, boolean isLocalUser) {
        long userId = authUserService.getAuthenticatedUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        user.updateUserInfo(userUpdateDto);
        if (isLocalUser) {
            updateLocalAuthPassword(userId, userUpdateDto.getPassword());
        }
    }

    private void updateLocalAuthPassword(long userId, String password) {
        LocalAuthDetailsEntity localAuthDetails = localAuthDetailsRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        localAuthDetails.updatePassword(passwordEncoder.encode(password));
    }
}
