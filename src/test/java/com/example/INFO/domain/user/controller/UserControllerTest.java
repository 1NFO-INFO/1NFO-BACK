package com.example.INFO.domain.user.controller;

import com.example.INFO.domain.auth.service.UserAuthService;
import com.example.INFO.domain.user.dto.LocalUserCreateDto;
import com.example.INFO.domain.user.dto.UserInfoMeDto;
import com.example.INFO.domain.user.dto.request.UserSignupRequest;
import com.example.INFO.domain.user.dto.request.UserUpdateRequest;
import com.example.INFO.domain.user.service.UserService;
import com.example.INFO.global.exception.CustomException;
import com.example.INFO.global.exception.DefaultException;
import com.example.INFO.global.payload.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        String phoneNumber = "01012345678";

        mockMvc.perform(
                post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserSignupRequest(username, password, phoneNumber)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 회원가입시_이미_회원가입된_username으로_회원가입을_하는경우_Conflict를_반환한다() throws Exception {
        String username = "username";
        String password = "password";
        String phoneNumber = "01012345678";
        LocalUserCreateDto localUserCreateDto = LocalUserCreateDto.of(username, password, phoneNumber);

        doThrow(new DefaultException(ErrorCode.DUPLICATE_ERROR))
                .when(userService).createUser(localUserCreateDto);

        mockMvc.perform(
                post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserSignupRequest(username, password, phoneNumber)))
                ).andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void 회원가입시_잘못된_전화번호_형식으로_회원가입을_하는경우_BadRequest를_반환한다() throws Exception {
        String username = "username";
        String password = "password";
        String phoneNumber = "010-5290-7914";

        mockMvc.perform(
                        post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new UserSignupRequest(username, password, phoneNumber)))
                ).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 나의_회원정보_가져오기_성공() throws Exception {
        given(userService.getUserInfo()).willReturn(mock(UserInfoMeDto.class));

        mockMvc.perform(
                get("/api/v1/users/info/me")
                ).andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void 나의_회원정보_가져오기_실패_인증이_없는_접근() throws Exception {
        willThrow(new CustomException(ErrorCode.UNAUTHORIZED))
                .given(userService).getUserInfo();

        mockMvc.perform(
                    get("/api/v1/users/info/me")
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void 나의_회원정보_가져오기_실패_없는_회원() throws Exception {
        willThrow(new CustomException(ErrorCode.NOT_FOUND))
                .given(userService).getUserInfo();

        mockMvc.perform(
                    get("/api/v1/users/info/me")
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void 닉네임_초기화_성공() throws Exception {
        given(userService.getUserInfo()).willReturn(mock(UserInfoMeDto.class));

        mockMvc.perform(
                        get("/api/v1/users/info/me")
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 닉네임_초기화_실패_인증이_없는_접근() throws Exception {
        willThrow(new CustomException(ErrorCode.UNAUTHORIZED))
                .given(userService).getUserInfo();

        mockMvc.perform(
                        get("/api/v1/users/info/me")
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void 닉네임_초기화_실패_없는_회원() throws Exception {
        willThrow(new CustomException(ErrorCode.UNAUTHORIZED))
                .given(userService).getUserInfo();

        mockMvc.perform(
                        get("/api/v1/users/info/me")
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void 회원정보_업데이트_성공() throws Exception {
        willDoNothing().given(userService).updateUserInfo(any(), anyBoolean());

        mockMvc.perform(
                patch("/api/v1/users/info/me")
                        .queryParam("is_local_user", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserUpdateRequest("nickname", "password", "010-1234-5678")))
                ).andDo(print())
                .andExpect(status().isOk());
    }
}
