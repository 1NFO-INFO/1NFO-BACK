package com.example.INFO.domain.auth.dto.request;

import lombok.Value;

@Value
public class UserLoginRequest {

    String username;
    String password;
}
