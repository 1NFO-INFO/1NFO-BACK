package com.example.INFO.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
public class KakaoOAuthUserInfoDto {

    String email;
}
