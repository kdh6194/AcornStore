package com.acorn.acornstore.domain;

public enum UserStatus {
    MAINTAIN("회원이시므로 접속이 가능합니다."),
    DELETED("회원이 아니므로 접속이 불가능합니다.");
    private final String message;

    UserStatus(String message) {
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
}