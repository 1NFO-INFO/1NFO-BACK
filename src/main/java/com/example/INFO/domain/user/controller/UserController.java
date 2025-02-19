package com.example.INFO.domain.user.controller;

import com.example.INFO.domain.user.dto.request.UserSignupRequest;
import com.example.INFO.domain.user.dto.response.UserInfoMeResponse;
import com.example.INFO.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> signup(@RequestBody UserSignupRequest request) {
        userService.createUser(request.getUsername(), request.getPassword());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/info/me")
    public ResponseEntity<UserInfoMeResponse> getUserInfoMe() {
        return ResponseEntity.ok(UserInfoMeResponse.fromDto(userService.getUserInfo()));
    }

    @PatchMapping("/nickname")
    public ResponseEntity<Void> initNickname(@RequestBody String nickname) {
        userService.updateNickname(nickname);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
