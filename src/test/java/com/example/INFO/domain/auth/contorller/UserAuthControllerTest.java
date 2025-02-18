package com.example.INFO.domain.auth.contorller;

import com.example.INFO.domain.auth.dto.JwtTokenDto;
import com.example.INFO.domain.auth.dto.request.UserLoginRequest;
import com.example.INFO.domain.auth.dto.request.UserRefreshRequest;
import com.example.INFO.domain.auth.service.UserAuthService;
import com.example.INFO.domain.user.service.UserService;
import com.example.INFO.global.exception.DefaultException;
import com.example.INFO.global.payload.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
public class UserAuthControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserAuthService userAuthService;

    @Test
    public void 로그인() throws Exception {
        String username = "username";
        String password = "password";

        when(userAuthService.login(username, password))
                .thenReturn(mock(JwtTokenDto.class));

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(username, password)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 로그인시_회원가입이_안된_username을_입력할경우_NotFound를_반환한다() throws Exception {
        String username = "username";
        String password = "password";

        doThrow(new DefaultException(ErrorCode.NOT_FOUND))
                .when(userAuthService).login(username, password);

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(username, password)))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void 로그인시_틀린_password를_입력할경우_Unauthorized를_반환한다() throws Exception {
        String username = "username";
        String password = "password";

        when(userAuthService.login(username, password))
                .thenThrow(new DefaultException(ErrorCode.INVALID_PASSWORD));

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(username, password)))
                ).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void 리프레쉬() throws Exception {
        String accessToken = "access-token";
        String refreshToken = "refresh-token";

        when(userAuthService.refresh(refreshToken))
                .thenReturn(JwtTokenDto.of(accessToken, refreshToken));

        mockMvc.perform(
                        post("/api/v1/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new UserRefreshRequest(refreshToken)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 리프레쉬_실패시_Unauthorized를_반환한다() throws Exception {
        String refreshToken = "refresh-token";

        when(userAuthService.refresh(refreshToken))
                .thenThrow(new DefaultException(ErrorCode.INVALID_AUTHENTICATION));

        mockMvc.perform(
                        post("/api/v1/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new UserRefreshRequest(refreshToken)))
                ).andDo(print())
                .andExpect(status().is4xxClientError());
    }
}
