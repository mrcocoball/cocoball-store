package dev.be.moduleadmin.api.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class PageResult<T> extends CommonResult {

    /**
     * API 반환값이 페이지 타입일 경우 처리
     */

    private Page<T> data;

}
