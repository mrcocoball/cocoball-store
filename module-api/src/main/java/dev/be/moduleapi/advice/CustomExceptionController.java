package dev.be.moduleapi.advice;

import dev.be.moduleapi.advice.exception.CustomAuthenticationEntrypointException;
import dev.be.moduleapi.api.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "CONTROLLER")
@RestController
@RequestMapping("/exception")
public class CustomExceptionController {

    /**
     * Spring Security 예외 관련, AuthenticationEntryPoint를 통해 리다이렉트된 경우 고의로 예외 발생시킴
     */

    @GetMapping("/entrypoint")
    public CommonResult entrypointException() {
        throw new CustomAuthenticationEntrypointException();
    }

    @GetMapping("/accessDenied")
    public CommonResult accessDeniedException() {
        throw new AccessDeniedException("");
    }

}
