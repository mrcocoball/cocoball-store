package com.dateplanner.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {

    /**
     * 전달될 데이터와 별개로 API의 처리 여부, 상태, 메시지가 담긴 데이터
     */

    // springfox의 SWAGGER-UI @ApiProperty
    @Schema(description = "응답 성공 여부, True / False")
    private boolean success;

    @Schema(description = "응답 코드, >=0 : 정상, <0 : 비정상")
    private int code;

    @Schema(description = "응답 메시지")
    private String msg;

}
