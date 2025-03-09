package com.example.INFO.domain.user.dto;

import com.example.INFO.domain.user.dto.request.UserUpdateRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
public class UserUpdateDto {

    String nickname;
    String password;
    String phoneNumber;

    public static UserUpdateDto fromRequest(UserUpdateRequest request) {
        return UserUpdateDto.of(
                request.getNickname(),
                request.getPassword(),
                request.getPhoneNumber()
        );
    }
}
