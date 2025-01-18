package com.example.INFO.user.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLoginResponse {

    String token;

    public static UserLoginResponse of(String token) {
        return new UserLoginResponse(token);
    }
}
