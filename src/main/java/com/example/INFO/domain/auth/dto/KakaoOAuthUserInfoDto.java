package com.example.INFO.domain.auth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoOAuthUserInfoDto {

    String email;
    String phoneNumber;

    public static KakaoOAuthUserInfoDto of(String email, String phoneNumber) {
        return new KakaoOAuthUserInfoDto(email, phoneNumber);
    }
}
