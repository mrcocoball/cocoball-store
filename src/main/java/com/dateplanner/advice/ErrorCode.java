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
    USER_ALREADY_EXISTS(6014, "이미 존재하는 회원입니다"),
    VALIDATION_ERROR(6015, "입력된 데이터에 오류가 존재합니다"),

    // DB 관련
    DATA_ACCESS_ERROR(7000, "잘못된 요청입니다"), // DataAccessException, 다양한 Data Access 관련 (JDBC, Hibernate, JPA 등) 스프링 예외
    DB_CONNECTION_OUT(7001, "잘못된 요청입니다"), // DataAccessResourceFailureException
    DATA_INTEGRITY_VIOLATION(7002, "잘못된 요청입니다"), // DataIntegrityViolationException
    SQL_GRAMMAR_ERROR(7003, "잘못된 요청입니다"), // BadSqlGrammarException
    DB_KEY_DUPLICATE(7004, "잘못된 요청입니다"), // DuplicateKeyException

    // 인증 관련
    AUTHENTICATION_FAILED(9000, "해당 리소스에 접근하기 위한 권한이 없습니다"),
    ACCESS_DENIED(9001, "해당 리소스에 접근하기 위한 권한이 없습니다"),
    REFRESH_TOKEN_INVALID(9002, "유효하지 않거나 만료된 리프레시 토큰입니다"),
    OAUTH_FAILED(9003, "소셜 로그인에 실패하였습니다"),
    OAUTH_NOT_AGREED(9004, "이메일 제공 동의를 받지 못하였습니다"),
    TOKEN_EXPIRED(9002, "만료된 토큰입니다"),

    // 서버 내부 오류
    INTERNAL_SERVER_ERROR(10000, "알 수 없는 오류입니다");

    private int code;
    private String description;

}
