package com.example.INFO.user.controller;

import com.example.INFO.user.configuration.SecurityConfig;
import com.example.INFO.user.dto.request.UserSignupRequest;
import com.example.INFO.user.exception.UserException;
import com.example.INFO.user.exception.UserExceptionType;
import com.example.INFO.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@Import(SecurityConfig.class)
public class UserControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

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
}
