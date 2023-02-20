package com.dateplanner.admin.user.controller;

import com.dateplanner.admin.user.dto.UserLoginRequestDto;
import com.dateplanner.advice.ErrorCode;
import com.dateplanner.api.model.CommonResult;
import com.dateplanner.api.model.SingleResult;
import com.dateplanner.api.service.ResponseService;
import com.dateplanner.security.dto.TokenDto;
import com.dateplanner.security.dto.TokenRequestDto;
import com.dateplanner.admin.user.dto.UserJoinRequestDto;
import com.dateplanner.admin.user.service.UserJoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "0. [로그인, 회원가입 화면] UserJoinApiController - 회원가입, 로그인, 토큰 발급 API")
@RequiredArgsConstructor
@RestController
public class UserJoinApiController {

    private final UserJoinService userJoinService;
    private final ResponseService responseService;


    @Operation(summary = "[POST] 로그인 요청",
            description = "이메일, 비밀번호를 입력하여 로그인 요청을 합니다. <br>" +
                    "로그인이 완료되면 액세스 토큰, 리프레시 토큰이 발급이 됩니다. 본 API 문서에서 인증이 필요한 API를 테스트하기 위해서는 <br>" +
                    "본 로그인 요청을 통해 전달 받은 액세스 토큰을 사용해야 합니다.")
    @PostMapping("/api/v1/login")
    public SingleResult<TokenDto> login(
            @Parameter(description = "로그인 요청 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserLoginRequestDto.class))) @RequestBody UserLoginRequestDto dto) {

        TokenDto tokenDto = userJoinService.login(dto);

        return responseService.getSingleResult(tokenDto);

    }

    @Operation(summary = "[POST] 회원가입 요청",
            description = "이메일, 비밀번호, 닉네임을 입력하여 회원가입 요청을 합니다. <br>" +
                    "아이디, 이메일은 통합이 될 가능성이 있으며, 자기소개는 닉네임으로 변경될 여지가 있습니다. <br>" +
                    "이메일 중복 체크의 경우 회원가입 요청을 받은 상태에서 서버에서 DB를 조회하여 결과를 반환하고 있으나, <br>" +
                    "필요하다면 중복 체크용 API를 별도로 만들어서 클라이언트 측에서 바로 중복 체크를 하게 할 수도 있습니다.")
    @PostMapping("/api/v1/join")
    public SingleResult<String> join(
            @Parameter(description = "회원가입 요청 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserJoinRequestDto.class))) @Valid @RequestBody UserJoinRequestDto dto) {

        String joinedUserId = userJoinService.join(dto);

        return responseService.getSingleResult(joinedUserId);

    }

    @Operation(summary = "[POST] 액세스, 리프레시 토큰 재발급 요청",
            description = "액세스 토큰 만료 시 서버에 액세스, 리프레시 토큰 재발급을 요청합니다. <br>" +
                    "서버에서는 요청 받은 토큰 정보를 토대로 회원 검증 및 리프레쉬 토큰 검증한 후 액세스 / 리프레시 토큰 재발급을 진행합니다")
    @PostMapping("/api/v1/refresh")
    public SingleResult<TokenDto> refresh(
            @Parameter(description = "재발급 요청을 할 토큰 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TokenRequestDto.class))) @RequestBody TokenRequestDto dto) {

        return responseService.getSingleResult(userJoinService.refresh(dto));
    }

    @Operation(summary = "[POST] 이메일 중복 여부 체크 요청",
            description = "회원가입 시 이메일 중복 체크 버튼을 누를 때 호출하는 API이며, 아래와 같이 호출해야 합니다. <br>" +
                    "[PUT] /api/v1/emailCheck?email={email}")
    @PostMapping("/api/v1/emailCheck")
    public ResponseEntity emailCheck(@Parameter(description = "중복 체크가 필요한 이메일") @RequestParam String email) {

        Boolean result = userJoinService.emailDuplicateCheck(email);
        if (!result) {
            return ResponseEntity.ok().body(responseService.getSuccessResult());
        } // DB 조회 시 중복이 아닌 경우
        return ResponseEntity.badRequest()
                .body(responseService
                        .getFailResult(ErrorCode.EMAIL_DUPLICATED.getCode(), ErrorCode.EMAIL_DUPLICATED.getDescription())
                ); // DB 조회 시 중복인 경우
    }

    @Operation(summary = "[POST] 닉네임 중복 여부 체크 요청",
            description = "회원가입 시 닉네임 중복 체크 버튼을 누를 때 호출하는 API이며, 아래와 같이 호출해야 합니다. <br>" +
                    "[PUT] /api/v1/nicknameCheck?nickname={nickname}")
    @PostMapping("/api/v1/nicknameCheck")
    public ResponseEntity nicknameCheck(@Parameter(description = "중복 체크가 필요한 닉네임") @RequestParam String nickname) {

        Boolean result = userJoinService.nicknameDuplicateCheck(nickname);
        if (!result) {
            return ResponseEntity.ok().body(responseService.getSuccessResult());
        } // DB 조회 시 중복이 아닌 경우
        return ResponseEntity.badRequest()
                .body(responseService
                        .getFailResult(ErrorCode.USER_NICKNAME_DUPLICATED.getCode(), ErrorCode.USER_NICKNAME_DUPLICATED.getDescription())
                ); // DB 조회 시 중복인 경우
    }
}
