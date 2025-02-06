package com.example.INFO.user.controller;

import com.example.INFO.user.dto.JwtTokenDto;
import com.example.INFO.user.dto.KakaoOAuthUserInfoDto;
import com.example.INFO.user.service.KakaoOAuthService;
import com.example.INFO.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class KakaoOAuthControllerTest {

    @MockitoBean
    private KakaoOAuthService kakaoOAuthService;

    @MockitoBean
    private UserService userService;

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    void 카카오_OAuth_인증_요청() throws Exception {
        String authorizationUri = "authorization-uri";
        given(kakaoOAuthService.getAuthorizationUri()).willReturn(authorizationUri);

        mockMvc.perform(get("/oauth/kakao/authorize"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(authorizationUri));
    }

    @Test
    void 카카오_OAuth_callback() throws Exception {
        String accessToken = "test_access_token";
        KakaoOAuthUserInfoDto userInfo = KakaoOAuthUserInfoDto.of("email");
        JwtTokenDto jwtTokenDto = JwtTokenDto.of("access-token", "refresh-token");

        given(kakaoOAuthService.getAccessToken(any())).willReturn(accessToken);
        given(kakaoOAuthService.getUserInfo(accessToken)).willReturn(userInfo);
        doNothing().when(userService).tryCreateUser(userInfo);
        given(userService.login(userInfo)).willReturn(jwtTokenDto);

        mockMvc.perform(
                get("/oauth/kakao/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("code", "code")
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 카카오_OAuth_callback_실패() throws Exception {
        String accessToken = "test_access_token";
        KakaoOAuthUserInfoDto userInfo = KakaoOAuthUserInfoDto.of("email");
        JwtTokenDto jwtTokenDto = JwtTokenDto.of("access-token", "refresh-token");

        given(kakaoOAuthService.getAccessToken(any())).willReturn(accessToken);
        given(kakaoOAuthService.getUserInfo(accessToken)).willReturn(userInfo);
        doNothing().when(userService).tryCreateUser(userInfo);
        given(userService.login(userInfo)).willReturn(jwtTokenDto);

        mockMvc.perform(
                get("/oauth/kakao/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("error", "error")
                        .param("error_description", "error_description")
                ).andDo(print())
                .andExpect(status().isForbidden());
    }
}
