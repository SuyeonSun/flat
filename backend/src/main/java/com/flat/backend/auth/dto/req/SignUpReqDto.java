package com.flat.backend.auth.dto.req;

// import lombok.Data;
import lombok.Getter;

@Getter
public class SignUpReqDto {
    private String email;
    private String password;
}
