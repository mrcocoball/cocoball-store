package com.dateplanner.api.service;

import com.dateplanner.api.model.CommonResult;
import com.dateplanner.api.model.ListResult;
import com.dateplanner.api.model.PageResult;
import com.dateplanner.api.model.SingleResult;
import com.dateplanner.api.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j(topic = "SERVICE")
@Service
public class ResponseService {

    /**
     * API가 반환한 모델을 처리, 결과 모델에 데이터를 넣어줌
     */


    /**
     * 단일 데이터 결과 처리 메소드
     */
    public <T> SingleResult<T> getSingleResult(T data) {

        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;

    }

    /**
     * 복수 데이터 결과 처리 메소드
     */
    public <T>ListResult<T> getListResult(List<T> list) {

        ListResult<T> result = new ListResult<>();
        result.setData(list);
        setSuccessResult(result);
        return result;

    }

    /**
     * 페이지 결과 처리 메소드
     */
    public <T> PageResult<T> getPageResult(Page<T> page) {

        PageResult<T> result = new PageResult<>();
        result.setData(page);
        setSuccessResult(result);
        return result;
    }

    /**
     * 성공 결과만 처리 (단순히 성공 / 실패 여부 응답용)
     */
    public CommonResult getSuccessResult() {

        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;

    }

    /**
     * 실패 결과만 처리 (단순히 성공 / 실패 여부 응답용)
     */
    public CommonResult getFailResult() {

        CommonResult result = new CommonResult();
        setFailResult(result);
        return result;

    }

    /**
     * 실패 결과 처리 (코드 및 메시지 전달)
     */
    public CommonResult getFailResult(int code, String msg) {

        CommonResult result = new CommonResult();
        result.setSuccess(false);
        setFailResult(result, code, msg);
        return result;

    }


    /**
     * API 요청 성공 시 응답 모델을 성공 데이터로 세팅
     */
    private void setSuccessResult(CommonResult result) {

        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());

    }

    /**
     * API 요청 실패 시 응답 모델을 실패 데이터로 세팅
     */
    private void setFailResult(CommonResult result) {

        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());

    }

    /**
     * API 요청 실패 시 응답 모델을 실패 데이터로 세팅 (코드, 메시지 추가)
     */
    private void setFailResult(CommonResult result, int code, String msg) {

        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);

    }

}
