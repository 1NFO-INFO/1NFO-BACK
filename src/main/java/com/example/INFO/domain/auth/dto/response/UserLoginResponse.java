package com.example.INFO.domain.auth.dto.response;

import com.example.INFO.domain.auth.dto.JwtTokenDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLoginResponse {

    @JsonProperty("access_token")
    String accessToken;

    @JsonProperty("refresh_token")
    String refreshToken;

    public static UserLoginResponse from(JwtTokenDto jwtTokenDto) {
        return new UserLoginResponse(jwtTokenDto.getAccessToken(), jwtTokenDto.getRefreshToken());
    }
}
