package com.example.INFO.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class KakaoOAuthCallbackRequest {

    @JsonProperty("code")
    String code;

    @JsonProperty("error")
    String error;

    @JsonProperty("error_description")
    String errorDescription;
}
