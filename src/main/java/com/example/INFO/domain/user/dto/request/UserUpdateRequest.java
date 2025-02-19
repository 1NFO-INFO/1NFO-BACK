package com.example.INFO.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class UserUpdateRequest {

    @JsonProperty("nickname")
    String nickname;

    @JsonProperty("password")
    String password;
}
