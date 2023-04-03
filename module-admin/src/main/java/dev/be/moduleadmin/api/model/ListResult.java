package dev.be.moduleadmin.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListResult<T> extends CommonResult {

    /**
     * API 반환값이 여러 개의 객체일 경우 처리
     */

    private List<T> data;

}
