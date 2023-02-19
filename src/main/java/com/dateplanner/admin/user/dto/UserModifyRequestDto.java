package com.dateplanner.admin.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Slf4j(topic = "DTO")
@Getter
@Setter
@NoArgsConstructor
public class UserModifyRequestDto {

    /**
     * 회원 수정용 Dto
     */

    @NotNull
    private String password;

    @NotNull
    @NotEmpty
    private String nickname;

    @Builder
    public UserModifyRequestDto(String password, String email, String nickname) {
        this.password = password;
        this.nickname = nickname;
    }
}
