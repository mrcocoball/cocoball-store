package com.dateplanner.advice;

import com.dateplanner.advice.exception.*;
import com.dateplanner.api.model.CommonResult;
import com.dateplanner.api.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@RequiredArgsConstructor
public class CustomAdvice {

    private final ResponseService responseService;

    @ExceptionHandler(CustomAuthenticationEntrypointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected CommonResult customAuthenticationEntrypointException(HttpServletRequest request, CustomAuthenticationEntrypointException e) {
        return responseService.getFailResult
                (ErrorCode.AUTHENTICATION_FAILED.getCode(), ErrorCode.AUTHENTICATION_FAILED.getDescription());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected CommonResult accessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        return responseService.getFailResult
                (ErrorCode.ACCESS_DENIED.getCode(), ErrorCode.ACCESS_DENIED.getDescription());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected CommonResult usernameNotFoundException(HttpServletRequest request, UsernameNotFoundException e) {
        return responseService.getFailResult
                (ErrorCode.USER_NOT_FOUND.getCode(), ErrorCode.USER_NOT_FOUND.getDescription());
    }

    @ExceptionHandler(UserNotFoundApiException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected CommonResult userNotFoundApiException(HttpServletRequest request, UserNotFoundApiException e) {
        return responseService.getFailResult
                (ErrorCode.USER_NOT_FOUND.getCode(), ErrorCode.USER_NOT_FOUND.getDescription());
    }

    @ExceptionHandler(UserIdDuplicateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult userIdDuplicateException(HttpServletRequest request, UserIdDuplicateException e) {
        return responseService.getFailResult
                (ErrorCode.USER_ID_DUPLICATED.getCode(), ErrorCode.USER_ID_DUPLICATED.getDescription());
    }

    @ExceptionHandler(EmailDuplicateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult emailDuplicateException(HttpServletRequest request, EmailDuplicateException e) {
        return responseService.getFailResult
                (ErrorCode.EMAIL_DUPLICATED.getCode(), ErrorCode.EMAIL_DUPLICATED.getDescription());
    }

    @ExceptionHandler(PasswordMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult passwordMismatchException(HttpServletRequest request, PasswordMismatchException e) {
        return responseService.getFailResult
                (ErrorCode.PASSWORD_MISMATCH.getCode(), ErrorCode.PASSWORD_MISMATCH.getDescription());
    }

    @ExceptionHandler(CustomRefreshTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected CommonResult customRefreshTokenException(HttpServletRequest request, CustomRefreshTokenException e) {
        return responseService.getFailResult
                (ErrorCode.TOKEN_INVALID.getCode(), ErrorCode.TOKEN_INVALID.getDescription());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult Exception(HttpServletRequest request, Exception e) {
        return responseService.getFailResult
                (ErrorCode.INTERNAL_SERVER_ERROR.getCode(), ErrorCode.INTERNAL_SERVER_ERROR.getDescription());
    }

}
