package com.dateplanner.user.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class UserLoginRequestDto {

    @NotEmpty
    @NotNull
    private String uid;

    @NotEmpty
    @NotNull
    private String password;

}
