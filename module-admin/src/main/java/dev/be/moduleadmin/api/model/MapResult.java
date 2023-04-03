package dev.be.moduleadmin.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class MapResult<T> extends CommonResult {

    /**
     * API 반환값이 Map 형식일 경우 처리
     */

    private Map<T, T> data;

}
