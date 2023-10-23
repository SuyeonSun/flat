package com.flat.backend.auth.dto;

import lombok.Builder;
// import lombok.Data;
import lombok.Getter;

@Getter
public class SignUpDto {
    private String email;
    private String password;
}
