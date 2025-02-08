package com.example.INFO.domain.user.service;

import com.example.INFO.domain.user.dto.KakaoOAuthTokenDto;
import com.example.INFO.domain.user.dto.KakaoOAuthUserInfoDto;
import com.example.INFO.domain.user.dto.request.KakaoOAuthTokenRequest;
import com.example.INFO.domain.user.dto.response.KakaoOAuthTokenResponse;
import com.example.INFO.domain.user.dto.response.KakaoOAuthUserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Log4j2
public class KakaoOAuthService {

    private static final String AUTHORIZATION_URI_FORMAT = "%s?client_id=%s&redirect_uri=%s&response_type=code";
    private static final String TOKEN_GRANTED_TYPE = "authorization_code";


    @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
    private String authorizationUri;
    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri;
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoUri;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    public String getAuthorizationUri() {
        return String.format(
                AUTHORIZATION_URI_FORMAT,
                authorizationUri, clientId, redirectUri
        );
    }

    public String getAccessToken(String code) {
        return getToken(code).getAccessToken();
    }

    public KakaoOAuthUserInfoDto getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoOAuthUserInfoResponse> response = new RestTemplate().exchange(
                userInfoUri,
                HttpMethod.GET,
                request,
                KakaoOAuthUserInfoResponse.class
        );
        KakaoOAuthUserInfoResponse userInfoResponse = response.getBody();
        return userInfoResponse.toDto();
    }

    private KakaoOAuthTokenDto getToken(String code) {
        KakaoOAuthTokenRequest tokenRequest = KakaoOAuthTokenRequest.builder()
                .grantType(TOKEN_GRANTED_TYPE)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri(redirectUri)
                .code(code)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(tokenRequest.toMultiValueMap(), headers);

        KakaoOAuthTokenResponse tokenResponse = new RestTemplate().exchange(
                tokenUri,
                HttpMethod.POST,
                request,
                KakaoOAuthTokenResponse.class
        ).getBody();

        return tokenResponse.toDto();
    }
}
