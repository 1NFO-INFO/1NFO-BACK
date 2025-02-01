package com.example.INFO.user.dto.response;

import com.example.INFO.user.dto.JwtTokenDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRefreshResponse {

    @JsonProperty("access_token")
    String accessToken;

    @JsonProperty("refresh_token")
    String refreshToken;

    public static UserRefreshResponse from(JwtTokenDto jwtTokenDto) {
        return new UserRefreshResponse(jwtTokenDto.getAccessToken(), jwtTokenDto.getRefreshToken());
    }
}
