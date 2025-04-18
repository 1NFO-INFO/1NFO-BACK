package com.example.INFO.domain.user.exception;

import com.example.INFO.global.payload.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException
    ) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
