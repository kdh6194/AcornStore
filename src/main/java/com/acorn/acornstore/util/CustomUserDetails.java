package com.acorn.acornstore.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private String id;
    private String email;

    public CustomUserDetails(String id, String email) {
        this.id = id;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 아무 권한도 부여하지 않습니다.
    }

    @Override
    public String getPassword() {
        return null; // 비밀번호 정보가 없다고 가정합니다.
    }

    @Override
    public String getUsername(){
        return email;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true; // 계정이 만료되지 않았다고 가정합니다.
    }

    @Override
    public boolean isAccountNonLocked(){
        return true; // 계정이 잠기지 않았다고 가정합니다.
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true; // 자격 증명(비밀번호)가 만료되지 않았다고 가정합니다.
    }

    @Override
    public boolean isEnabled(){
        return true; // 계정이 활성화 되었다고 가정합니디.
    }
}