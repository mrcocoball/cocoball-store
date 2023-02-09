package com.dateplanner.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class PageResult<T> extends CommonResult {

    /**
     * API 반환값이 페이지 타입일 경우 처리
     */

    @Schema(description = "페이지 형식 데이터")
    private Page<T> data;

}
