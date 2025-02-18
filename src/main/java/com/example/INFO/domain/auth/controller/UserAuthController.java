package com.example.INFO.domain.auth.controller;

import com.example.INFO.domain.auth.dto.JwtTokenDto;
import com.example.INFO.domain.auth.dto.request.UserLoginRequest;
import com.example.INFO.domain.auth.dto.request.UserRefreshRequest;
import com.example.INFO.domain.auth.dto.response.UserLoginResponse;
import com.example.INFO.domain.auth.dto.response.UserRefreshResponse;
import com.example.INFO.domain.auth.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        JwtTokenDto jwtTokenDto = userAuthService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.status(HttpStatus.OK)
                .body(UserLoginResponse.from(jwtTokenDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserRefreshResponse> refresh(@RequestBody UserRefreshRequest request) {
        JwtTokenDto jwtTokenDto = userAuthService.refresh(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK)
                .body(UserRefreshResponse.from(jwtTokenDto));
    }
}
