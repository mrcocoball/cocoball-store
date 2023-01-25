package com.dateplanner.user.controller;

import com.dateplanner.api.model.SingleResult;
import com.dateplanner.api.service.ResponseService;
import com.dateplanner.security.dto.TokenDto;
import com.dateplanner.security.dto.TokenRequestDto;
import com.dateplanner.security.jwt.JwtProvider;
import com.dateplanner.user.dto.UserJoinRequestDto;
import com.dateplanner.user.dto.UserLoginRequestDto;
import com.dateplanner.user.dto.UserLoginResponseDto;
import com.dateplanner.user.service.UserJoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "UserJoinApiController")
@RequiredArgsConstructor
@RestController
public class UserJoinApiController {

    private final UserJoinService userJoinService;
    private final ResponseService responseService;


    @Operation(summary = "로그인 요청", description = "[GET] 로그인 요청")
    @PostMapping("/api/v1/login")
    public SingleResult<TokenDto> login(
            @Parameter(description = "로그인 요청 정보", required = true) @RequestBody UserLoginRequestDto dto) {

        TokenDto tokenDto = userJoinService.login(dto);

        return responseService.getSingleResult(tokenDto);

    }

    @Operation(summary = "회원가입 요청", description = "[POST] 회원가입 요청")
    @PostMapping("/api/v1/join")
    public SingleResult<String> join(
            @Parameter(description = "회원가입 요청 정보") @Valid @RequestBody UserJoinRequestDto dto) {

        String joinedUserId = userJoinService.join(dto);

        return responseService.getSingleResult(joinedUserId);

    }

    @Operation(summary = "액세스, 리프레시 토큰 재발급", description = "액세스 토근 만료 시 회원 검증 후 리프레쉬 토큰 검증한 후 액세스 / 리프레시 토크 재발급")
    @PostMapping("/api/v1/refresh")
    public SingleResult<TokenDto> refresh(
            @Parameter(description = "토큰 재발급 요청", required = true) @RequestBody TokenRequestDto dto) {
        return responseService.getSingleResult(userJoinService.refresh(dto));
    }

}
