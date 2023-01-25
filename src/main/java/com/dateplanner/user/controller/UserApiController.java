package com.dateplanner.user.controller;

import com.dateplanner.api.model.CommonResult;
import com.dateplanner.api.model.SingleResult;
import com.dateplanner.api.service.ResponseService;
import com.dateplanner.user.dto.UserModifyRequestDto;
import com.dateplanner.user.dto.UserRequestDto;
import com.dateplanner.user.dto.UserResponseDto;
import com.dateplanner.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "UserApiController")
@RequiredArgsConstructor
@RestController
public class UserApiController {

    /**
     * UserService : 회원 CRUD 비즈니스 로직 담당
     * ResponseService : 응답 처리를 위한 컨트롤러 공통 서비스
     */

    private final UserService userService;
    private final ResponseService responseService;


    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "회원 단건 조회(회원 ID)", description = "[GET] 회원 ID로 회원 단건 조회")
    @GetMapping("/api/v1/user/id/{user_id}")
    public SingleResult<UserResponseDto> getUserByUid
            (@Parameter(description = "회원 ID", required = true) @PathVariable("user_id")String uid) {

        return responseService.getSingleResult(userService.findByUid(uid));
    }

    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "회원 단건 조회(이메일)", description = "[GET] 이메일로 회원 단건 조회")
    @GetMapping("/api/v1/user/email/{email}")
    public SingleResult<UserResponseDto> getUserByEmail
            (@Parameter(description = "이메일", required = true) @PathVariable("email")String email) {

        return responseService.getSingleResult(userService.findByEmail(email));
    }

    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "회원 생성", description = "[POST] 회원 생성")
    @PostMapping(value = "/api/v1/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<String> createUser(@Parameter(description = "회원 생성 정보", required = true) @Valid @RequestBody UserRequestDto dto) {

        return responseService.getSingleResult(userService.save(dto));

    }

    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "회원 정보 수정", description = "[PUT] 회원 정보 수정")
    @PutMapping(value = "/api/v1/user/{user_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<String> modifyUser
            (@Parameter(description = "회원 ID", required = true) @PathVariable("user_id") String uid,
            @Parameter(description = "회원 수정 정보", required = true) @Valid @RequestBody UserModifyRequestDto dto) {

        return responseService.getSingleResult(userService.update(uid, dto));

    }

    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "회원 삭제", description = "[DELETE(PUT)] 회원 삭제 처리")
    @PutMapping("/api/v1/user/delete/{user_id}")
    public CommonResult deleteUser
            (@Parameter(description = "회원 ID", required = true) @PathVariable("user_id") String uid) {

        return responseService.getSuccessResult();

    }
}
