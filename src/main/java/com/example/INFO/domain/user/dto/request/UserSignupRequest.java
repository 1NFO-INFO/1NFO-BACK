package com.example.INFO.domain.user.dto.request;

import com.example.INFO.domain.user.dto.LocalUserCreateDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Value;

@Value
public class UserSignupRequest {

    @JsonProperty("username")
    String username;

    @JsonProperty("password")
    String password;

    @JsonProperty("phone_number")
    @Pattern(regexp = "^\\d{11}$")
    String phoneNumber;

    public LocalUserCreateDto toLocalUserCreateDto() {
        return LocalUserCreateDto.of(
                username,
                password,
                phoneNumber
        );
    }
}
