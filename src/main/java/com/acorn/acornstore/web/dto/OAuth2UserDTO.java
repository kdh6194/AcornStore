package com.acorn.acornstore.web.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class OAuth2UserDTO implements OAuth2User {
    private Long id;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public OAuth2UserDTO(Long id, Map<String, Object> attributes) {
        this.id = id;
        this.attributes = attributes;
        this.authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return String.valueOf(this.id); // name 대신 id를 리턴한다.
    }
}
