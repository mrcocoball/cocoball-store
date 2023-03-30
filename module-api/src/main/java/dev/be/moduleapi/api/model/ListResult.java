package dev.be.moduleapi.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListResult<T> extends CommonResult {

    /**
     * API 반환값이 여러 개의 객체일 경우 처리
     */

    @Schema(description = "리스트 형식 데이터")
    private List<T> data;

}
