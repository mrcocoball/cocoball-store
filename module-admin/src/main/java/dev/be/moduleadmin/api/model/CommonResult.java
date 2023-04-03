package dev.be.moduleadmin.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {

    /**
     * 전달될 데이터와 별개로 API의 처리 여부, 상태, 메시지가 담긴 데이터
     */

    private boolean success;

    private int code;

    private String msg;

}
