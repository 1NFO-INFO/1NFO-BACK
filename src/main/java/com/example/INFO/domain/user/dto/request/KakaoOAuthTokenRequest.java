package com.example.INFO.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Value
@Builder
public class KakaoOAuthTokenRequest {

    @JsonProperty("grant_type")
    String grantType;

    @JsonProperty("client_id")
    String clientId;

    @JsonProperty("redirect_uri")
    String redirectUri;

    @JsonProperty("code")
    String code;

    @JsonProperty("client_secret")
    String clientSecret;

    public MultiValueMap<String, String> toMultiValueMap() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", grantType);
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("client_secret", clientSecret);
        return body;
    }
}
