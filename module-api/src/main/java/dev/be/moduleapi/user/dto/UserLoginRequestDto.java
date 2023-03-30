package dev.be.moduleapi.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserLoginRequestDto {

    @Schema(description = "회원 이메일")
    @NotEmpty(message = "이메일이 입력되어야 합니다")
    @NotNull(message = "이메일이 입력되어야 합니다")
    private String email;

    @Schema(description = "비밀번호")
    @NotEmpty(message = "비밀번호가 입력되어야 합니다")
    @NotNull(message = "비밀번호가 입력되어야 합니다")
    private String password;

}
