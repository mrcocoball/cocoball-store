package com.dateplanner.admin.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class UserLoginRequestDto {

    @Schema(description = "회원 이메일")
    @NotEmpty
    @NotNull
    private String email;

    @Schema(description = "비밀번호")
    @NotEmpty
    @NotNull
    private String password;

}
