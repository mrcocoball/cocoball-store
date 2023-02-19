package com.dateplanner.admin.user.dto;

import com.dateplanner.admin.user.entity.User;
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
    private String nickname;

    @Builder
    public UserRequestDto(String uid, String password, String email, String nickname) {
        this.uid = uid;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }

    public User toEntity() {
        return User.builder()
                .uid(uid)
                .password(password)
                .email(email)
                .nickname(nickname)
                .build();
    }

}
