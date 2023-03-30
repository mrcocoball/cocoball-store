package dev.be.moduleapi.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class MapResult<T> extends CommonResult {

    /**
     * API 반환값이 Map 형식일 경우 처리
     */

    @Schema(description = "맵 형식 데이터")
    private Map<T, T> data;

}
