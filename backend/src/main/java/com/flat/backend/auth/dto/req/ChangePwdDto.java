package com.flat.backend.auth.dto.req;

import lombok.Getter;

@Getter
public class ChangePwdDto {

    private String email;
    private String prevPwd;
    private String newPwd;
}
