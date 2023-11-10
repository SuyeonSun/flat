package com.flat.backend.auth.dto.req;

import lombok.Getter;

@Getter
public class SignUpReqDto {
    private String email;
    private String name;
    private String profile;
    private String address;
    private String addressLat;
    private String addressLng;
    private String password;
    private String phoneNumber;
}
