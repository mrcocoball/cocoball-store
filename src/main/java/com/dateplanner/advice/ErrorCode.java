package com.dateplanner.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(6000, "유저를 찾을 수 없습니다"),
    EMAIL_DUPLICATED(6001, "이미 회원가입된 이메일입니다"),
    USER_ID_DUPLICATED(6002, "이미 회원가입된 ID입니다"),
    PASSWORD_MISMATCH(6003, "비밀번호가 맞지 않습니다"),
    AUTHENTICATION_FAILED(9000, "해당 리소스에 접근하기 위한 권한이 없습니다"),
    ACCESS_DENIED(9001, "해당 리소스에 접근하기 위한 권한이 없습니다"),
    TOKEN_INVALID(9002, "유효하지 않은 토큰입니다"),
    INTERNAL_SERVER_ERROR(10000, "알 수 없는 오류입니다");

    private int code;
    private String description;
}
