package com.example.INFO.domain.user.controller;

import com.example.INFO.domain.user.dto.JwtTokenDto;
import com.example.INFO.domain.user.dto.KakaoOAuthUserInfoDto;
import com.example.INFO.domain.user.dto.response.UserLoginResponse;
import com.example.INFO.domain.user.service.KakaoOAuthService;
import com.example.INFO.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/oauth/kakao")
@RequiredArgsConstructor
@Log4j2
public class KakaoOAuthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final UserService userService;

    @GetMapping("/authorize")
    public ResponseEntity<Void> authorize() {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(kakaoOAuthService.getAuthorizationUri()))
                .build();
    }

    @GetMapping("/callback")
    public ResponseEntity<UserLoginResponse> callback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String errorDescription
    ) {
        if (error != null) {
            log.debug("Error occurs while getting authorization code. {}", errorDescription);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String accessToken = kakaoOAuthService.getAccessToken(code);
        KakaoOAuthUserInfoDto userInfo = kakaoOAuthService.getUserInfo(accessToken);

        userService.tryCreateUser(userInfo);
        JwtTokenDto jwtTokenDto = userService.login(userInfo);

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserLoginResponse.from(jwtTokenDto));
    }
}
