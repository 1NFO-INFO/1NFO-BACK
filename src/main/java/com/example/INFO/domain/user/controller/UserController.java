package com.example.INFO.domain.user.controller;

import com.example.INFO.domain.auth.dto.request.UserLoginRequest;
import com.example.INFO.domain.auth.dto.request.UserRefreshRequest;
import com.example.INFO.domain.auth.service.UserAuthService;
import com.example.INFO.domain.user.dto.request.UserSignupRequest;
import com.example.INFO.domain.auth.dto.response.UserLoginResponse;
import com.example.INFO.domain.auth.dto.response.UserRefreshResponse;
import com.example.INFO.domain.user.service.UserService;
import com.example.INFO.domain.auth.dto.JwtTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;
    private final UserAuthService userAuthService;

    @PostMapping
    public ResponseEntity<Void> signup(@RequestBody UserSignupRequest request) {
        userService.createUser(request.getUsername(), request.getPassword());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

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
