package com.example.INFO.user.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    @JsonProperty("secret-key")
    private String secretKey;

    @JsonProperty("access-token")
    private JwtAccessTokenProperties accessToken;

    @JsonProperty("refresh-token")
    private JwtRefreshTokenProperties refreshToken;

    public long getAccessTokenExpiredTimeSec() {
        return accessToken.expiredTimeSec;
    }

    public long getRefreshTokenExpiredTimeSec() {
        return refreshToken.expiredTimeSec;
    }

    @Data
    public static class JwtAccessTokenProperties {

        @JsonProperty("expired-time-sec")
        private long expiredTimeSec;
    }

    @Data
    public static class JwtRefreshTokenProperties {

        @JsonProperty("expired-time-sec")
        private long expiredTimeSec;
    }
}
