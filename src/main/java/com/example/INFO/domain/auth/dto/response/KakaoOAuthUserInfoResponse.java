package com.example.INFO.domain.auth.dto.response;

import com.example.INFO.domain.auth.dto.KakaoOAuthUserInfoDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class KakaoOAuthUserInfoResponse {

    @JsonProperty("id")
    Long id;

    @JsonProperty("kakao_account")
    KakaoAccount kakaoAccount;

    public KakaoOAuthUserInfoDto toDto() {
        return KakaoOAuthUserInfoDto.of(kakaoAccount.getEmail());
    }

    @Value
    private static class KakaoAccount {
        String email;
    }
}
