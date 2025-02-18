package com.example.INFO.domain.auth.dto.response;

import com.example.INFO.domain.auth.dto.KakaoOAuthTokenDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class KakaoOAuthTokenResponse {

    @JsonProperty("token_type")
    String tokenType;

    @JsonProperty("access_token")
    String accessToken;

    @JsonProperty("expires_in")
    Integer expiresIn;

    @JsonProperty("refresh_token")
    String refreshToken;

    @JsonProperty("refresh_token_expires_in")
    Integer refreshTokenExpiresIn;

    public KakaoOAuthTokenDto toDto() {
        return KakaoOAuthTokenDto.of(accessToken, refreshToken);
    }
}
