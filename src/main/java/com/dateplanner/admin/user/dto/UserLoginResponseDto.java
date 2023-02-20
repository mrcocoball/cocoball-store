package com.dateplanner.admin.user.dto;

import com.dateplanner.admin.user.entity.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j(topic = "DTO")
@Getter
public class UserLoginResponseDto {
    private final List<String> roles;
    private final LocalDateTime createdDate;

    public UserLoginResponseDto(User user) {
        this.roles = user.getRoleSet();
        this.createdDate = user.getCreatedAt();
    }
}
