package com.example.INFO.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class UserRefreshRequest {

    @JsonProperty("refresh-token")
    String refreshToken;
}
