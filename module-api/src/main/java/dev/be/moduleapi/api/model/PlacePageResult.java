package dev.be.moduleapi.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class PlacePageResult<T> extends CommonResult {

    /**
     * API 반환값이 장소 정보 페이지 타입일 경우 처리
     */

    @Schema(description = "x축 좌표")
    private double x;

    @Schema(description = "y축 좌표")
    private double y;

    @Schema(description = "페이지 형식 데이터")
    private Page<T> data;

}
