package com.example.INFO.user.dto.request;

import lombok.Value;

@Value
public class UserLoginRequest {

    String username;
    String password;
}
