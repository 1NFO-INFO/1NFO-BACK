package com.example.INFO.domain.user.service;

import com.example.INFO.domain.user.model.entity.LocalAuthDetailsEntity;
import com.example.INFO.domain.user.model.entity.RefreshTokenEntity;
import com.example.INFO.domain.user.model.entity.UserEntity;
import com.example.INFO.domain.user.dto.JwtTokenDto;
import com.example.INFO.domain.user.exception.UserException;
import com.example.INFO.domain.user.exception.UserExceptionType;
import com.example.INFO.domain.user.properties.JwtProperties;
import com.example.INFO.domain.user.repository.LocalAuthDetailsRepository;
import com.example.INFO.domain.user.repository.RefreshTokenRepository;
import com.example.INFO.domain.user.repository.UserRepository;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final JwtProperties jwtProperties;

    public void createUser(String username, String password) {
        if (localAuthDetailsRepository.existsByUsername(username)) {
            log.debug("username: {} is duplicated", username);
            throw new UserException(UserExceptionType.DUPLICATED_USERNAME);
        }

        UserEntity user = userRepository.save(UserEntity.of(username));
        log.info("user: {} is created", user);

        LocalAuthDetailsEntity localAuthDetails = localAuthDetailsRepository.save(
                LocalAuthDetailsEntity.of(user, username, passwordEncoder.encode(password))
        );
        log.info("localAuthDetails: {} is created", localAuthDetails);
    }

    public JwtTokenDto login(String username, String password) {
        log.debug("login try: username: {}, password: {}", username, password);
        LocalAuthDetailsEntity localAuthDetails = localAuthDetailsRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, localAuthDetails.getPassword())) {
            log.debug("password is not matched");
            throw new UserException(UserExceptionType.INVALID_PASSWORD);
        }

        JwtTokenDto jwtTokenDto = jwtTokenService.generateJwtToken(username);
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
            throw new UserException(UserExceptionType.INVALID_REFRESH_TOKEN);
        }

        // repository에서 refresh token이 존재하는지 확인
        if (!refreshTokenRepository.existsByValue(refreshToken)) {
            throw new UserException(UserExceptionType.INVALID_REFRESH_TOKEN);
        }

        // refresh token이 유효하다면 새로운 JWT token을 발급
        String username = jwtTokenService.getUsername(refreshToken);
        return jwtTokenService.generateJwtToken(username);
    }
}
