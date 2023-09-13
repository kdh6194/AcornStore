package com.acorn.acornstore.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignUpDTO {
    private String email;
    private String password;
    private String phone;
    private String address;
    private String gender;
    private String about_me;
    @Nullable
    private List<MultipartFile> profileImg;
}
