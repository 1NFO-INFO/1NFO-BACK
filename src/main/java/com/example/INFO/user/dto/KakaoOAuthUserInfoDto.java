package com.example.INFO.user.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
public class KakaoOAuthUserInfoDto {

    String email;
}
