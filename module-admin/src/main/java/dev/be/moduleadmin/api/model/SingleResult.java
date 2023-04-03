package dev.be.moduleadmin.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResult<T> extends CommonResult {

    /**
     * 단일 응답 모델, API 반환값이 단일 객체일 경우 해당 모델로 처리, 엔티티가 아닌 다른 엔티티에도 적용 가능
     */

    private T data;

}
