package com.acorn.acornstore.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

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

        public String getId(){
            return id;
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

//public class CustomUserDetails implements UserDetails {
//    private String id;
//    private String email;
//    private String password; // 비밀번호 필드 추가
//    private List<String> recentSearches; // 최근 검색어
//    private List<GrantedAuthority> authorities; // 권한 목록 필드 추가
//    private boolean accountNonExpired; // 계정 만료 상태 필드 추가
//    private boolean accountNonLocked; // 계정 잠김 상태 필드 추가
//    private boolean credentialsNonExpired; // 자격 증명 만료 상태 필드 추가
//
//    public CustomUserDetails(String id, String email, String password) {
//        this.id = id;
//        this.email = email;
//        this.password = password;
//        this.recentSearches = new ArrayList<>();
//        this.authorities = new ArrayList<>();
//        this.accountNonExpired = true;
//        this.accountNonLocked = true;
//        this.credentialsNonExpired = true;
//
//        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));  // 기본적으로 ROLE_USER 권한 부여
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities.;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername(){
//        return email;
//    }
//
//    public void addRecentSearch(String search) {
//        recentSearches.add(search);
//    }
//
//    @Override
//    public boolean isAccountNonExpired(){
//        return accountNonExpired;
//    }
//
//    @Override
//    public boolean isAccountNonLocked(){
//        return accountNonLocked;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired(){
//        return credentialsNonExpired;
//    }
//
//    @Override
//    public boolean isEnabled(){
//        return true ;
//    }
//}