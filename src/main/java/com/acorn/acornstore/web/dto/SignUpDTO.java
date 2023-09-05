package com.acorn.acornstore.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SignUpDTO {
    private String email;
    private String password;
    private String phone;
    private String address;
    private String gender;
    private String about_me;
    private String profileImg;
}
