package com.dateplanner.admin.user.dto;

import com.dateplanner.user.entity.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DTO")
@Getter
public class UserResponseDto {

    private final String email;
    private final String nickname;

    public UserResponseDto(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }

}
