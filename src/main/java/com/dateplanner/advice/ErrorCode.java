package com.dateplanner.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 비즈니스 로직 관련
    USER_NOT_FOUND(6000, "유저를 찾을 수 없습니다"),
    EMAIL_DUPLICATED(6001, "이미 회원가입된 이메일입니다"),
    USER_NICKNAME_DUPLICATED(6002, "중복된 닉네임입니다"),
    PASSWORD_MISMATCH(6003, "비밀번호가 맞지 않습니다"),
    PLACE_NOT_FOUND(6004, "장소 정보를 찾을 수 없습니다"),
    BOOKMARK_NOT_FOUND(6005, "북마크 정보를 찾을 수 없습니다"),
    BOOKMARK_DUPLICATED(6006, "이미 북마크가 저장되어 있습니다"),
    REVIEW_NOT_FOUND(6007, "리뷰를 찾을 수 없습니다"),
    PLAN_NOT_FOUND(6008, "플랜 정보를 찾을 수 없습니다"),
    DETAIL_PLAN_NOT_FOUND(6009, "세부 플랜 정보를 찾을 수 없습니다"),
    REQUEST_FAILED(6010, "REST API 호출에 실패하였습니다"),
    SEARCH_RESULT_NOT_FOUND(6011, "검색 결과를 찾을 수 없습니다"),
    ADDRESS_INVALID(6012, "주소가 입력되지 않았거나 잘못된 입력 주소입니다"),
    CATEGORY_INVALID(6013, "유효하지 않은 카테고리입니다"),

    // 인증 관련
    AUTHENTICATION_FAILED(9000, "해당 리소스에 접근하기 위한 권한이 없습니다"),
    ACCESS_DENIED(9001, "해당 리소스에 접근하기 위한 권한이 없습니다"),
    TOKEN_INVALID(9002, "유효하지 않은 토큰입니다"),

    // 서버 내부 오류
    INTERNAL_SERVER_ERROR(10000, "알 수 없는 오류입니다");

    private int code;
    private String description;
}
