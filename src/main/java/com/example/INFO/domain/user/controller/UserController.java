package com.example.INFO.domain.user.controller;

import com.example.INFO.domain.user.dto.UserUpdateDto;
import com.example.INFO.domain.user.dto.request.UserSignupRequest;
import com.example.INFO.domain.user.dto.request.UserUpdateRequest;
import com.example.INFO.domain.user.dto.response.UserInfoMeResponse;
import com.example.INFO.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "회원 API", description = "회원 관련 API")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "회원가입",
            description = "회원가입합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원가입 성공"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Void> signup(@RequestBody UserSignupRequest request) {
        userService.createUser(request.getUsername(), request.getPassword());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "내 정보 조회",
            description = "나의 정보를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "나의 정보 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserInfoMeResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/info/me")
    public ResponseEntity<UserInfoMeResponse> getUserInfoMe() {
        return ResponseEntity.ok(UserInfoMeResponse.fromDto(userService.getUserInfo()));
    }

    @Operation(
            summary = "닉네임 초기화",
            description = "닉네임을 초기화합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "닉네임 업데이트 성공"
                    )
            }
    )
    @PatchMapping("/nickname")
    public ResponseEntity<Void> initNickname(@RequestBody String nickname) {
        userService.updateNickname(nickname);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "회원 정보 수정",
            description = "회원 정보를 수정합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원 정보 수정 성공"
                    )
            }
    )
    @PatchMapping("/info/me")
    public ResponseEntity<Void> updateUserInfo(
            @RequestBody UserUpdateRequest request,
            @RequestParam(value = "is_local_user") boolean isLocalUser
    ) {
        userService.updateUserInfo(UserUpdateDto.fromRequest(request), isLocalUser);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
