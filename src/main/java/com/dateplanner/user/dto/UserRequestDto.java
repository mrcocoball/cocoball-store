package com.dateplanner.user.dto;

import com.dateplanner.user.entity.User;
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
public class UserRequestDto {

    /**
     * 회원 생성용 Dto
     */

    @NotNull
    private String uid;

    @NotNull
    private String password;

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String introduce;

    @Builder
    public UserRequestDto(String uid, String password, String email, String introduce) {
        this.uid = uid;
        this.password = password;
        this.email = email;
        this.introduce = introduce;
    }

    public User toEntity() {
        return User.builder()
                .uid(uid)
                .password(password)
                .email(email)
                .introduce(introduce)
                .build();
    }

}
