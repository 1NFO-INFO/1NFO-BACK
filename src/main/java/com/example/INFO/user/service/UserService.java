package com.example.INFO.user.service;

import com.example.INFO.user.exception.UserException;
import com.example.INFO.user.exception.UserExceptionType;
import com.example.INFO.user.model.entity.LocalAuthDetailsEntity;
import com.example.INFO.user.model.entity.UserEntity;
import com.example.INFO.user.repository.LocalAuthDetailsRepository;
import com.example.INFO.user.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final LocalAuthDetailsRepository localAuthDetailsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

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

    public String login(String username, String password) {
        log.debug("login try: username: {}, password: {}", username, password);
        LocalAuthDetailsEntity localAuthDetails = localAuthDetailsRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, localAuthDetails.getPassword())) {
            log.debug("password is not matched");
            throw new UserException(UserExceptionType.INVALID_PASSWORD);
        }

        String token = jwtTokenService.generateToken(username);

        return token;
    }
}
