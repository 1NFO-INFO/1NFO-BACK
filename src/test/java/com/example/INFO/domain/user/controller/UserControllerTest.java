package com.example.INFO.domain.user.controller;

import com.example.INFO.domain.auth.dto.JwtTokenDto;
import com.example.INFO.domain.auth.dto.request.UserLoginRequest;
import com.example.INFO.domain.auth.dto.request.UserRefreshRequest;
import com.example.INFO.domain.auth.service.UserAuthService;
import com.example.INFO.domain.user.dto.request.UserSignupRequest;
import com.example.INFO.domain.user.exception.UserException;
import com.example.INFO.domain.user.exception.UserExceptionType;
import com.example.INFO.domain.user.service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
public class UserControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserAuthService userAuthService;

    @Test
    public void 회원가입() throws Exception {
        String username = "username";
        String password = "password";

        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserSignupRequest(username, password)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 회원가입시_이미_회원가입된_username으로_회원가입을_하는경우_Conflict를_반환한다() throws Exception {
        String username = "username";
        String password = "password";

        doThrow(new UserException(UserExceptionType.DUPLICATED_USERNAME))
                .when(userService).createUser(username, password);

        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserSignupRequest(username, password)))
                ).andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void 로그인() throws Exception {
        String username = "username";
        String password = "password";

        when(userAuthService.login(username, password))
                .thenReturn(mock(JwtTokenDto.class));

        mockMvc.perform(
                post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(username, password)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 로그인시_회원가입이_안된_username을_입력할경우_NotFound를_반환한다() throws Exception {
        String username = "username";
        String password = "password";

        doThrow(new UserException(UserExceptionType.USER_NOT_FOUND))
                .when(userAuthService).login(username, password);

        mockMvc.perform(
                post("/users/login")
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
                .thenThrow(new UserException(UserExceptionType.INVALID_PASSWORD));

        mockMvc.perform(
                post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(username, password)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void 리프레쉬() throws Exception {
        String accessToken = "access-token";
        String refreshToken = "refresh-token";

        when(userAuthService.refresh(refreshToken))
                .thenReturn(JwtTokenDto.of(accessToken, refreshToken));

        mockMvc.perform(
                post("/users/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserRefreshRequest(refreshToken)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 리프레쉬_실패시_Unauthorized를_반환한다() throws Exception {
        String refreshToken = "refresh-token";

        when(userAuthService.refresh(refreshToken))
                .thenThrow(new UserException(UserExceptionType.INVALID_REFRESH_TOKEN));

        mockMvc.perform(
                post("/users/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserRefreshRequest(refreshToken)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
