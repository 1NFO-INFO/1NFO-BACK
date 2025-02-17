package com.example.INFO.domain.auth.service;

import com.example.INFO.domain.auth.dto.JwtTokenDto;
import com.example.INFO.domain.auth.dto.KakaoOAuthUserInfoDto;
import com.example.INFO.domain.auth.model.constant.OAuthProvider;
import com.example.INFO.domain.auth.model.entity.LocalAuthDetailsEntity;
import com.example.INFO.domain.auth.model.entity.OAuthDetailsEntity;
import com.example.INFO.domain.auth.model.entity.RefreshTokenEntity;
import com.example.INFO.domain.auth.repository.LocalAuthDetailsRepository;
import com.example.INFO.domain.auth.repository.OAuthDetailsRepository;
import com.example.INFO.domain.auth.repository.RefreshTokenRepository;
import com.example.INFO.global.exception.DefaultException;
import com.example.INFO.global.payload.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserAuthService {

    private final OAuthDetailsRepository oAuthDetailsRepository;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LocalAuthDetailsRepository localAuthDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    public JwtTokenDto login(KakaoOAuthUserInfoDto kakaoOAuthUserInfoDto) {
        String email = kakaoOAuthUserInfoDto.getEmail();

        OAuthDetailsEntity kakaoOAuthDetails = oAuthDetailsRepository.findByEmailAndProvider(kakaoOAuthUserInfoDto.getEmail(), OAuthProvider.KAKAO)
                .orElseThrow(() -> new DefaultException(ErrorCode.NOT_FOUND));

        JwtTokenDto jwtTokenDto = jwtTokenService.generateJwtToken(kakaoOAuthDetails.getId());
        String refreshToken = jwtTokenDto.getRefreshToken();
        LocalDateTime refreshTokenIssuedAt = jwtTokenService.getIssuedAt(refreshToken);
        LocalDateTime refreshTokenExpiration = jwtTokenService.getExpiration(refreshToken);

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.of(
                kakaoOAuthDetails.getUser(),
                jwtTokenDto.getRefreshToken(),
                refreshTokenIssuedAt,
                refreshTokenExpiration
        );

        refreshTokenRepository.save(refreshTokenEntity);

        return jwtTokenDto;
    }

    public JwtTokenDto login(String username, String password) {
        log.debug("login try: username: {}, password: {}", username, password);
        LocalAuthDetailsEntity localAuthDetails = localAuthDetailsRepository.findByUsername(username)
                .orElseThrow(() -> new DefaultException(ErrorCode.NOT_FOUND));

        if (!passwordEncoder.matches(password, localAuthDetails.getPassword())) {
            log.debug("password is not matched");
            throw new DefaultException(ErrorCode.INVALID_PASSWORD);
        }

        JwtTokenDto jwtTokenDto = jwtTokenService.generateJwtToken(localAuthDetails.getId());
        String refreshToken = jwtTokenDto.getRefreshToken();
        LocalDateTime refreshTokenIssuedAt = jwtTokenService.getIssuedAt(refreshToken);
        LocalDateTime refreshTokenExpiration = jwtTokenService.getExpiration(refreshToken);

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.of(localAuthDetails.getUser(), jwtTokenDto.getRefreshToken(), refreshTokenIssuedAt, refreshTokenExpiration);
        refreshTokenRepository.save(refreshTokenEntity);

        return jwtTokenDto;
    }

    public JwtTokenDto refresh(String refreshToken) {
        // refresh token이 만료되었는지 확인
        if (jwtTokenService.isExpired(refreshToken)) {
            throw new DefaultException(ErrorCode.INVALID_AUTHENTICATION);
        }

        // repository에서 refresh token이 존재하는지 확인
        if (!refreshTokenRepository.existsByValue(refreshToken)) {
            throw new DefaultException(ErrorCode.INVALID_AUTHENTICATION);
        }

        // refresh token이 유효하다면 새로운 JWT token을 발급
        long userId = jwtTokenService.getUserId(refreshToken);
        return jwtTokenService.generateJwtToken(userId);
    }
}
