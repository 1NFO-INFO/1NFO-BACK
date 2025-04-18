package com.example.INFO.domain.user.service;

import com.example.INFO.domain.auth.dto.KakaoOAuthUserInfoDto;
import com.example.INFO.domain.auth.model.constant.OAuthProvider;
import com.example.INFO.domain.auth.model.entity.LocalAuthDetailsEntity;
import com.example.INFO.domain.auth.model.entity.OAuthDetailsEntity;
import com.example.INFO.domain.user.model.entity.UserEntity;
import com.example.INFO.domain.auth.repository.LocalAuthDetailsRepository;
import com.example.INFO.domain.auth.repository.OAuthDetailsRepository;
import com.example.INFO.domain.user.repository.UserRepository;
import com.example.INFO.global.exception.DefaultException;
import com.example.INFO.global.payload.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final LocalAuthDetailsRepository localAuthDetailsRepository;
    private final OAuthDetailsRepository oAuthDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(String username, String password) {
        if (localAuthDetailsRepository.existsByUsername(username)) {
            log.debug("username: {} is duplicated", username);
            throw new DefaultException(ErrorCode.DUPLICATE_ERROR);
        }

        UserEntity user = userRepository.save(UserEntity.of(username));
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

        if (oAuthDetailsRepository.existsByEmailAndProvider(email, OAuthProvider.KAKAO)) {
            throw new DefaultException(ErrorCode.DUPLICATE_ERROR);
        }

        UserEntity user = userRepository.save(UserEntity.of(email));

        oAuthDetailsRepository.save(OAuthDetailsEntity.of(user, email, OAuthProvider.KAKAO));
    }
}
