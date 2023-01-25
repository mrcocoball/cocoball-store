package com.dateplanner.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonResponse {

    /**
     * 공통 응답 모델을 success / fail로 처리
     */

    SUCCESS(0, "success"),
    FAIL(-1, "fail");

    private int code;
    private String msg;

}
