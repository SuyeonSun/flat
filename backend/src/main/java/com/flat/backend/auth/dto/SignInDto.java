package com.flat.backend.auth.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class SignInDto {
    private String email;
    private String password;

    @Override
    public String toString() {
        return "SignInDto{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
