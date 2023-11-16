package com.flat.backend.user.dto.req;

import lombok.Getter;

@Getter
public class ChangeAddressDto {
    private String email;
    private String address;
    private String lat;
    private String lng;
}
