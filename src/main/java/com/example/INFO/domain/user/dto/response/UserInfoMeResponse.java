package com.example.INFO.domain.user.dto.response;

import com.example.INFO.domain.user.dto.UserInfoMeDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class UserInfoMeResponse {

    @JsonProperty("nickname")
    String nickname;
    @JsonProperty("email")
    String email;
    @JsonProperty("auth_type")
    String authType;

    public static UserInfoMeResponse fromDto(UserInfoMeDto dto) {
        return new UserInfoMeResponse(
                dto.getNickname(),
                dto.getEmail(),
                dto.getOAuthProvider() != null ? dto.getOAuthProvider().name() : "LOCAL"
        );
    }
}
